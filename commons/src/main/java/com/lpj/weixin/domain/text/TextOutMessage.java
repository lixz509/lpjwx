package com.lpj.weixin.domain.text;

import com.lpj.weixin.domain.OutMessage;

public class TextOutMessage extends OutMessage {

	private TextContent text;
	
	public TextOutMessage(String toUser,String text) {
		super(toUser,"text");
		this.text=new TextContent(text);
	}

	public TextContent getText() {
		return text;
	}

	public void setText(TextContent text) {
		this.text = text;
	}
	public static class TextContent{
		private String content; 
		public TextContent(String content){
			this.content=content;
		}
		
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		
	}
	
}
