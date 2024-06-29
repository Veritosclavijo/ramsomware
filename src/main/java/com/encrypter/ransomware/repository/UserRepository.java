package com.encrypter.ransomware.repository;

import com.encrypter.ransomware.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // define que la interface sirve como repositorio
public interface UserRepository extends JpaRepository<User, Integer> {
} // interface que provee los métodos del crud para cada uno de los modelos
