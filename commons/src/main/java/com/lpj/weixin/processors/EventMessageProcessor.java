package com.lpj.weixin.processors;

import com.lpj.weixin.domain.event.EventInMessage;

public interface EventMessageProcessor {
	void onMessage(EventInMessage msg);
}
