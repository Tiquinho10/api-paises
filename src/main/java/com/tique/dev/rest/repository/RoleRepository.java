package com.tique.dev.rest.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.tique.dev.rest.model.Role;
import com.tique.dev.rest.model.User;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
