package com.mindhub.homebankingJDK17.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests( ant ->
                ant.requestMatchers("/index.html", "/Web/login.html", "/Web/register.html", "/resources/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/login", "/api/clients").permitAll()
                        .requestMatchers("/rest/**", "/api/clients", "/web/admin/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/clients/currents", "/api/accounts/{id}",
                                "/api/transactions", "/api/loans").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/clients/current/**", "/api/loans/pay").authenticated()
                        .requestMatchers("/web/accounts.html", "/web/account.html", "/web/cards.html",
                                "/web/create-card.html", "/web/transfers.html", "/web/loan-application.html",
                                "/web/loan-payment.html").authenticated()
                        .requestMatchers("/**").hasAuthority("ADMIN")
                        .anyRequest().denyAll())
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(options -> options.disable()))
                .exceptionHandling(exc -> exc.authenticationEntryPoint(((request, response, authException) -> clearAuthenticationAttributes(request))));

        http.formLogin( formLogin -> formLogin.loginPage("/web/login.html")
                .loginProcessingUrl("/api/login")
                .usernameParameter("email")
                .passwordParameter("pwd")
                .successHandler((request, response, authentication) -> clearAuthenticationAttributes(request))
                .failureHandler((request, response, exception) -> response.sendError(403)))
                .logout(logout -> logout.logoutUrl("/api/logout")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                        .deleteCookies("JSESSIONID"))
                .rememberMe(Customizer.withDefaults());

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        // verifica que no haya una sesion activa
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Si hay una sesión, esta línea elimina el atributo denominado
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}