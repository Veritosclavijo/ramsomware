package com.encrypter.ransomware.controller;

import com.encrypter.ransomware.model.User;
import com.encrypter.ransomware.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        return userRepository.save(user);
    }

    @PutMapping
    public User updateUser(@PathVariable Integer id, @RequestBody User userDetails){
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setUserName(userDetails.getUserName());
        user.setUserType(userDetails.getUserType());
        user.setIpUserVictim(userDetails.getIpUserVictim());
        user.setOrganization(userDetails.getOrganization());

        return userRepository.save(user);

    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id){
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        userRepository.delete(user);
    }

}
