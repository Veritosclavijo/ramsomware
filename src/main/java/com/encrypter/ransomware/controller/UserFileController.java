package com.encrypter.ransomware.controller;

import com.encrypter.ransomware.model.File;
import com.encrypter.ransomware.model.UserFile;
import com.encrypter.ransomware.repository.FileRepository;
import com.encrypter.ransomware.repository.UserFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/userFile")
public class UserFileController {
    @Autowired
    UserFileRepository userFileRepository;

    @GetMapping
    public List<UserFile> getAllUserFiles() {
        return userFileRepository.findAll();
    }
}
