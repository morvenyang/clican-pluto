package com.ikidstv.quiz.action;

import java.util.Date;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;

import com.ikidstv.quiz.model.User;

@Scope(ScopeType.PAGE)
@Name("userAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class UserAction extends BaseAction {

	private List<User> users;

	private User user;

	@In(required = true)
	FacesMessages statusMessages;

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
		boolean validated = true;
		if (this.user.getId() == null) {
			// we must check the password policy
			if (this.user.getPassword().length() < getSpringProperty()
					.getPasswordMinLength()) {
				this.statusMessages.addToControlFromResourceBundle("password",
						Severity.ERROR, "userPasswordMinLengthValidation",
						getSpringProperty().getPasswordMinLength());
				validated = false;
			}
			if (!this.user.getPassword().equals(this.user.getConfirmedPassword())) {
				this.statusMessages.addToControlFromResourceBundle("confirmedPassword",
						Severity.ERROR, "userPasswordSameWithConfirmedPassword");
				validated = false;
			}
			if(this.getUserService().checkAccountExisted(this.user.getAccount())){
				this.statusMessages.addToControlFromResourceBundle("account",
						Severity.ERROR, "userAccountExisted");
				validated = false;
			}
		}
		if(validated){
			this.getUserService().saveUser(this.user);
			users = this.getUserService().findAllUsers();
		}
	}

	public void editUser(User user) {
		this.user = user;
	}

	public void enableUser(User user) {
		user.setActive(true);
		this.getUserService().saveUser(user);
		users = this.getUserService().findAllUsers();
	}

	public void disableUser(User user) {
		user.setActive(false);
		this.getUserService().saveUser(user);
		users = this.getUserService().findAllUsers();
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
