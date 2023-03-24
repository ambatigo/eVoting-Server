package com.sbfu.evoting.model;

import javax.validation.constraints.NotNull;

public class Vote {
	
	@NotNull(message="userId cannot be null")
	public String userId;
	
	@NotNull(message="voteId cannot be null")
	public Integer voterId;

}
