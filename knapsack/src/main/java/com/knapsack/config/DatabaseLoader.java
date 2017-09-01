package com.knapsack.config;

import com.knapsack.model.KnapSackUser;
import com.knapsack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

	private final UserRepository userRepository;

	@Autowired
	public DatabaseLoader(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void run(String... strings) throws Exception {

		KnapSackUser user = this.userRepository.save(new KnapSackUser("user", "password", "ROLE_MANAGER"));

		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken("user", "doesn't matter",
				AuthorityUtils.createAuthorityList("ROLE_MANAGER")));

		SecurityContextHolder.clearContext();
	}
}