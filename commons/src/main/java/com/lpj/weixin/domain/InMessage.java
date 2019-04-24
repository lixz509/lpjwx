package com.lpj.weixin.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class InMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//Xml开头的注解都为JAXB
	@XmlElement(name="ToUserName")
	@JsonProperty("ToUserName")
	private String toUserName;
	
	@XmlElement(name="FromUserName")
	@JsonProperty("FromUserName")
	private String fromUserName;
	
	@XmlElement(name="CreateTime")
	@JsonProperty("CreateTime")
	private long createTime;
	
	@XmlElement(name="MsgType")
	@JsonProperty("MsgType")
	private String msgType;
	
	@XmlElement(name="MsgId")
	@JsonProperty("MsgId")
	private long msgId;

	public String getToUserName() {
		return toUserName;
	}


	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}


	public String getFromUserName() {
		return fromUserName;
	}


	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}


	public long getCreateTime() {
		return createTime;
	}


	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}


	public String getMsgType() {
		return msgType;
	}


	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}


	public long getMsgId() {
		return msgId;
	}


	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	@Override
	abstract public String toString();

}
