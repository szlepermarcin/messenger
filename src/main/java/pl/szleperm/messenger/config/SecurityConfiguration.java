package pl.szleperm.messenger.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired
	protected UserDetailsService userDetailsService;
	
	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.requestMatchers()
				.antMatchers("/*", "/resource/**", "/users/**", "/register/**", "/messages/**").and()
			.authorizeRequests()
				.antMatchers("/", "/index.html", "/register/**", "/users/**", "/messages/**").permitAll()
				/*.antMatchers("/users/**").hasRole("ADMIN")*/ //temporary
				.anyRequest().authenticated().and()
			.formLogin()
				.loginPage("/login").permitAll().and()
			.csrf()
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}		
}