package com.encrypter.ransomware.controller;

import com.encrypter.ransomware.model.User;
import com.encrypter.ransomware.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Es el controlador
@RequestMapping("api/users") // CREAR EL CONTEXTO DEL ENDPOINT (http://localhost:8080/api/users) para comunicarse el front con el back
public class UserController {

    @Autowired  // Inyeccion de dependencias
    private UserRepository userRepository; // instanciacion del userrepository

    @GetMapping // traer informacion de bases de datos
    public List<User> getAllUsers() { // SE CREA EL METODO PARA LLAMAR EL METODO FINDALL

        return userRepository.findAll();

    }

    @PostMapping // guardar informacion
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
