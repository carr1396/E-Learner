package edu.craw.e_learner.dao;

import java.sql.Timestamp;

/**
 * Created by farid on 3/29/2016.
 */
public class Course {


    private long id;
    private long networkId;
    private String name;
    private String description;
    private String abbrev;
    private String code;
    private Timestamp updatedAt;
    private Timestamp createdAt;
    private long addedId;
    private long schoolId;
    private String addedUsername;
    private String schoolName;

    public Course(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Course(long id) {
        this.id = id;
    }

    public Course() {
    }

    public Course(long networkId, String name, String description, String abbrev, String code, Timestamp updatedAt, Timestamp createdAt, long addedId, long schoolId, String addedUsername, String schoolName) {
        this.networkId = networkId;
        this.name = name;
        this.description = description;
        this.abbrev = abbrev;
        this.code = code;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.addedId = addedId;
        this.schoolId = schoolId;
        this.addedUsername = addedUsername;
        this.schoolName = schoolName;
    }

    public Course(long id, long networkId, String name, String description, String abbrev, String code, Timestamp updatedAt, Timestamp createdAt, long addedId, long schoolId, String addedUsername, String schoolName) {
        this.id = id;
        this.networkId = networkId;
        this.name = name;
        this.description = description;
        this.abbrev = abbrev;
        this.code = code;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.addedId = addedId;
        this.schoolId = schoolId;
        this.addedUsername = addedUsername;
        this.schoolName = schoolName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public long getAddedId() {
        return addedId;
    }

    public void setAddedId(long addedId) {
        this.addedId = addedId;
    }

    public long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(long schoolId) {
        this.schoolId = schoolId;
    }

    public String getAddedUsername() {
        return addedUsername;
    }

    public void setAddedUsername(String addedUsername) {
        this.addedUsername = addedUsername;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }


    public long getNetworkId() {
        return networkId;
    }

    public void setNetworkId(long networkId) {
        this.networkId = networkId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
