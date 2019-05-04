package com.lpj.weixin.domain.text;

import com.lpj.weixin.domain.OutMessage;

public class TextOutMessage extends OutMessage {

	private String text;
	
	public TextOutMessage(String toUser,String text) {
		super(toUser,"text");
		this.text=text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
