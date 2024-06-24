package com.encrypter.ransomware.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class EncryptedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "encryptedfile_sequence")
    @SequenceGenerator(name = "encryptedfile_sequence", sequenceName = "encryptedfile_sequence", allocationSize = 1)
    private Integer idEncryptedFile;

    @ManyToOne
    @JoinColumn(name = "fileId")
    private File file;

    @ManyToOne
    @JoinColumn(name = "idAttack")
    private Encryptor encryptor;


}
