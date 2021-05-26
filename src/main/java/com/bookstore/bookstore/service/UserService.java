package com.bookstore.bookstore.service;

import java.util.Set;

import com.bookstore.bookstore.domain.User;
import com.bookstore.bookstore.domain.security.PasswordResetToken;
import com.bookstore.bookstore.domain.security.UserRole;

public interface UserService {
	
	PasswordResetToken getPasswordResetToken(final String token);
	
	void createPasswordResetTokenForUser(final User user,final String token);
	
	User findByEmail(String email);
	
	User findByUsername(String username);
	
	User createUser(User user,Set<UserRole> userRoles) throws Exception;
	
	User save(User user);

}
