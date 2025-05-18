package com.Electro.Service.Impl;

import com.Electro.Entities.Cart;
import com.Electro.Entities.User;
import com.Electro.payload.respone.JwtResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;


public class UserDetailsImpl implements UserDetails {

    private Long id;
    private String username;
    private String email;
    private String password;
    private String address;
    private String phone;
    private String gender;
    private String birth;
    private Cart cart;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    // Factory method để tạo từ entity User
    public static UserDetailsImpl build(User user) {
        Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))  // Chuyển enum ERole thành String
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    // Các phương thức bắt buộc override từ UserDetails

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
        return username;
    }

    // Bạn có thể tùy chỉnh các logic dưới đây tùy ý
    @Override
    public boolean isAccountNonExpired() {
        return true;  // Tạm để true, tức tài khoản chưa hết hạn
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Tạm để true, tức tài khoản chưa bị khóa
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Tạm để true, tức mật khẩu chưa hết hạn
    }

    @Override
    public boolean isEnabled() {
        return true;  // Tạm để true, tức tài khoản đang được kích hoạt
    }

    // Nếu muốn có getter cho id và email
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        // Giả sử user chỉ có 1 role chính, lấy role đầu tiên trong authorities
        if (authorities != null && !authorities.isEmpty()) {
            return authorities.iterator().next().getAuthority();
        }
        return null;
    }

    public Cart getCart() {
        return cart;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getBirth() {
        return birth;
    }
}
