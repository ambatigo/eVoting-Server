package com.sbfu.evoting.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sbfu.evoting.entity.Voter;
import com.sbfu.evoting.model.GetVote;
import com.sbfu.evoting.model.IndividualVote;
import com.sbfu.evoting.model.Query;
import com.sbfu.evoting.model.ResetPassword;
import com.sbfu.evoting.model.SignIn;
import com.sbfu.evoting.model.Signup;
import com.sbfu.evoting.model.Vote;
import com.sbfu.evoting.repository.EvotingHelper;
import com.sbfu.evoting.repository.EvotingRepository;

@Service
public class EvotingService {

	@Autowired
	EvotingRepository repository;

	@Autowired
	EvotingHelper helper;

	@Autowired
	MailService mailService;

	@Value("${sending.mailer.email}")
	private String mailerEmail;

	public ResponseEntity<String> doSignUp(Signup signupRequest) {
		List<Voter> findByUserId = repository.findByUserId(signupRequest.userId);
		if (findByUserId.size() > 0) {
			return ResponseEntity.status(503).body("The userId is already registered");
		}
		Voter voter = helper.mapRequestToEntity(signupRequest);

		try {
			repository.save(voter);
			UUID token = java.util.UUID.randomUUID();
			return ResponseEntity.ok(token.toString());

		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal server error");
		}

	}

	public ResponseEntity<String> doSignIn(SignIn signInRequest) {

		try {
			List<Voter> findByUserId = repository.findByUserIdAndPassword(signInRequest.userId, signInRequest.password);
			if (findByUserId.size() != 1) {
				return ResponseEntity.status(401).body("Invalid Credentials");
			}
			UUID token = java.util.UUID.randomUUID();
			return ResponseEntity.ok(token.toString());

		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal server error");
		}

	}

	public GetVote getCummulativeVote() {
		List<Voter> findTotalVotes = repository.findAll();
		GetVote response = new GetVote();
		List<IndividualVote> totalvotes = new LinkedList<>();
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		findTotalVotes.stream().forEach((vote) -> {
			map.put(vote.voterId, (int) map.getOrDefault(vote.voterId, 0) + 1);
		});
		Integer totalCount = 0;
		for (var entry : map.entrySet()) {
			IndividualVote vote = new IndividualVote();
			vote.voterId = entry.getKey();
			vote.votingCount = entry.getValue();
			totalvotes.add(vote);
			if (entry.getKey() != null) {
				totalCount += entry.getValue();
			}
		}

		response.totalVotes = totalCount;
		response.votes = totalvotes;

		return response;
	}

	public ResponseEntity<String> setQueryMsg(Query query) {
		try {
			List<Voter> findByUserId = repository.findByUserId(query.userId);
			if (findByUserId.size() != 1) {
				return ResponseEntity.status(404).body("The user is not present");
			}
			Voter currentUser = findByUserId.get(0);
			currentUser.queryName = query.myName;
			currentUser.queryMsg = query.msg;
			repository.save(currentUser);
			mailService.sendMail(mailerEmail, currentUser.email, "Query successfully sent.",
					"The query: " + query.msg + " is successfully sent. The organiser will soon resolve your query.");
			mailService.sendMail(mailerEmail, mailerEmail, "A query is sent by " + query.myName,
					"Query : " + query.msg + " .Please respond to the mail " + currentUser.email + " .From userID: "
							+ currentUser.userId);

			return ResponseEntity.ok("Successfully saved the query");

		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal server error");
		}

	}

	public ResponseEntity<String> castVote(Vote vote) {
		try {
			List<Voter> findByUserId = repository.findByUserId(vote.userId);
			if (findByUserId.size() != 1) {
				return ResponseEntity.status(404).body("The user is not present");
			}
			Voter currentUser = findByUserId.get(0);
			if (currentUser.voterId != null) {
				return ResponseEntity.status(500).body("The user has already cast the vote");
			}

			currentUser.voterId = vote.voterId;
			repository.save(currentUser);
			mailService.sendMail(mailerEmail, currentUser.email, "Cast Vote || Student Id :"+currentUser.userId,
					"You have successfully cast your vote. Please login again to confirm your selection.");
			return ResponseEntity.ok("Successfully saved your choice. Thank you.");

		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal server error");
		}

	}

	public ResponseEntity<String> fetchVoteStatus(String userId) {
		try {
			List<Voter> findByUserId = repository.findByUserId(userId);
			if (findByUserId.size() != 1) {
				return ResponseEntity.status(404).body("The user is not present");
			}
			Voter currentUser = findByUserId.get(0);
			if (currentUser.voterId != null) {
				return ResponseEntity.ok(currentUser.voterId.toString());
			} else {
				return ResponseEntity.status(503).body(null);
			}

		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal server error");
		}

	}

	public ResponseEntity<String> generateOTP(String userId) {
		try {
			List<Voter> findByUserId = repository.findByUserId(userId);
			if (findByUserId.size() != 1) {
				return ResponseEntity.status(404).body("The user is not present");
			}
			Voter currentUser = findByUserId.get(0);
			if (currentUser.voterId != null) {
				Random r = new Random(System.currentTimeMillis());
				int otp = ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
				currentUser.otp = otp;
				repository.save(currentUser);
				mailService.sendMail(mailerEmail, currentUser.email, "OTP verfication || Student Id :"+currentUser.userId,
						"The OTP for the reset password : "+currentUser.otp);
				return ResponseEntity.ok("OTP sent successfully. Please check your registered email for OTP.");
			} else {
				return ResponseEntity.status(503).body(null);
			}

		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal server error");
		}

	}

	public ResponseEntity<String> resetOTP(ResetPassword resetPasswordRequest) {
		try {
			List<Voter> findByUserId = repository.findByUserId(resetPasswordRequest.userId);
			if (findByUserId.size() != 1) {
				return ResponseEntity.status(404).body("The user is not present");
			}
			Voter currentUser = findByUserId.get(0);
			if (currentUser.voterId != null) {
				if (currentUser.otp != Integer.parseInt(resetPasswordRequest.otp)) {
					return ResponseEntity.ok("Invalid OTP. Please check your mail to get the correct OTP.");
				}
				currentUser.password = resetPasswordRequest.password;
				repository.save(currentUser);
				mailService.sendMail(mailerEmail, currentUser.email, "Password reset successfully || Student Id: "+currentUser.userId,
						"The password is successfully updated.");
				return ResponseEntity.ok("Password reset successfully");
			} else {
				return ResponseEntity.status(503).body(null);
			}

		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal server error");
		}

	}

}
