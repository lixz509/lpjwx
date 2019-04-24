package com.lpj.weixin.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lpj.weixin.domain.User;

@Service//把当前类的对象加入Spring中管理
public class WeiXinProxy {
	
	private ObjectMapper objectMapper=new ObjectMapper();
	
	private static final Logger LOG=LoggerFactory.getLogger(WeiXinProxy.class);
	
	@Autowired
	private AccessTokenManager accessTokenManager;
	
	public User getUser(String account,String openId) {
		
		String accessToken =accessTokenManager.getToken(account);
		
		String url="https://api.weixin.qq.com/cgi-bin/user/info"
				+ "?access_token="+accessToken
				+ "&openid="+openId
				+ "&lang=zh_CN";
		
		//创建一个Http客户端，可以重复使用，但此时不考虑重复使用
		HttpClient hc = HttpClient.newBuilder().build();
		//创建请求并已GET方式发送请求
		HttpRequest request =HttpRequest.newBuilder(URI.create(url)).GET().build();
		//发送请求
		try {
			HttpResponse<String> response=hc.send(request, BodyHandlers.ofString(Charset.forName("utf-8")));
			String body=response.body();
			LOG.trace("调用远程接口返回内容：\n{}",body);
			if(!body.contains("errcode")) {
				//成功
				User user= objectMapper.readValue(body, User.class);
				return user;
			}
			
			
		} catch (Exception e) {
			LOG.error("调用远程接口出现错误："+e.getLocalizedMessage(),e);
		} 
		
		return null;
	}

	public void sendText(String account, String openId, String string) {
		// TODO 发送文本信息给指定用户
	}
}
