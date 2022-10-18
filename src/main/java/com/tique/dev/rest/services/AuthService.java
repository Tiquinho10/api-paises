package com.tique.dev.rest.services;

import com.tique.dev.rest.model.Pais;
import com.tique.dev.rest.model.User;
import com.tique.dev.rest.repository.PaisRepository;
import com.tique.dev.rest.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;

    private final PaisRepository paisRepository;

    public AuthService(UserRepository userRepository, PaisRepository paisRepository) {
        this.userRepository = userRepository;
        this.paisRepository = paisRepository;
    }


    @Transactional(readOnly = true)
    public User authenticated(){
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            Optional<User> user = userRepository.findByEmail(username);

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

    @Transactional(readOnly = true)
    public void validateSelfOrAdminForEdit(Long paisId){
        User user = authenticated();
        Optional<Pais> obj = paisRepository.findById(paisId);

        Pais pais = obj.orElseThrow(
                () -> new IllegalStateException("O pais com o id: " + paisId + " nao existe")
        );


        logger.info("Created by user id " + pais.getUser().getId());

        if(!user.getId().equals(pais.getUser().getId()) && !user.hasRole("ROLE_ADMIN"))
            throw new UnauthorizedUserException("acess denied");
    }

}
