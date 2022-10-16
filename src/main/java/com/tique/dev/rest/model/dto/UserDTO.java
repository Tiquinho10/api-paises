package com.tique.dev.rest.model.dto;

import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.tique.dev.rest.model.RoleUser;
import com.tique.dev.rest.model.User;



public class UserDTO {
    private Long id;
    @NotBlank(message =  "campo requerido")
    private String firstName;
    @NotBlank(message =  "campo requerido")
    private String lastName;

    @Email(message = "Por favor digite um email valido")
    private String email;
    private String password;

    private RoleUser roleUser;

    //private Set<RoleDTO> roles = new HashSet<>();

    public UserDTO(){

    }

    public UserDTO(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UserDTO(User entity){
          id = entity.getId();
          firstName = entity.getFirstName();
          lastName = entity.getLastName();
          email = entity.getEmail();
          password = entity.getPassword();
          roleUser = entity.getRole();


    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public RoleUser getRoleUser() {
        return roleUser;
    }

    public void setRoleUser(RoleUser roleUser) {
        this.roleUser = roleUser;
    }


}

