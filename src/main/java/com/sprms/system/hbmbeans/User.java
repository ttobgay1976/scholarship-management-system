package com.sprms.system.hbmbeans;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_users")
public class User implements Serializable {

	// listing the user entity
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="USER_ID")
	private Long Id;

	@Column(name = "USER_FIRST_NAME")
	private String firstname;
	@Column(name = "USER_MIDDLE_NAME")
	private String middlename;
	@Column(name = "USER_LAST_NAME")
	private String lastname;
	@Column(name = "USER_CID_NO")
	private String cidno;
	@Column(name = "USER_CONTACT_NO")
	private String contactno;
	@Column(name = "USER_LOGIN_ID")
	private String username;
	@Column(name = "USER_LOGIN_PWD")
	private String password;
	@Column(name = "CREATED_AT")
	private LocalDateTime createdat;
	@Column(name = "UPDATED_AT")
	private LocalDateTime updateat;
	@Column(name = "STATUS")
	private int status;

	// 🔥 User → UserRoles (One-to-Many)
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UserRoles> userRoles = new HashSet<>();

	// Constructors
	public User() {
	}

	// Helper methods (VERY IMPORTANT)
	public void addRole(Role role) {
		UserRoles userRole = new UserRoles();
		userRole.setUser(this);
		userRole.setRole(role);
		userRoles.add(userRole);
	}

	public void removeRole(Role role) {
		userRoles.removeIf(ur -> ur.getRole().equals(role));
	}



	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getMiddlename() {
		return middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getCidno() {
		return cidno;
	}

	public void setCidno(String cidno) {
		this.cidno = cidno;
	}

	public String getContactno() {
		return contactno;
	}

	public void setContactno(String contactno) {
		this.contactno = contactno;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDateTime getCreatedat() {
		return createdat;
	}

	public void setCreatedat(LocalDateTime createdat) {
		this.createdat = createdat;
	}

	public LocalDateTime getUpdateat() {
		return updateat;
	}

	public void setUpdateat(LocalDateTime updateat) {
		this.updateat = updateat;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Set<UserRoles> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRoles> userRoles) {
		this.userRoles = userRoles;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}



}
