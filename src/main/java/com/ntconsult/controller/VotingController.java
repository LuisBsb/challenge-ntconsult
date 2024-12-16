package com.ntconsult.controller;

import com.ntconsult.entity.Topic;
import com.ntconsult.entity.Vote;
import com.ntconsult.service.VotingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes")
@AllArgsConstructor
public class VotingController {

    private final VotingService votingService;

    @PostMapping("/topics")
    public Topic createTopic(@RequestBody Topic topic) {
        return votingService.createTopic(topic);
    }

    @PostMapping("/topics/{id}/votes")
    public String vote(@PathVariable Long id, @RequestBody Vote vote) {
        return votingService.vote(id, vote);
    }

    @PostMapping("/topics/{id}/open-session")
    public String openSession(@PathVariable Long id, @RequestParam(required = false, defaultValue = "60") Long durationInSeconds) {
        return votingService.openSession(id, durationInSeconds);
    }

    @GetMapping("/topics/{id}/result")
    public String getResult(@PathVariable Long id) {
        return votingService.getResult(id);

    }
}
