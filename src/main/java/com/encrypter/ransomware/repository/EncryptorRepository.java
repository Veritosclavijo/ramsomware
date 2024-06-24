package com.encrypter.ransomware.repository;

import com.encrypter.ransomware.model.Encryptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EncryptorRepository extends JpaRepository<Encryptor, Integer> {
}
