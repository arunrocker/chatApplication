package com.arun.chat.application;

import java.util.HashMap;
import java.util.Map;

public class UserCtx {
	private static UserCtx userCtx;
	private static Map<Integer,String> users;
	private static Integer userId =0;
	private UserCtx() {
        // private constructor to prevent instantiation
    }
    public static UserCtx getInstance() {
        // Check if the instance is already created
        if (userCtx == null) {
            // If not, create the instance
        	userCtx = new UserCtx();
        	users = new HashMap<>();
        }
        return userCtx;
    }
	public void setUser(String user) {
		users.put(++userId,user);
	}
	public String getUser(int userId) {
		return users.get(userId);
	}
	public Integer getId() {
		return userId;
	}

}
