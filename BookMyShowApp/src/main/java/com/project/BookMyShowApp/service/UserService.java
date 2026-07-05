package com.project.BookMyShowApp.service;

import com.project.BookMyShowApp.dto.JwtResponse;
import com.project.BookMyShowApp.dto.LoginUserDto;
import com.project.BookMyShowApp.dto.RegisterUserDto;
import com.project.BookMyShowApp.dto.UserDto;
import com.project.BookMyShowApp.exception.ResourceNotFoundException;
import com.project.BookMyShowApp.model.Role;
import com.project.BookMyShowApp.model.User;
import com.project.BookMyShowApp.repository.RoleRepository;
import com.project.BookMyShowApp.repository.UserRepository;
import com.project.BookMyShowApp.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserDto createUser(UserDto userDto){
        User user = mapToEntity(userDto);
        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    public UserDto getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found with id : " + id));
        return mapToDto(user);
    }

    public List<UserDto> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public UserDto registerUser(RegisterUserDto registerUserDto) {
        User user = new User();
        user.setName(registerUserDto.getName());
        user.setEmail(registerUserDto.getEmail());
        user.setPhoneNumber(registerUserDto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("ROLE_USER not found"));
        user.getRoles().add(userRole);
        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public JwtResponse loginUser(LoginUserDto dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        // fetch user to include roles/claims
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtTokenProvider.generateToken(user);
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        return new JwtResponse(token, "Bearer", roles);
    }
//    public void loginUser(LoginUserDto loginUserDto) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginUserDto.getEmail(),
//                        loginUserDto.getPassword()
//                )
//        );
//
//    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User doesn't exist"));
        System.out.println("In user Service delete method");
        userRepository.delete(user);
    }

    public User mapToEntity(UserDto userDto){
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.getRoles().add(roleRepository.findByName("ROLE_USER").get());
        return user;
    }

    public UserDto mapToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        return userDto;

    }

    public UserDto createTheaterAdmin(RegisterUserDto registerUserDto) {
        User user = new User();
        user.setName(registerUserDto.getName());
        user.setEmail(registerUserDto.getEmail());
        user.setPhoneNumber(registerUserDto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        Role userRole = roleRepository.findByName("ROLE_THEATER_ADMIN")
                .orElseThrow(() -> new ResourceNotFoundException("ROLE_USER not found"));
        user.getRoles().add(userRole);
        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }
}