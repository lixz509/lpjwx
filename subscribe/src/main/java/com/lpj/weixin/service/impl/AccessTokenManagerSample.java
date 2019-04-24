package com.lpj.weixin.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lpj.weixin.domain.ResponseError;
import com.lpj.weixin.domain.ResponseMessage;
import com.lpj.weixin.domain.ResponseToken;
import com.lpj.weixin.service.AccessTokenManager;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;

import org.springframework.stereotype.Service;

@Service
public class AccessTokenManagerSample implements AccessTokenManager {
	
	private ObjectMapper objectMapper=new ObjectMapper();

	public String getToken(String account) throws RuntimeException {
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
		//发送请求
		try {
			HttpResponse<String> response=hc.send(request, BodyHandlers.ofString(Charset.forName("utf-8")));
			String body=response.body();
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
				return ((ResponseToken) msg).getToken();
			}
			
		} catch (Exception e) {
			throw new RuntimeException("获取访问令牌出错："+e.getLocalizedMessage(),e);
		} 
		throw new RuntimeException("获取访问令牌出错，错误代码="+((ResponseError) msg).getErrorCode()
				+"错误信息="+((ResponseError) msg).getErrorMessage());
	}

}
