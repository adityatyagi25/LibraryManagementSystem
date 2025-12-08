package com.librarymanagementsystem.Security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.librarymanagementsystem.Entity.Users;

public class UserPrinciple implements UserDetails
{
    private Users user;
	public UserPrinciple(Users user) {
		this.user=user;
	}

	@Override
	public Set<GrantedAuthority> getAuthorities() {
	    return user.getRoles().stream()
	            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole()))
	            .collect(Collectors.toSet());
	}


	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}   

}
