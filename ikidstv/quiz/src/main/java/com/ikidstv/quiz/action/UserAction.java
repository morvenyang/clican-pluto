package com.ikidstv.quiz.action;

import java.util.Date;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;

import com.ikidstv.quiz.model.User;

@Scope(ScopeType.PAGE)
@Name("userAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class UserAction extends BaseAction {

	private List<User> users;

	private User user;

	public void listUsers() {
		users = this.getUserService().findAllUsers();
	}

	public void addUser() {
		this.user = new User();
	}

	public void saveUser() {
		if (this.user.getCreateTime() == null) {
			this.user.setCreateTime(new Date());
		}
		this.getUserService().saveUser(this.user);
	}

	public void editUser(User user) {
		this.user = user;
	}
	
	public void enableUser(User user) {
		user.setActive(true);
		this.getUserService().saveUser(this.user);
	}
	
	public void disableUser(User user) {
		user.setActive(false);
		this.getUserService().saveUser(this.user);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<User> getUsers() {
		return users;
	}

}
