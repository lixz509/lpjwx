package com.lpj.weixin.domain;

public abstract class ResponseMessage {
	//响应状态值，设置1表示成功，其他表示失败
	private int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
