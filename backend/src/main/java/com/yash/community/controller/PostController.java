package com.yash.community.controller;

import com.yash.community.model.Models.Comment;
import com.yash.community.model.Models.Job;
import com.yash.community.repository.CommentRepository;
import com.yash.community.repository.JobRepository;
import com.yash.community.repository.JobRequestRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
public class PostController {

    public record CreatePostRequest(String title, String description, BigDecimal pay, String location) {}
    public record CommentRequest(String text) {}
    public record PostWithComments(Job post, List<Comment> comments) {}

    private final JobRepository jobs;
    private final CommentRepository comments;
    private final JobRequestRepository requests;

    public PostController(JobRepository jobs, CommentRepository comments, JobRequestRepository requests) {
        this.jobs = jobs;
        this.comments = comments;
        this.requests = requests;
    }

    @GetMapping("/posts")
    public List<Job> getPosts() {
        return jobs.findAll();
    }

    @GetMapping("/posts/{postid}")
    public PostWithComments getPost(@PathVariable long postid) {
        Job post = jobs.findById(postid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return new PostWithComments(post, comments.findByPost(post.id()));
    }

    @PostMapping("/create-post")
    public Map<String, Boolean> createPost(@RequestBody CreatePostRequest req, Authentication auth) {
        jobs.create(req.title(), req.description(), req.pay(), auth.getName(), req.location());
        return Map.of("success", true);
    }

    @PostMapping("/posts/{postid}/comments")
    public Map<String, Boolean> addComment(@PathVariable long postid, @RequestBody CommentRequest req,
                                           Authentication auth) {
        comments.create(postid, auth.getName(), req.text());
        return Map.of("success", true);
    }

    @PostMapping("/posts/{postid}/request")
    public Map<String, Boolean> requestJob(@PathVariable long postid, Authentication auth) {
        Job post = jobs.findById(postid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        requests.create(auth.getName(), post.author(), post.id());
        return Map.of("success", true);
    }
}
