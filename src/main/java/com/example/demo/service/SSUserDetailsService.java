package com.example.demo.service;

import com.example.demo.beans.Role;
import com.example.demo.beans.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class SSUserDetailsService implements UserDetailsService {

  private UserRepository appUserRepository;

  public SSUserDetailsService(UserRepository userRepository){
    this.appUserRepository=userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String appUsername) throws UsernameNotFoundException {
    try {
      User user = appUserRepository.findByUsername(appUsername);
      if (user == null) {
        System.out.println("User not found with the provided username" + user
                .toString());
        return null;
      }

      System.out.println(" User from username " + user.toString());

      return new org.springframework.security.core.userdetails.User(
              user.getUsername(),
              user.getPassword(),
              getAuthorities(user));
    } catch (Exception e){
      throw new UsernameNotFoundException("User not found");
    }
  } // End loadUserByUsername

  private Set<GrantedAuthority> getAuthorities(User appUser) {
    Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
    for (Role role : appUser.getRoles()) {
      GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRole());
      authorities.add(grantedAuthority);
    }
    System.out.println("User authorities are" + authorities.toString());
    return authorities;
  }
}

