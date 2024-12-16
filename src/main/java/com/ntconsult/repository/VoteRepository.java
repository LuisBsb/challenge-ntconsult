package com.ntconsult.repository;

import com.ntconsult.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByAssociateIdAndTopicId(String associateId, Long topicId);
}