package org.solteh.service;

import org.solteh.model.User;
import org.solteh.model.UserState;
import org.solteh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (UserState role: UserState.values()) {
	        grantedAuthorities.add(new SimpleGrantedAuthority(role.name()));
        }

        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getEncrytedPassword(), grantedAuthorities);
    }
}