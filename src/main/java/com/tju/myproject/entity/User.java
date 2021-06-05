package com.tju.myproject.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class User
{
    private Integer id;
    private String university;
    private String student_id;
    private String name;
    private Integer gender;
    private String faculty;
    private String phone;
    private String id_card;
    private Integer role;
    private String join_date;
    private Integer point;
}
