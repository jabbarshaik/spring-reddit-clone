package com.sample.springredditclone.service;

import com.sample.springredditclone.model.User;
import com.sample.springredditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private  final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        Optional<User> byUsername = userRepository.findByUsername(userName);

        User user = byUsername.orElseThrow(() -> new UsernameNotFoundException("User Name not exists in DB"));



        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),
                                    user.isEnabled(),true,true,true,getAuthorities());
    }

    private Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }
}
