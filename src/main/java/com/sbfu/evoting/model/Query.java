package com.sbfu.evoting.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Query {

	@NotNull(message="userId cannot be null")
	public String userId;
	
	@NotNull(message="name cannot be null")
	public String myName;
	
	@NotNull(message="Query message cannot be null")
	@Size(min = 3, max = 100, message = "Query message must be between 3 and 100 characters long") 
	public String msg;
}
