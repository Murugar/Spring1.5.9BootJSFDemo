package com.iqmsoft;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@ManagedBean(eager = true)
@SessionScoped
@Component("bean")
@Scope(FacesViewScope.NAME)
public class UserBean {

	private String username;
	private String password;

	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void display() {
	
	System.out.println("display");
	
	
	}

}
