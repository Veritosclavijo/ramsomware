package com.encrypter.ransomware.controller;

import com.encrypter.ransomware.service.FileEncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/files")
public class FileEncryptController {

    @Autowired
    private FileEncryptionService fileEncryptionService;

    @PostMapping("/encrypt")
    public String encryptFile(@RequestParam("file") MultipartFile file) {
        try {
            return fileEncryptionService.encryptFile(file, fileEncryptionService.generateKey());
        } catch (IOException | NoSuchAlgorithmException e){
            e.printStackTrace();
            return "Error encriptando"+e;
        }
    }

    @PostMapping("/decrypt")
    public String decryptFile(@RequestParam("file") MultipartFile file, @RequestParam("key") String key) {
        try {
            fileEncryptionService.decryptFile(file, key);
            return "File decrypted successfully!";
        } catch (IOException e){
            e.printStackTrace();
            return "Error desencriptando"+e;
        }
    }


}
