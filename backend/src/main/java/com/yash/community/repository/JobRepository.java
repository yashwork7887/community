package com.yash.community.repository;

import com.yash.community.model.Models.Job;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class JobRepository {

    private static final RowMapper<Job> MAPPER = (rs, i) -> new Job(
            rs.getLong("id"), rs.getString("title"), rs.getString("description"),
            rs.getBigDecimal("pay"), rs.getString("author"), rs.getString("location"));

    private final JdbcTemplate jdbc;

    public JobRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void create(String title, String description, BigDecimal pay, String author, String location) {
        jdbc.update("INSERT INTO job (title, description, pay, author, location) VALUES (?, ?, ?, ?, ?)",
                title, description, pay, author, location);
    }

    public List<Job> findAll() {
        return jdbc.query("SELECT * FROM job", MAPPER);
    }

    public Optional<Job> findById(long id) {
        return jdbc.query("SELECT * FROM job WHERE id = ?", MAPPER, id).stream().findFirst();
    }

    public List<Job> findByAuthor(String username) {
        return jdbc.query("SELECT * FROM job WHERE author = ?", MAPPER, username);
    }
}
