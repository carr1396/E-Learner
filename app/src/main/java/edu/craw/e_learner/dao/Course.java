package edu.craw.e_learner.dao;

import java.sql.Timestamp;

/**
 * Created by farid on 3/29/2016.
 */
public class Course {


    private long id;
    private long name;
    private long description;
    private long abbrev;
    private Timestamp updatedAt;

    private Timestamp createdAt;

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


    public Course(long id) {
        this.id = id;
    }

    public Course() {
    }

    public Course(long id, long name, long description, long abbrev) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.abbrev = abbrev;
    }

    public Course(long id, long name, long description, long abbrev, Timestamp updatedAt, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.abbrev = abbrev;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getName() {
        return name;
    }

    public void setName(long name) {
        this.name = name;
    }

    public long getDescription() {
        return description;
    }

    public void setDescription(long description) {
        this.description = description;
    }

    public long getAbbrev() {
        return abbrev;
    }

    public void setAbbrev(long abbrev) {
        this.abbrev = abbrev;
    }
}
