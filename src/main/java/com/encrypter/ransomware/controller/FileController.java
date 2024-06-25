package com.encrypter.ransomware.controller;

import com.encrypter.ransomware.model.File;
import com.encrypter.ransomware.model.User;
import com.encrypter.ransomware.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/files")
public class FileController {

    @Autowired
    FileRepository fileRepository;

    @GetMapping
    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }

}
