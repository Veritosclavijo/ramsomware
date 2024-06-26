package com.encrypter.ransomware.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // crea en enlace o conexión con la entidad
@Data // genera los getters y setters
@AllArgsConstructor // crea el constructor con los atributos
@NoArgsConstructor // constructor vacío
public class UserFile {

    // llave primaria
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userfile_sequence")
    @SequenceGenerator(name = "userfile_sequence", sequenceName = "userfile_sequence", allocationSize = 1)
    private Integer idUserFile;

// relación de las entidades
    @ManyToOne // muchos a uno
    @JoinColumn(name="fileId") // llave foranea
    private File file; // instanciacion del file

    // relacion de las entidades
    @ManyToOne
    @JoinColumn(name="userId") // llave foranea
    private User user; // Instanciacion del user

    private String paymentDetails;

}
