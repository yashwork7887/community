package com.yash.community.repository;

import com.yash.community.model.Models.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepository {

    private static final RowMapper<Comment> MAPPER = (rs, i) -> new Comment(
            rs.getLong("id"), rs.getLong("postid"), rs.getString("author"), rs.getString("text"));

    private final JdbcTemplate jdbc;

    public CommentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void create(long postId, String author, String text) {
        jdbc.update("INSERT INTO comment (postid, author, text) VALUES (?, ?, ?)", postId, author, text);
    }

    public List<Comment> findByPost(long postId) {
        return jdbc.query("SELECT * FROM comment WHERE postid = ?", MAPPER, postId);
    }

    public List<Comment> findByAuthor(String username) {
        return jdbc.query("SELECT * FROM comment WHERE author = ?", MAPPER, username);
    }
}
