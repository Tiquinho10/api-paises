package com.tique.dev.rest.config;


import com.tique.dev.rest.model.RoleUser;
import com.tique.dev.rest.model.User;
import com.tique.dev.rest.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class FirstUserConfig implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(FirstUserConfig.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public FirstUserConfig(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() != 0) {
            return;
        }

        logger.info("Nenhum usuário encontrado, cadastrando usuários padrão.");


        userRepository.save(
                new User("admin",
                        "admin",
                         "admin@email.com",
                        passwordEncoder.encode("123456"),
                          RoleUser.ROLE_ADMIN)

        );


    }
}

