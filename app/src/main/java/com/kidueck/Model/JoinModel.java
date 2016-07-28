package com.kidueck.Model;

/**
 * Created by system777 on 2016-07-06.
 */
public class JoinModel {
    String id;
    String password;
    String sex;
    String joinRoot;
    String birth;

    public JoinModel() {
    }

    public JoinModel(String id, String password, String sex, String joinRoot, String birth) {
        this.id = id;
        this.password = password;
        this.sex = sex;
        this.joinRoot = joinRoot;
        this.birth = birth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getJoinRoot() {
        return joinRoot;
    }

    public void setJoinRoot(String joinRoot) {
        this.joinRoot = joinRoot;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }
}
