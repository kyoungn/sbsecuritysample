package com.cos.security11.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private LoginSuccessHandler successHandler;
	
	@Autowired
	private LoginFailHandler loginFailHandler;
	
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	

	@Override
	public void configure(WebSecurity webSecurity) throws Exception{
		webSecurity.ignoring().antMatchers("/css/**", "/images/**", "/js/**", "/fonts/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated()
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.antMatchers("/manager/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/login")
			.loginProcessingUrl("/loginProc")
//			.successHandler(successHandler)
			.defaultSuccessUrl("/")
//			.failureForwardUrl("/error")
			.failureHandler(loginFailHandler)
//			.and()
//			.logout()
//			.logoutUrl("/logout")
//			.logoutSuccessUrl("/")
//			.and()
//			.exceptionHandling()
//			.accessDeniedPage("/error")
//			.and()
//			.sessionManagement().invalidSessionUrl("/expired")
			;
		
		http.logout()
	        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	        .logoutSuccessUrl("/")
	        .invalidateHttpSession(true);

		http.exceptionHandling()
        	.accessDeniedPage("/denied");
			
	}
}
