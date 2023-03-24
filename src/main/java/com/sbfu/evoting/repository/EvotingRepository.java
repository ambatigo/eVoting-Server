package com.sbfu.evoting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbfu.evoting.entity.Voter;

@Repository
public interface EvotingRepository extends JpaRepository<Voter, Integer> {
	public List<Voter> findByUserId(String userId);
	public List<Voter> findByUserIdAndPassword(String userId, String password);
}