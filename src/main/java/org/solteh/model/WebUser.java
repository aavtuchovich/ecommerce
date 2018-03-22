package org.solteh.model;


import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

/**
 * The website visitor, considered not logged in yet.
 */
@Entity
@Table(name = "users",uniqueConstraints = {@UniqueConstraint(columnNames = "LOGIN")})
public class WebUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_ID")
    private int id;
    @Column(name = "LOGIN",nullable = false,length = 100)
    private String login;
    //Should be encrypted
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @Column(nullable = false,length = 200)
    private String password;
    @Enumerated(EnumType.ORDINAL)
    @Column
    private UserState state;
    @OneToOne(cascade = CascadeType.ALL)
    private Customer customer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}

