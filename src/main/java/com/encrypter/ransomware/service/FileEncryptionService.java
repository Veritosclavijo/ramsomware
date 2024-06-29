package com.encrypter.ransomware.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.spec.ECField;
import java.util.Base64;

@Service // Para indicar que esta clase va a ser el servicio
public class FileEncryptionService {

    private static final String ALGORITHM = "AES";
    private static final String KEY_ALGORITHM = "AES";


// Creación de llave
    public SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(256);
        return keyGenerator.generateKey(); // genera y devuelve la clave
    }
    //Encriptación de archivo
    public String encryptFile(MultipartFile multipartFile, SecretKey key) throws IOException {
        byte[] fileBytes = multipartFile.getBytes();
        byte[] encryptedBytes = encrypt(fileBytes, key); // Encriptando bytes

        byte[] fileBytesCopy = multipartFile.getBytes();
        Path backupPath = Paths.get("C:\\Users\\HP\\Desktop\\PruebaEncrypt\\", "Copia_" + multipartFile.getOriginalFilename());
        Files.write(backupPath, fileBytesCopy); // copia del archivo

        String filePath = Paths.get("C:\\Users\\HP\\Desktop\\PruebaEncrypt\\", multipartFile.getOriginalFilename()).toString();
        try (FileOutputStream fos = new FileOutputStream(filePath)){
            fos.write(encryptedBytes); // construir el archivo con los bites encriptados
        }

        return Base64.getEncoder().encodeToString(key.getEncoded()); // metodo para obtener llave para mostrarla en string
    }

    public void decryptFile(MultipartFile multipartFile, String encodedKey) throws IOException {
        byte[] fileBytes = multipartFile.getBytes();
        SecretKey key = new SecretKeySpec(Base64.getDecoder().decode(encodedKey), ALGORITHM);
        byte[] decryptedBytes = decrypt(fileBytes, key);

        String filePath = Paths.get("C:\\Users\\HP\\Desktop\\PruebaEncrypt\\", multipartFile.getOriginalFilename()).toString();

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(decryptedBytes);
        }
    }
// metodo para encriptar
    private byte[] encrypt(byte[] data, SecretKey key){
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e){
            throw new RuntimeException("Error encriptando archivo",e);
        }
    }
// metodo para desencriptar
    private byte[] decrypt(byte[] data, SecretKey key){
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e){
            throw new RuntimeException("Error desencriptando archivo",e);
        }
    }


}
