package org.solteh.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.*;

@Entity
@Table(name = "Users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements Serializable {
	private static final long serialVersionUID = -812013033671051986L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "User_Name", length = 20, unique = true, nullable = false)
	private String userName;

	@Column(name = "Encryted_Password", length = 128, nullable = false)
	private String encrytedPassword;
	@Column(name = "email", unique = true)
	private String email;

	@Enumerated
	@Column(name = "User_State")
	private UserState userState;

	@Column
	private String fio;
	@Column
	private String address;
	@Column
	private String phone;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEncrytedPassword() {
		return encrytedPassword;
	}

	public void setEncrytedPassword(String encrytedPassword) {
		this.encrytedPassword = encrytedPassword;
	}

	public UserState getUserState() {
		return userState;
	}

	public void setUserState(UserState userState) {
		this.userState = userState;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFio() {
		return fio;
	}

	public void setFio(String fio) {
		this.fio = fio;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isCorrectPersonalInfo() {
		return getAddress() != null && !getAddress().isEmpty() &&
				getFio() != null && !getFio().isEmpty() &&
				getEmail() != null && !getEmail().isEmpty() &&
				getPhone() != null && !getPhone().isEmpty();
	}
}