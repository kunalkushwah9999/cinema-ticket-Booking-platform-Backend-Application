
package com.project.BookMyShowApp.controller;

import com.project.BookMyShowApp.dto.JwtResponse;
import com.project.BookMyShowApp.dto.LoginUserDto;
import com.project.BookMyShowApp.dto.RegisterUserDto;
import com.project.BookMyShowApp.dto.UserDto;
import com.project.BookMyShowApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisterUserDto registerUserDto){
        return new ResponseEntity<>(userService.registerUser(registerUserDto), HttpStatus.CREATED);
    }

    @PostMapping("/admin/create-theater-admin")
    public  ResponseEntity<UserDto> createTheaterAdmin(@RequestBody RegisterUserDto registerUserDto){
        return new ResponseEntity<>(userService.createTheaterAdmin(registerUserDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginUserDto dto) {
        JwtResponse jwt = userService.loginUser(dto);
        return ResponseEntity.ok(jwt);
    }

//    @PostMapping("/login")
//    public void loginUser(@RequestBody LoginUserDto loginUserDto){
//        userService.loginUser(loginUserDto);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserByUserId(@PathVariable Long id){
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id){
        System.out.println("in user controller delete method");
        userService.deleteUser(id);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

}
