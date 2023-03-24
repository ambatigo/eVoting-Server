package com.sbfu.evoting.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.sbfu.evoting.entity.Voter;
import com.sbfu.evoting.model.Signup;

@Component
public class EvotingHelper {
	
	public Voter mapRequestToEntity(Signup signupRequest) {
		Voter voter = new Voter();
		voter.email=signupRequest.email;
		voter.name=signupRequest.name;
		voter.userId=signupRequest.userId;
		voter.password = signupRequest.password;
		return voter;
	}
	
}
