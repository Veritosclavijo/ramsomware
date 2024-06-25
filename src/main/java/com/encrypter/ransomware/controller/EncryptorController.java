package com.encrypter.ransomware.controller;

import com.encrypter.ransomware.model.EncryptedFile;
import com.encrypter.ransomware.model.Encryptor;
import com.encrypter.ransomware.repository.EncryptedFileRepository;
import com.encrypter.ransomware.repository.EncryptorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/encryptor")
public class EncryptorController {

    @Autowired
    EncryptorRepository encryptorRepository;

    @GetMapping
    public List<Encryptor> getAllEncryptor() {
        return encryptorRepository.findAll();
    }

}
