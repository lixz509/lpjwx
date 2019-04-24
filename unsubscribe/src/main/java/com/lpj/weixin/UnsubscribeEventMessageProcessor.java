package com.lpj.weixin;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lpj.weixin.domain.User;
import com.lpj.weixin.domain.event.EventInMessage;
import com.lpj.weixin.processors.EventMessageProcessor;
import com.lpj.weixin.repository.UserRepository;

//把Bean加入容器管理，默认类名首字母小写作为ID
//如果service注解有值则表示自定义ID
@Service("unsubscribeMessageProcessor")
public class UnsubscribeEventMessageProcessor implements EventMessageProcessor{

	@Autowired
	private UserRepository userRepository;
	
	@Transactional//表示方法要有事务，在事务内调用数据库查询到的对象的set方法，会自动更新到数据库
	public void onMessage(EventInMessage msg) {
		if(msg.getEvent().equals("subscribe")) {
			System.out.println("取消关注消息处理器："+msg);
			//1.解除用户的关注状态
			User user= this.userRepository.findByOpenId(msg.getFromUserName());
			if(user !=null) {
				user.setStatus(User.Status.IS_UNSUBSCRIBE);
				user.setUnsubTime(new Date());
			}
			//一般不删除数据，而是把数据标记为已经取消关注
		}
	}

}
