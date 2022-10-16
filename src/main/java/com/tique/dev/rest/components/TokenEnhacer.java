package com.tique.dev.rest.components;

import com.tique.dev.rest.model.User;
import com.tique.dev.rest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class TokenEnhacer implements TokenEnhancer {

    private final UserRepository userRepository;

    public TokenEnhacer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken acess, OAuth2Authentication auth) {

        Optional<User> obj = userRepository.findByEmail(auth.getName());

        User user = obj.orElseThrow(
                () -> new IllegalStateException("User not found")
        );

        Map<String, Object> map = new HashMap<>();
        map.put("userFirstName", user.getFirstName());
        map.put("userId", user.getId());
        map.put("userRole", user.getRole());

        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) acess;
        token.setAdditionalInformation(map);
        return acess;
    }
}
