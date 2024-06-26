package com.encrypter.ransomware.controller;

import com.encrypter.ransomware.model.*;
import com.encrypter.ransomware.repository.*;
import com.encrypter.ransomware.service.FileEncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/filesEncrypt")
public class FileEncryptController {

    @Autowired
    private FileEncryptionService fileEncryptionService;

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private EncryptorRepository encryptorRepository;
    @Autowired
    private EncryptedFileRepository encryptedFileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFileRepository userFileRepository;

    @PostMapping("/encrypt")
    public String encryptFile(@RequestParam("file") MultipartFile multipartFile, @RequestParam("idUser") Integer idUser) {
        try {

            Optional<User> user = userRepository.findById(idUser);

            if (user.isPresent()) {

                String keyEncrypt = fileEncryptionService.encryptFile(multipartFile, fileEncryptionService.generateKey());

                File file = new File();
                file.setFileName(multipartFile.getOriginalFilename());
                file.setEncryptionDate(new Date());
                fileRepository.save(file);

                Encryptor encryptor = new Encryptor();
                encryptor.setAlgorithmType("AES");
                encryptorRepository.save(encryptor);

                EncryptedFile encryptedFile = new EncryptedFile();
                encryptedFile.setFile(file);
                encryptedFile.setEncryptor(encryptor);
                encryptedFile.setEncryptionKey(keyEncrypt);
                encryptedFileRepository.save(encryptedFile);

                UserFile userFile = new UserFile();
                userFile.setFile(file);
                userFile.setUser(user.get());

                userFileRepository.save(userFile);

                return keyEncrypt;
            }
            else {
                return "El usuario con el id: " + idUser + " no existe";
            }
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
