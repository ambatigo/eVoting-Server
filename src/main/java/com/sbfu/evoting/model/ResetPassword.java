package com.sbfu.evoting.model;

import javax.validation.constraints.NotNull;

public class ResetPassword {
	
	@NotNull(message="userId cannot be null")
	public String userId;
	
	@NotNull(message="password cannot be null")
	public String password;
	
	@NotNull(message="otp cannot be null")
	public String otp;

}
