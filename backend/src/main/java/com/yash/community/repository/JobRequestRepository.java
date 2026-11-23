package com.yash.community.repository;

import com.yash.community.model.Models.JobRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JobRequestRepository {

    private static final RowMapper<JobRequest> MAPPER = (rs, i) -> new JobRequest(
            rs.getLong("id"), rs.getString("sentby"), rs.getString("sentto"),
            rs.getBoolean("accepted"), rs.getLong("jobid"));

    private final JdbcTemplate jdbc;

    public JobRequestRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void create(String sender, String receiver, long jobId) {
        jdbc.update("INSERT INTO jobrequest (sentby, sentto, accepted, jobid) VALUES (?, ?, ?, ?)",
                sender, receiver, false, jobId);
    }

    public Optional<JobRequest> findById(long id) {
        return jdbc.query("SELECT * FROM jobrequest WHERE id = ?", MAPPER, id).stream().findFirst();
    }

    public void accept(long requestId) {
        jdbc.update("UPDATE jobrequest SET accepted = true WHERE id = ?", requestId);
    }

    public List<JobRequest> findReceived(String username) {
        return jdbc.query("SELECT * FROM jobrequest WHERE sentto = ?", MAPPER, username);
    }

    public List<JobRequest> findSent(String username) {
        return jdbc.query("SELECT * FROM jobrequest WHERE sentby = ?", MAPPER, username);
    }
}
