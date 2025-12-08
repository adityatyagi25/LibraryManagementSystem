package com.librarymanagementsystem.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.librarymanagementsystem.Entity.Users;
import com.librarymanagementsystem.Repository.UsersRepository;
@Service
public class CustomUserDetailsService implements UserDetailsService{
	@Autowired
    private UsersRepository usersRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	   Users user=usersRepository.findById(username).orElseThrow(); 
	   return new UserPrinciple(user);
	}

}
