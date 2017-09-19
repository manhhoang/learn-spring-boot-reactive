package com.knapsack.service;

import com.knapsack.model.KnapSackUser;
import com.knapsack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class KnapSackUserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public KnapSackUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        KnapSackUser user = this.userRepository.findByName(name);
        return new User(user.getName(), user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRoles()));
    }

}