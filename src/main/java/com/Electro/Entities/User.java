package com.Electro.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "user")
/**
 *
 */
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    private String address;
    private String phone;
    private String gender;

    private String birth;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private Cart cart;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Invoice> invoices;

    public List<Invoice> getInvoices() {
    return invoices;
}

    public void setInvoices(List<Invoice> invoices) {
    this.invoices = invoices;
}

    public Cart getCart() {
    return cart;
}

    public void setCart(Cart cart) {
    this.cart = cart;
}

    private String role;

    public User() {
}

    public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
}

    public User(String username, String email, String password, String address, String phone, String gender, String birth) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.address = address;
    this.phone = phone;
    this.gender = gender;
    this.birth = birth;
}

    public User(String username, String email, String password, String address, String phone, String gender, String birth
        , String role
) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.address = address;
    this.phone = phone;
    this.gender = gender;
    this.birth = birth;
    this.role = role;
}


    public Long getId() {
    return id;
}

    public void setId(Long id) {
    this.id = id;
}

    public String getUsername() {
    return username;
}

    public void setUsername(String username) {
    this.username = username;
}

    public String getEmail() {
    return email;
}

    public void setEmail(String email) {
    this.email = email;
}

    public String getPassword() {
    return password;
}

    public void setPassword(String password) {
    this.password = password;
}

    public Set<Role> getRoles() {
    return roles;
}

    public void setRoles(Set<Role> roles) {
    this.roles = roles;
}

    public String getAddress() {
    return address;
}

    public void setAddress(String address) {
    this.address = address;
}

    public String getPhone() {
    return phone;
}

    public void setPhone(String phone) {
    this.phone = phone;
}

    public String getGender() {
    return gender;
}

    public void setGender(String gender) {
    this.gender = gender;
}

    public String getBirth() {
    return birth;
}

    public void setBirth(String birth) {
    this.birth = birth;
}
    public String getRole(){
    return role;
    }
}
