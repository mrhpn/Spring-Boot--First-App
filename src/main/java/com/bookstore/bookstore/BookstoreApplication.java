package com.bookstore.bookstore;

import java.util.HashSet;
import java.util.Set;

import com.bookstore.bookstore.domain.User;
import com.bookstore.bookstore.domain.security.Role;
import com.bookstore.bookstore.domain.security.UserRole;
import com.bookstore.bookstore.service.UserService;
import com.bookstore.bookstore.utilities.SecurityUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookstoreApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user1 = new User();
		user1.setFirstname("Htet Phyo");
		user1.setLastname("Naing");
		user1.setPhone("0988888888");
		user1.setUsername("hpn");
		user1.setPassword(SecurityUtility.passwordEncoder().encode("11111"));
		user1.setEmail("hpn@gmail.com");

		Set<UserRole> userRoles = new HashSet<>();
		Role role1 = new Role();
		role1.setRoleId(1l);
		role1.setName("ROLE_USER");

		userRoles.add(new UserRole(user1, role1));

		userService.createUser(user1, userRoles);
	}

}
