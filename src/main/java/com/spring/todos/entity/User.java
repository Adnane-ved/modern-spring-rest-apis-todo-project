package com.spring.todos.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="users")
public class User implements UserDetails { // means we will store users information and use them for authentication


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable=false)
    private Long id;

    @Column(nullable=false)
    private String firstName;

    @Column(nullable=false)
    private String lastName;

    @Column(nullable=false)
    private String email;


    @Column(nullable=false)
    private String password;

    @CreationTimestamp
    @Column(nullable=false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable=false)
    private Date updatedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="user_authorities",joinColumns = @JoinColumn(name="user_id"))
    private List<Authority> authorities;

    // mappedBy => we will get the todos of the current user object from the table owning the relatioship
    // meaning get the users from todos table that contains user_id so we don't have to create a new
    // separate table for joining or adding new column to order_id to user
    // if we don't put mapped , we will either create a new table for joining or add a new column order_id
    // to use table
    // OneToMany and ManyToOne give the same table , we use mapped to prevent to use one table ( the one owning the relationship)
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)  /* if a toddo is deleted from the list it will be orphan it should be delete from toddo table */
    private List<Todo>  todos;


    public User(){}



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
