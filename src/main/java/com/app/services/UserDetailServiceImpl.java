package com.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.controllers.dto.AuthCreateUserRequest;
import com.app.controllers.dto.AuthLoginRequest;
import com.app.controllers.dto.AuthResponse;
import com.app.persistence.entities.RolesEntity;
import com.app.persistence.entities.UserEntity;
import com.app.persistence.repository.RoleRepository;
import com.app.persistence.repository.UserRepository;
import com.app.util.JwtUtils;

import jakarta.validation.Valid;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private JwtUtils jwtUtils;

        @Autowired
        private RoleRepository roleRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

                UserEntity userEntity = this.userRepository.findUserEntityByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "El usuario " + username + " no existe."));

                List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

                userEntity.getRoles()
                                .forEach(role -> authorityList
                                                .add((new SimpleGrantedAuthority(
                                                                "ROLE_".concat(role.getRoleEnum().name())))));

                userEntity.getRoles().stream()
                                .flatMap((role) -> role.getPermissionList().stream())
                                .forEach(permission -> authorityList
                                                .add(new SimpleGrantedAuthority(permission.getName())));

                return new User(userEntity.getUsername(),
                                userEntity.getPassword(),
                                userEntity.isEnabled(),
                                userEntity.isAccountNotExpired(),
                                userEntity.isCredentialNoExpired(),
                                userEntity.isAccountNoLocked(),
                                authorityList);

        }

        public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {
                String username = authLoginRequest.username();
                String password = authLoginRequest.password();

                Authentication authentication = this.authenticate(username, password);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                String accesToken = this.jwtUtils.createToken(authentication);

                return new AuthResponse(username, "User Loged Succesfuly", accesToken, true);
        }

        public Authentication authenticate(String username, String password) {
                UserDetails userDetails = this.loadUserByUsername(username);

                if (userDetails == null) {
                        throw new BadCredentialsException("Invalid username or password");
                }
                if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                        throw new BadCredentialsException("Invalid password");
                }

                return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(),
                                userDetails.getAuthorities());

        }

        public AuthResponse createUser(AuthCreateUserRequest authCreateUserRequest) {
                String username = authCreateUserRequest.username();
                String password = authCreateUserRequest.password();
                List<String> roleRequest = authCreateUserRequest.roleRequest().roleListName();
                Set<RolesEntity> roleEntitySet = this.roleRepository.findRoleEntitiesByRoleEnumIn(roleRequest).stream()
                                .collect(Collectors.toSet());
                if (roleEntitySet.isEmpty()) {
                        throw new IllegalArgumentException("The roles specified not exits");
                }

                UserEntity userEntity = UserEntity.builder()
                                .username(username)
                                .password(passwordEncoder.encode(password))
                                .roles(roleEntitySet)
                                .isEnabled(true)
                                .accountNoLocked(true)
                                .accountNotExpired(true)
                                .credentialNoExpired(true)
                                .build();

                UserEntity userCreated = this.userRepository.save(userEntity);

                List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
                userCreated.getRoles().forEach(role -> authorityList
                                .add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

                userCreated.getRoles()
                                .stream()
                                .flatMap(role -> role.getPermissionList().stream())
                                .forEach(permission -> authorityList
                                                .add(new SimpleGrantedAuthority(permission.getName())));

                Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getUsername(), userCreated.getPassword(), authorityList);

                String accessToken = jwtUtils.createToken(authentication);

                AuthResponse authResponse = new AuthResponse(userCreated.getUsername(), "User created successfuly", accessToken, true);
                return authResponse;
        }

}
