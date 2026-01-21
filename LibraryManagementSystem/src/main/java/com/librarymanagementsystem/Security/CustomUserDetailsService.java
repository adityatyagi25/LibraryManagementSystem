package com.librarymanagementsystem.Security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.librarymanagementsystem.Entity.Users;
import com.librarymanagementsystem.Repository.UsersRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UsersRepository usersRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Users> optional = usersRepository.findById(username);
		   if (optional.isEmpty()) {
		        throw new UsernameNotFoundException(
		                "User not found with username: " + username
		        );
		    }
		else {
		Users user=optional.get();
		return new UserPrinciple(user);
		}
	}

}
