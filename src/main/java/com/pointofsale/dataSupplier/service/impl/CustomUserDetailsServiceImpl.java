package com.pointofsale.dataSupplier.service.impl;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pointofsale.dataSupplier.entity.UserCredential;
import com.pointofsale.dataSupplier.repository.UserCredentialRepository;
import com.pointofsale.dataSupplier.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {

        private final UserCredentialRepository userCredentialRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                UserCredential userCredential = userCredentialRepository.findByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "User not found with username: " + username));

                List<SimpleGrantedAuthority> grantedAuthorities = userCredential.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority(role.getRole().name())).toList();

                return UserDetailsImpl.builder()
                                .userCredentialId(userCredential.getId())
                                .username(userCredential.getUsername())
                                .email(userCredential.getEmail())
                                .password(userCredential.getPassword())
                                .store(userCredential.getCashier().getStore())
                                .authorities(grantedAuthorities)
                                .build();
        }

}
