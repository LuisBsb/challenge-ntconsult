package com.ntconsult.service;

import com.ntconsult.entity.Topic;
import com.ntconsult.entity.Vote;
import com.ntconsult.repository.TopicRepository;
import com.ntconsult.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VotingServiceTest {

    @InjectMocks
    private VotingService votingService;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private VoteRepository voteRepository;

    private Topic topic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        topic = new Topic();
        topic.setId(1L);
        topic.setName("Test Topic");
        topic.setSessionOpen(false);
        topic.setSessionEndTime(Instant.now().getEpochSecond() + 60);
    }

    @Test
    void shouldCreateTopic() {
        when(topicRepository.save(any(Topic.class))).thenReturn(topic);

        Topic createdTopic = votingService.createTopic(topic);

        assertNotNull(createdTopic);
        assertEquals("Test Topic", createdTopic.getName());
        verify(topicRepository, times(1)).save(topic);
    }

    @Test
    void shouldOpenSession() {
        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        when(topicRepository.save(any(Topic.class))).thenReturn(topic);

        String result = votingService.openSession(1L, 120L);

        assertEquals("Voting session opened for topic: Test Topic", result);
        verify(topicRepository, times(1)).findById(1L);
        verify(topicRepository, times(1)).save(topic);
        assertTrue(topic.isSessionOpen());
    }

    @Test
    void shouldRegisterVote() {
        Vote vote = new Vote();
        vote.setAssociateId("12345678901");
        vote.setVote("Sim");

        topic.setSessionOpen(true);
        when(topicRepository.findAll()).thenReturn(List.of(topic));
        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        when(voteRepository.existsByAssociateIdAndTopicId("12345678901", 1L)).thenReturn(false);
        when(voteRepository.save(any(Vote.class))).thenReturn(vote);

        String result = votingService.vote(1L, vote);

        assertEquals("Vote registered", result);
        verify(voteRepository, times(1)).existsByAssociateIdAndTopicId("12345678901", 1L);
        verify(voteRepository, times(1)).save(vote);
    }

    @Test
    void shouldNotAllowDuplicateVote() {
        Vote vote = new Vote();
        vote.setAssociateId("12345678901");
        vote.setVote("Sim");

        topic.setSessionOpen(true);
        when(topicRepository.findAll()).thenReturn(List.of(topic));
        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        when(voteRepository.existsByAssociateIdAndTopicId("12345678901", 1L)).thenReturn(true);

        String result = votingService.vote(1L, vote);

        assertEquals("Associate has already voted", result);
        verify(voteRepository, times(1)).existsByAssociateIdAndTopicId("12345678901", 1L);
        verify(voteRepository, never()).save(any(Vote.class));
    }
}
