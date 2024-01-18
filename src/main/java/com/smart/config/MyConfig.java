package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class MyConfig {
	
	//aurthzation
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	return	http.csrf().disable().authorizeHttpRequests()
		.requestMatchers("/user/**").hasRole("USER").requestMatchers("/admin/**").hasRole("ADMIN").and()
		.authorizeHttpRequests().requestMatchers("/**").permitAll()
		.and().formLogin()
		.loginPage("/signin")
		.loginProcessingUrl("/dologin")
		.defaultSuccessUrl("/user/index")
		.and().build();
	}
	
	
	
	@Bean
	public UserDetailsService getUserDetailsService() {
		return new UserDetailsServiceImpl();	
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
		@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
 }
	
//	//configure method..
//	@Bean
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//       auth.authenticationProvider(authenticationProvider());
//   }
	
////	@Bean
////	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////
////		return http.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN")
////		.requestMatchers("/user/**").authorizeHttpRequests()
////		.requestMatchers("/**").permitAll().and().formLogin().and().csrf().disable();
////	}
//	
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//	return	http.csrf().disable().authorizeHttpRequests()
//		.requestMatchers("/user/**").permitAll().and()
//		.authorizeHttpRequests().requestMatchers("/admin/**")
//		.authenticated().and().formLogin()
//		.and().build();
//	}
//	
	
	
}
