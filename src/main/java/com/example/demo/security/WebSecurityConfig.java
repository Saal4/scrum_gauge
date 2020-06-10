package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public UserRepositoryAuthProvider userRepoAuthProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

    	// Public pages
        http.authorizeRequests().antMatchers("/logOut").permitAll();
        http.authorizeRequests().antMatchers("/logIn").permitAll();
        http.authorizeRequests().antMatchers("/logIn/newAccount").permitAll();
        http.authorizeRequests().antMatchers("/logIn/newAccount/try").permitAll();



        // Private pages (all other pages)

        http.authorizeRequests().antMatchers("/question/**").hasAnyRole("SCRUM_GAUGE");
        http.authorizeRequests().antMatchers("/questions/**").hasAnyRole("SCRUM_GAUGE");
        http.authorizeRequests().antMatchers("/MainPage").hasAnyRole("ADMIN","SCRUM_GAUGE","USER");
        http.authorizeRequests().antMatchers("/questionnaire/edit/**").hasAnyRole("ADMIN","SCRUM_GAUGE");
        http.authorizeRequests().antMatchers("/questionnaire").hasAnyRole("ADMIN","SCRUM_GAUGE");
        http.authorizeRequests().antMatchers("/questionnaire/Reply/**").hasAnyRole("ADMIN","SCRUM_GAUGE","USER");
        http.authorizeRequests().antMatchers("/questionnaire/pending").hasAnyRole("ADMIN","SCRUM_GAUGE","USER");


        // Login form
        http.formLogin().loginPage("/logIn");
        http.formLogin().usernameParameter("username");
        http.formLogin().passwordParameter("password");
        http.formLogin().defaultSuccessUrl("/MainPage");
        http.formLogin().failureUrl("/loginError");

        //LogOut form
        http.logout().logoutUrl("/logOut");
        http.logout().logoutSuccessUrl("/logIn");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        // Database authentication provider
        auth.authenticationProvider(userRepoAuthProvider);
    }
}
