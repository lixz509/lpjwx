package com.lpj.weixin.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lpj.weixin.domain.User;
import com.lpj.weixin.domain.text.TextOutMessage;

@Service//把当前类的对象加入Spring中管理
public class WeiXinProxy {
	
	private ObjectMapper objectMapper=new ObjectMapper();
	
	private static final Logger LOG=LoggerFactory.getLogger(WeiXinProxy.class);
	
	@Autowired
	private AccessTokenManager accessTokenManager;
	
	//创建一个Http客户端，重复使用
	HttpClient httpClient = HttpClient.newBuilder().build();
	
	public User getUser(String account,String openId) {
		
		String accessToken =accessTokenManager.getToken(account);
		
		String url="https://api.weixin.qq.com/cgi-bin/user/info"
				+ "?access_token="+accessToken
				+ "&openid="+openId
				+ "&lang=zh_CN";
		//创建请求并已GET方式发送请求
		HttpRequest request =HttpRequest.newBuilder(URI.create(url)).GET().build();
		//发送请求
		try {
			HttpResponse<String> response=httpClient.send(request
					, BodyHandlers.ofString(Charset.forName("utf-8")));
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

	public void sendText(String account, String openId, String text) {
		//发送消息给客户
		TextOutMessage out = new TextOutMessage(openId, text);
		//一般情况下是放入消息队列来发送的，但是这里为了简单起见，暂不使用消息队列
		try {
			String json = this.objectMapper.writeValueAsString(out);
			LOG.trace("客服接口要发送的消息内容：{}",json);
			String accessToken =accessTokenManager.getToken(account);
			String url="https://api.weixin.qq.com/cgi-bin/message/custom/send"
					+ "?access_token="+accessToken;
			//创建请求并已POST方式发送请求
			HttpRequest request =HttpRequest.newBuilder(URI.create(url))
					.POST(BodyPublishers.ofString(json,Charset.forName("UTF-8"))).build();
			
			HttpResponse<String> response=httpClient
					.send(request, BodyHandlers.ofString(Charset.forName("UTF-8")));
			
			LOG.trace("发送客服消息的结果：{}",response.body());
			
		} catch ( IOException | InterruptedException e) {
			// TODO 自动生成的 catch 块
			LOG.error("发送消息出现问题："+e.getLocalizedMessage(),e);
		}
	}
}
