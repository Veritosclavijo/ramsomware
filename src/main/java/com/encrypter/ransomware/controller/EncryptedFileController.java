package com.encrypter.ransomware.controller;

import com.encrypter.ransomware.model.EncryptedFile;
import com.encrypter.ransomware.model.File;
import com.encrypter.ransomware.repository.EncryptedFileRepository;
import com.encrypter.ransomware.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/encryptedFile")
public class EncryptedFileController {

    @Autowired
    EncryptedFileRepository encryptedFileRepository;

    @GetMapping
    public List<EncryptedFile> getAllencryptedFiles() {
        return encryptedFileRepository.findAll();
    }
}
