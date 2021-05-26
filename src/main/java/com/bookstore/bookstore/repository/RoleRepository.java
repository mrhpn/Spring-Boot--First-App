package com.bookstore.bookstore.repository;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.bookstore.domain.security.Role;

public interface RoleRepository extends CrudRepository<Role,Long>{

	Role findByName(String name);
	

}
