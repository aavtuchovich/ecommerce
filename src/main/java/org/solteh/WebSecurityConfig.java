package org.solteh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private final UserDetailsService userDetailsService;

	@Autowired
	public WebSecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager customAuthenticationManager() throws Exception {
		return authenticationManager();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// Setting Service to find User in the database.
		// And Setting PassswordEncoder
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/webjars/**", "/static/**").permitAll();

		http.csrf().disable();
		// Requires login with role ROLE_EMPLOYEE or ROLE_MANAGER.
		// If not, it will redirect to /admin/login.
		http.authorizeRequests().antMatchers("/user/orders", "/user/order", "/profile")//
				.access("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER')");

		// Pages only for MANAGER
		http.authorizeRequests().antMatchers("/admin/product").access("hasRole('ROLE_MANAGER')");

		// When user login, role XX.
		// But access to the page requires the YY role,
		// An AccessDeniedException will be thrown.
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

		// Configuration for Login Form.
		http.authorizeRequests().and().formLogin()//
				.loginProcessingUrl("/j_spring_security_check") // Submit URL
				.loginPage("/login")//
				.defaultSuccessUrl("/")//
				.failureUrl("/login?error=true")//
				.usernameParameter("userName")//
				.passwordParameter("password")

				// Configuration for the Logout page.
				// (After logout, go to home page)
				.and().logout().logoutUrl("/logout").logoutSuccessUrl("/");

	}
}
