package com.springBoot.userApp.main.constant;

import org.springframework.stereotype.Component;

@Component
public class AppConstants {
	public static final String CANDIDATE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	public static final String REG_SUCCESS = "userRegSucc";
	public static final String REG_FAILED = "userRegFaild";
	private AppConstants() {
		
	}

}
