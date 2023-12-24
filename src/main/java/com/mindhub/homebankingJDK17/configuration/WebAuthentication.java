package com.mindhub.homebankingJDK17.configuration;

import com.mindhub.homebankingJDK17.models.Client;
import com.mindhub.homebankingJDK17.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userNameEmail -> {
            Client client = clientRepository.findByEmail(userNameEmail);
            if (client != null) {
                return new User(client.getEmail(), client.getPassword(),
                        (client.isAdmin()) ? AuthorityUtils.createAuthorityList("ADMIN")
                                : AuthorityUtils.createAuthorityList("CLIENT"));
            }
            else {
                throw new UsernameNotFoundException("Unknown user: " + userNameEmail);
            }
        });
    }
}
