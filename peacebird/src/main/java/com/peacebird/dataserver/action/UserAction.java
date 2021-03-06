package com.peacebird.dataserver.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;

import com.peacebird.dataserver.model.DimBrand;
import com.peacebird.dataserver.model.User;

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

	private List<DimBrand> brands;
	
	private String keyword;

	public void listUsers() {
		users = this.getUserService().findAllUsers();
		this.brands = this.getDataService().getAllBrands();
	}
	
	public void searchUsers(){
		if(StringUtils.isEmpty(keyword)){
			users = this.getUserService().findAllUsers();
		}else{
			users = this.getUserService().findUsers(keyword);
		}
	}

	public void addUser() {
		this.user = new User();
		brandMap = new HashMap<String, Boolean>();
		for (DimBrand brand : brands) {
			brandMap.put(brand.getName(), false);
		}
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
			this.user.setBrands(StringUtils.join(brands, ","));
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
		brandMap = new HashMap<String, Boolean>();
		for (DimBrand brand : brands) {
			brandMap.put(brand.getName(), false);
		}
		if (StringUtils.isNotEmpty(this.user.getBrands())) {
			for (String brand : this.user.getBrands().split(",")) {
				if (brandMap.containsKey(brand)) {
					brandMap.put(brand, true);
				}
			}
		}
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
	
	public void deleteUser(User user){
		this.getUserService().deleteUser(user);
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

	public Map<String, Boolean> getBrandMap() {
		return brandMap;
	}

	public void setBrandMap(Map<String, Boolean> brandMap) {
		this.brandMap = brandMap;
	}

	public List<DimBrand> getBrands() {
		return brands;
	}

	public void setBrands(List<DimBrand> brands) {
		this.brands = brands;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
