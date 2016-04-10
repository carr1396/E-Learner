package edu.craw.e_learner.dao;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by farid on 3/18/2016.
 */
public class History {
    private int id;
    private String log;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public History(int id, String log, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.log = log;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public History(String log) {
        this.log = log;
    }

    public History() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}

