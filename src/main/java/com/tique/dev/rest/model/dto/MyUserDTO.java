package com.tique.dev.rest.model.dto;

import com.tique.dev.rest.model.RoleUser;
import com.tique.dev.rest.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class MyUserDTO {
    private Long id;
    @NotBlank(message =  "campo requerido")
    private String firstName;

    private String lastName;

    @Email(message = "Por favor digite um email valido")
    @NotNull(message = "campo requerido")
    private String email;
    private String password;


    private RoleUser roleUser;


    public MyUserDTO(){}

    public MyUserDTO(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public MyUserDTO(User entity){
          id = entity.getId();
          firstName = entity.getFirstName();
          lastName = entity.getLastName();
          email = entity.getEmail();
          password = entity.getPassword();
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
        return RoleUser.ROLE_CLIENT;
    }

    public void setRoleE(RoleUser roleUser) {
        this.roleUser = roleUser;
    }



    @Override
    public String toString() {
        return "MyUserDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roleE=" + roleUser +
                '}';
    }
}

