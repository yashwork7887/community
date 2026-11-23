package com.yash.community.repository;

import com.yash.community.model.Models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<User> findByUsername(String username) {
        return jdbc.query("SELECT * FROM users WHERE username = ?",
                (rs, i) -> new User(rs.getLong("id"), rs.getString("username"),
                        rs.getString("password"), rs.getString("email")),
                username).stream().findFirst();
    }

    public Optional<Long> findIdByUsername(String username) {
        return jdbc.query("SELECT id FROM users WHERE username = ?",
                (rs, i) -> rs.getLong("id"), username).stream().findFirst();
    }

    public void create(String username, String hashedPassword, String email) {
        jdbc.update("INSERT INTO users (username, password, email) VALUES (?, ?, ?)",
                username, hashedPassword, email);
    }

    public void follow(long userId, long followerId) {
        jdbc.update("INSERT INTO followers (userid, followerid) VALUES (?, ?)", userId, followerId);
    }
}
