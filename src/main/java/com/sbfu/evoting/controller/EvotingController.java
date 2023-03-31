package com.sbfu.evoting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sbfu.evoting.model.GetVote;
import com.sbfu.evoting.model.Query;
import com.sbfu.evoting.model.SignIn;
import com.sbfu.evoting.model.Signup;
import com.sbfu.evoting.model.Vote;
import com.sbfu.evoting.service.EvotingService;

@RestController
@CrossOrigin(origins = "*")
public class EvotingController {

	@Autowired
	EvotingService service;

	@PostMapping("signUp")
	public ResponseEntity<String> signUp(@RequestBody Signup signUpRequest) {
		return service.doSignUp(signUpRequest);
	}

	@PostMapping("signIn")
	public ResponseEntity<String> signIn(@RequestBody SignIn signInRequest) {
		return service.doSignIn(signInRequest);
	}
	
	@PostMapping("queryUs")
	public ResponseEntity<String> setQueryMsg(@RequestBody Query query){
		return service.setQueryMsg(query);
	}
	
	@PostMapping("castVote")
	public ResponseEntity<String> castVote(@RequestBody Vote vote){
		return service.castVote(vote);
	}
	
	@GetMapping("fetchVoteStatus/{userId}")
	public ResponseEntity<String> fetchVoteStatus(@PathVariable("userId") String userId){
		return service.fetchVoteStatus(userId);
	}
	
	@GetMapping("fetchTotalVotes")
	public GetVote getCummulativeVote() {
		return service.getCummulativeVote();
	}
	

}
