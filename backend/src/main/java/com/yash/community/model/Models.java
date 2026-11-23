package com.yash.community.model;

import java.math.BigDecimal;

/**
 * Row models. Field names are lowercase to match the column names the
 * Node/pg version returned, so the JSON the frontend receives is unchanged.
 */
public final class Models {
    private Models() {}

    public record User(long id, String username, String password, String email) {}

    public record Job(long id, String title, String description, BigDecimal pay,
                      String author, String location) {}

    public record Comment(long id, long postid, String author, String text) {}

    public record JobRequest(long id, String sentby, String sentto, boolean accepted, long jobid) {}
}
