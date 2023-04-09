package com.sbfu.evoting.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "voter")
public class Voter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    
    @Column(name="name")
    public String name;
    
    @Column(name="email")
    public String email;
    
    @Column(name="userId")
    public String userId;
    
    @Column(name="password")
    public String password;
    
    @Column(name="voterId")
    public Integer voterId;
    
    @Column(name="queryName")
    public String queryName;
    
    @Column(name="queryMsg")
    public String queryMsg;
    
    @Column(name = "otp")
    public int otp;
}
