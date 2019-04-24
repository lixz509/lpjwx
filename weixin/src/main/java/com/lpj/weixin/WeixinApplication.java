package com.lpj.weixin;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class WeixinApplication implements CommonsConfig {
	
	public static void main(String[] args) {
		SpringApplication.run(WeixinApplication.class, args);
	}

}
