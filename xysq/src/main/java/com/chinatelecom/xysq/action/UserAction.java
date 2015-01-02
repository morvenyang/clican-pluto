package com.chinatelecom.xysq.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;

import com.chinatelecom.xysq.model.User;

@Scope(ScopeType.PAGE)
@Name("userAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class UserAction extends BaseAction {

	private List<User> users;

	private User user;

	@In(required = true)
	FacesMessages statusMessages;

	private String password;
	private String confirmedPassword;

	private Map<String, Boolean> brandMap;

	public void listUsers() {
		users = this.getUserService().findAllUsers();
	}

	public void addUser() {
		this.user = new User();
	}

	public void saveUser() {
		boolean validated = true;
		if (this.user.getId() == null) {
			if (!this.user.getPassword().equals(
					this.user.getConfirmedPassword())) {
				this.statusMessages.addToControlFromResourceBundle(
						"confirmedPassword", Severity.ERROR,
						"userPasswordSameWithConfirmedPassword");
				validated = false;
			}
			if (this.getUserService().findUserByUserName(
					this.user.getUserName()) != null) {
				this.statusMessages.addToControlFromResourceBundle("userName",
						Severity.ERROR, "userNameExisted");
				validated = false;
			}
		}
		if (validated) {
			List<String> brands = new ArrayList<String>();
			for (String brand : brandMap.keySet()) {
				Boolean select = brandMap.get(brand);
				if (select) {
					brands.add(brand);
				}
			}
			this.getUserService().saveUser(this.user);
			users = this.getUserService().findAllUsers();
		}
	}

	public void changePassword(User user) {
		this.user = user;
	}

	public void updatePassword() {
		boolean validated = true;
		// we must check the password policy

		if (!this.getPassword().equals(this.getConfirmedPassword())) {
			this.statusMessages.addToControlFromResourceBundle(
					"cpPonfirmedPassword", Severity.ERROR,
					"userPasswordSameWithConfirmedPassword");
			validated = false;
		}
		if (validated) {
			this.user.setPassword(this.getPassword());
			this.getUserService().updatePassword(this.user);
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmedPassword() {
		return confirmedPassword;
	}

	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}

}