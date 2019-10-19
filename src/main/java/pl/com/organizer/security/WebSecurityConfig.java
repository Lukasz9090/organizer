package pl.com.organizer.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private String[] staticResources  =  {
            "/css/**",
            "/images/**",
            "/fonts/**",
    };

    private String [] publicAddresses = {
            "/",
            "/home",
            "/home/***",
            "/home/login/***"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(staticResources).permitAll()
                .antMatchers(publicAddresses).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/home/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/user")
                .failureUrl("/home/login?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/home")
                .permitAll();
    }
}
