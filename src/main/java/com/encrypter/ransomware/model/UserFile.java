package com.encrypter.ransomware.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userfile_sequence")
    @SequenceGenerator(name = "userfile_sequence", sequenceName = "userfile_sequence", allocationSize = 1)
    private Integer idUserFile;

    @ManyToOne
    @JoinColumn(name="fileId")
    private File file;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    private String paymentDetails;

}
