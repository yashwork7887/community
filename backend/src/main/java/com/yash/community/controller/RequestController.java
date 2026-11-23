package com.yash.community.controller;

import com.yash.community.model.Models.JobRequest;
import com.yash.community.repository.JobRequestRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
public class RequestController {

    private final JobRequestRepository requests;

    public RequestController(JobRequestRepository requests) {
        this.requests = requests;
    }

    @PostMapping("/requests/{requestid}/accept")
    public Map<String, Boolean> accept(@PathVariable long requestid, Authentication auth) {
        JobRequest req = requests.findById(requestid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
        // Only the person the request was sent to may accept it.
        if (!req.sentto().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to accept this request");
        }
        requests.accept(requestid);
        return Map.of("success", true);
    }
}
