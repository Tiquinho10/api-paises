package com.tique.dev.rest.services;

import com.tique.dev.rest.model.User;
import com.tique.dev.rest.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    private static Logger logger = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    private UserRepository repository;

    @Transactional(readOnly = true)
    public User authenticated(){
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            Optional<User> user = repository.findByEmail(username);

            return user.get();
        } catch (Exception e) {
            throw new UnauthorizedUserException("Unauthorized acess");
        }
    }

    @Transactional(readOnly = true)
    public void validateSelfOrAdmin(Long userId){
        User user = authenticated();

       logger.info("is admin? " + user.hasRole("ROLE_ADMIN"));

        if(!user.getId().equals(userId) && !user.hasRole("ROLE_ADMIN"))
            throw new UnauthorizedUserException("acess denied");
    }

}
