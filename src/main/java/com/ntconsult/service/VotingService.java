package com.ntconsult.service;

import com.ntconsult.entity.Topic;
import com.ntconsult.entity.Vote;
import com.ntconsult.repository.TopicRepository;
import com.ntconsult.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@AllArgsConstructor
@Service
public class VotingService {

    private final TopicRepository topicRepository;
    private final VoteRepository voteRepository;
    private final CPFValidationService cpfValidationService;
    private final MessagingService messagingService;

    public Topic createTopic(Topic topic){
        return topicRepository.save(topic);
    }

    public String vote(Long id, Vote vote){
        Optional<Topic> topicOpt = topicRepository.findById(id);

        if (topicOpt.isEmpty() || !topicOpt.get().isSessionOpen()) {
            return "Voting session is closed or topic not found";
        }

        if (voteRepository.existsByAssociateIdAndTopicId(vote.getAssociateId(), id)) {
            return "Associate has already voted";
        }

//        if (!cpfValidationService.canVote(vote.getAssociateId())) {
//            return "Associate not allowed to vote";
//        }

        vote.setTopic(topicOpt.get());
        voteRepository.save(vote);
        return "Vote registered";
    }

    public String openSession(Long id, Long durationInSeconds){
        Optional<Topic> topicOpt = topicRepository.findById(id);
        if (topicOpt.isEmpty()) {
            return "Topic not found";
        }

        Topic topic = topicOpt.get();
        topic.setSessionOpen(true);
        topic.setSessionEndTime(Instant.now().getEpochSecond() + durationInSeconds);
        topicRepository.save(topic);
        return "Voting session opened for topic: " + topic.getName();
    }

    public String getResult(Long id){
        Optional<Topic> topicOpt = topicRepository.findById(id);
        if (topicOpt.isEmpty()) {
            return "Topic not found";
        }

        long yesVotes = topicOpt.get().getVotes().stream().filter(v -> "Sim".equalsIgnoreCase(v.getVote())).count();
        long noVotes = topicOpt.get().getVotes().stream().filter(v -> "Não".equalsIgnoreCase(v.getVote())).count();
        return "Yes: " + yesVotes + ", No: " + noVotes;
    }

    @Scheduled(fixedRate = 10000)
    public void closeExpiredSessions() {
        topicRepository.findAll().stream()
                .filter(topic -> topic.isSessionOpen() && topic.getSessionEndTime() < Instant.now().getEpochSecond())
                .forEach(topic -> {
                    topic.setSessionOpen(false);
                    topicRepository.save(topic);

                    long yesVotes = topic.getVotes().stream().filter(v -> "Sim".equalsIgnoreCase(v.getVote())).count();
                    long noVotes = topic.getVotes().stream().filter(v -> "Não".equalsIgnoreCase(v.getVote())).count();

                    String resultMessage = "Topic: " + topic.getName() + " | Yes: " + yesVotes + " | No: " + noVotes;

                    System.out.println("Sending message to queue: " + resultMessage);

                    //messagingService.sendMessage("voting-results", resultMessage);
                });
    }
}
