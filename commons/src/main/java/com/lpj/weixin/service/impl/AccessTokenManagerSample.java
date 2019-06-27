package com.lpj.weixin.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lpj.weixin.domain.ResponseError;
import com.lpj.weixin.domain.ResponseMessage;
import com.lpj.weixin.domain.ResponseToken;
import com.lpj.weixin.service.AccessTokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenManagerSample implements AccessTokenManager {
	
	private static final Logger LOG=LoggerFactory.getLogger(AccessTokenManagerSample.class);
	
	private ObjectMapper objectMapper=new ObjectMapper();

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String,ResponseToken> redisTemplate;
	
	public String getToken(String account) throws RuntimeException {
		String key="wx_access_token";
		ResponseToken token= redisTemplate.boundValueOps(key).get();
		if(token == null) {
			LOG.trace("数据库中没有令牌，需要重新获取，正在获取分布式事务锁");
			//加上分布式事务锁
			Boolean locked = redisTemplate.boundValueOps(key+"_lock")
					//如果数据库里面本身已经有对应的key，那么不能再增加相同的key，会等待指定时间
					.setIfAbsent(new ResponseToken(),1,TimeUnit.MINUTES); 
			LOG.trace("获取分布式事务锁结束，结果{}",locked);
			try{
				if(locked !=null && locked == true) {
					token = redisTemplate.boundValueOps(key).get();
					if(token==null) {
						LOG.trace("调用远程接口获取令牌");
						//再次获取令牌，还是没有令牌，此时就要调用远程接口
						token = getRemoteToken(account);
						//把获取到的令牌存储到数据库
						redisTemplate.boundValueOps(key).set(token,token.getExpiresIn()-100,
								TimeUnit.SECONDS);
					}
				}
			}finally {
				//删除分布式事务锁
				redisTemplate.delete(key+"_lock");
			}
		} else {
			throw new RuntimeException("没有获得事务锁，无法更新令牌");
		}
		return token.getToken();
	}
	
	private ResponseToken getRemoteToken(String account){

		//此时不管过期问题，也不管开发者的身份问题，调用此方法，总是获取一个新的令牌
//		公众号使用的，由于权限不够，暂不使用
//		String appid="wx9e1bc1182bdf8864";
//		String appSecret="db0f2733a1f585f3f23dd5b626fe7031";
		//测试号使用
		String appid="wx656999f734cc7d7b";
		String appSecret="6baaabdfa5bfa88b0f764f6cc89be919";
		String url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"
				+ "&appid="+appid
				+ "&secret="+appSecret;
		//创建一个Http客户端，可以重复使用，但此时不考虑重复使用
		HttpClient hc = HttpClient.newBuilder().build();
		//创建请求并已GET方式发送请求
		HttpRequest request =HttpRequest.newBuilder(URI.create(url)).GET().build();
		ResponseMessage msg;
		
		try {
			//发送请求
			//BodyHandlers里面包含了一系列的响应体处理程序，能够把响应体转换为需要的数据类型
			//ofString表示转换为String类型的数据
			//Charset.forName("utf-8")表示使用UTF-8的编码转换数据
			HttpResponse<String> response=hc.send(request,
					BodyHandlers.ofString(Charset.forName("utf-8")));
			String body=response.body();
			LOG.trace("调用远程接口的返回值:\n{}",body);
			if(body.contains("errcode")) {
				//错误
				msg = objectMapper.readValue(body, ResponseError.class);
				msg.setStatus(2);
			}else {
				//成功
				msg = objectMapper.readValue(body, ResponseToken.class);
				msg.setStatus(1);
			}
			
			if(msg.getStatus() == 1){
				//成功返回令牌
				return (ResponseToken) msg;
			}
			
		} catch (Exception e) {
			throw new RuntimeException("获取访问令牌出错："+e.getLocalizedMessage(),e);
		} 
		throw new RuntimeException("获取访问令牌出错，错误代码="+((ResponseError) msg).getErrorCode()
				+"错误信息="+((ResponseError) msg).getErrorMessage());
	}

}
