package com.sbfu.evoting.model;

import javax.validation.constraints.NotNull;

public class SignIn {
	
	@NotNull(message="userId cannot be null")
	public String userId;
	
	@NotNull(message="password cannot be null")
	public String password;

}
