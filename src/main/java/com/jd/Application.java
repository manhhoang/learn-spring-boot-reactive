package com.jd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jd.models.User;
import com.jd.models.UserDao;

@SpringBootApplication
@RestController
public class Application {

	@RequestMapping("/")
	public String home() {
		return "I am a super hero :)";
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner init(final UserDao userDao) {

		return new CommandLineRunner() {

			@Override
			public void run(String... arg0) throws Exception {
				userDao.save(new User("test", "password"));
			}

		};

	}

}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	UserDao userDao;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService());
	}

	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				User user = userDao.findByUsername(username);
				if (user != null) {
					return new org.springframework.security.core.userdetails.User(user.getUsername(),
							user.getPassword(), true, true, true, true, AuthorityUtils.createAuthorityList("USER"));
				} else {
					throw new UsernameNotFoundException("could not find the user '" + username + "'");
				}
			}

		};
	}
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().fullyAuthenticated().and().httpBasic().and().csrf().disable();
	}

}