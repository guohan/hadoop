package com.kit;

public class UserInfo {

	 

	 private Integer userId;

	 private String userName;

	 public Integer getUserId() {

	  return userId;

	 }

	 public void setUserId(Integer userId) {

	  this.userId = userId;

	 }

	 public String getUserName() {

	  return userName;

	 }

	 public void setUserName(String userName) {

	  this.userName = userName;

	 }

	 public UserInfo(Integer userId, String userName) {

	  this.userId = userId;

	  this.userName = userName;

	 }

	}