package com.tassel.util;

import com.tassel.entity.User;
import org.springframework.stereotype.Component;

/**
 * @Description 持有用户信息，代替 Session 对象 ---线程隔离
 * @Author Zhang Shuaiyin
 * @Date 2020/07/12
 */
@Component
public class HostHolder {

	/**
	 * 线程隔离
	 */
	private ThreadLocal<User> users = new ThreadLocal<>();

	public void setUser(User user) {
		users.set(user);
	}

	public User getUser() {
		return users.get();
	}

	public void clear() {
		users.remove();
	}
}
