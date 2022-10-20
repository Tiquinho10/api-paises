package com.tique.dev.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServer extends ResourceServerConfigurerAdapter {

    @Autowired
    private JwtTokenStore tokenStore;


    private static final String[] PUBLIC = {"/oauth/token", "/myuser", "/swagger-ui.html"};


    private static final String[] CLIENT_ADMIN = {"/paises/**"};



    private static final String[] ADMIN = {"/user/**", "/myuser/admin/**"};



    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                .antMatchers(PUBLIC).permitAll()
                .antMatchers(HttpMethod.GET, CLIENT_ADMIN).permitAll()
                .antMatchers(CLIENT_ADMIN).hasAnyRole("ADMIN", "CLIENT")
                .antMatchers(ADMIN).hasRole("ADMIN")
                .anyRequest().authenticated();
    }


}
