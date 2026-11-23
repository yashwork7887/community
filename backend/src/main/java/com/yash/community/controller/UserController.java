package com.yash.community.controller;

import com.yash.community.model.Models.Comment;
import com.yash.community.model.Models.Job;
import com.yash.community.model.Models.JobRequest;
import com.yash.community.repository.CommentRepository;
import com.yash.community.repository.JobRepository;
import com.yash.community.repository.JobRequestRepository;
import com.yash.community.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    public record RequestsResponse(List<JobRequest> sent, List<JobRequest> received) {}

    private final UserRepository users;
    private final JobRepository jobs;
    private final CommentRepository comments;
    private final JobRequestRepository requests;

    public UserController(UserRepository users, JobRepository jobs, CommentRepository comments,
                          JobRequestRepository requests) {
        this.users = users;
        this.jobs = jobs;
        this.comments = comments;
        this.requests = requests;
    }

    @GetMapping("/users/{user}/posts")
    public List<Job> posts(@PathVariable("user") String user) {
        return jobs.findByAuthor(user);
    }

    @GetMapping("/users/{user}/comments")
    public List<Comment> comments(@PathVariable("user") String user) {
        return comments.findByAuthor(user);
    }

    @GetMapping("/users/{user}/requests")
    public RequestsResponse requests(@PathVariable("user") String user) {
        return new RequestsResponse(requests.findSent(user), requests.findReceived(user));
    }

    @PostMapping("/users/{user}/follow")
    public ResponseEntity<Map<String, String>> follow(@PathVariable("user") String target, Authentication auth) {
        try {
            long targetId = users.findIdByUsername(target)
                    .orElseThrow(() -> new IllegalStateException("User not found"));
            long followerId = users.findIdByUsername(auth.getName())
                    .orElseThrow(() -> new IllegalStateException("User not found"));
            users.follow(targetId, followerId);
            return ResponseEntity.ok(Map.of("message", "Followed successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to follow user"));
        }
    }
}
