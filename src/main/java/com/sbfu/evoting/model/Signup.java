package com.sbfu.evoting.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class Signup {
	
	@NotNull(message="name cannot be null")
	public String name;
	
	@NotNull(message="email cannot be null")
	@Email(message="email is invalid")
	public String email;
	
	@NotNull(message="userId cannot be null")
	public String userId;
	
	@NotNull(message="password cannot be null")
	public String password;

}
