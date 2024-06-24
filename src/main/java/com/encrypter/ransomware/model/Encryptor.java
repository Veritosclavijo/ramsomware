package com.encrypter.ransomware.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Encryptor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "encryptor_sequence")
    @SequenceGenerator(name = "encryptor_sequence", sequenceName = "encryptor_sequence", allocationSize = 1)
    private Integer idAttack;

    private String algorithmType;
    private String encryptionKey;

}
