package com.itechart.students_lab.waybill_suppliers.config;

import com.itechart.students_lab.waybill_suppliers.entity.ActiveStatus;
import com.itechart.students_lab.waybill_suppliers.entity.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@RequiredArgsConstructor
public class SecurityUser implements UserDetails {

    private final String username;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;
    private final boolean isActive;

//    public SecurityUser(String username, String password, List<SimpleGrantedAuthority> authorities, boolean isActive) {
//        this.username = username;
//        this.password = password;
//        this.authorities = authorities;
//        this.isActive = isActive;
//    }

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

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static UserDetails fromUser(User user){
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(), user.getPassword(),
                user.getActiveStatus().equals(ActiveStatus.ACTIVE),
                user.getActiveStatus().equals(ActiveStatus.ACTIVE),
                user.getActiveStatus().equals(ActiveStatus.ACTIVE),
                user.getActiveStatus().equals(ActiveStatus.ACTIVE),
                user.getRole().getAuthorities()
        );
    }
}
