package com.encrypter.ransomware.repository;

import com.encrypter.ransomware.model.EncryptedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EncryptedFileRepository extends JpaRepository <EncryptedFile, Integer> {
}
