package com.encrypter.ransomware.ui;

import com.encrypter.ransomware.model.UserFile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.encrypter.ransomware.model.User;
import com.encrypter.ransomware.model.EncryptedFile;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class FileEncryptorUI {
    private JFrame frame;
    private JTextField txtFilePath;
    private JTextField txtEncryptionKey;
    private JComboBox<User> cmbUsers;
    private JTable tblEncryptedFiles;
    private JTable tblUserFiles;
    private DefaultTableModel tblModelEncryptedFiles;
    private DefaultTableModel tblModelUserFiles;
    private final String ENCRYPT_URL = "http://localhost:8080/api/filesEncrypt/encrypt";
    private final String DECRYPT_URL = "http://localhost:8080/api/filesEncrypt/decrypt";
    private final String GET_USERS_URL = "http://localhost:8080/api/users";
    private final String GET_ENCRYPTED_FILES_URL = "http://localhost:8080/api/encryptedFile";
    private final String GET_USER_FILES_URL = "http://localhost:8080/api/userFile";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                FileEncryptorUI window = new FileEncryptorUI();
                window.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public FileEncryptorUI() {
        initialize();
        loadUsers();
    }

    public void show() {
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("Encriptador de Archivos");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitle.setBounds(120, 20, 200, 20);
        frame.getContentPane().add(lblTitle);

        JLabel lblFilePath = new JLabel("Seleccionar Archivo:");
        lblFilePath.setBounds(30, 70, 150, 14);
        frame.getContentPane().add(lblFilePath);

        txtFilePath = new JTextField();
        txtFilePath.setBounds(180, 70, 200, 20);
        frame.getContentPane().add(txtFilePath);
        txtFilePath.setColumns(10);

        JButton btnBrowse = new JButton("Seleccionar");
        btnBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    txtFilePath.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        btnBrowse.setBounds(390, 70, 100, 20);
        frame.getContentPane().add(btnBrowse);

        JLabel lblEncryptionKey = new JLabel("Clave de Encriptación:");
        lblEncryptionKey.setBounds(30, 110, 150, 14);
        frame.getContentPane().add(lblEncryptionKey);

        txtEncryptionKey = new JTextField();
        txtEncryptionKey.setBounds(180, 110, 200, 20);
        frame.getContentPane().add(txtEncryptionKey);
        txtEncryptionKey.setColumns(10);

        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setBounds(30, 150, 250, 14);
        frame.getContentPane().add(lblUser);

        cmbUsers = new JComboBox<>();
        cmbUsers.setBounds(180, 150, 300, 20);
        frame.getContentPane().add(cmbUsers);

        JButton btnEncrypt = new JButton("Encriptar");
        btnEncrypt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    File file = new File(txtFilePath.getText());
                    FileSystemResource fileResource = new FileSystemResource(file);

                    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                    body.add("file", fileResource);

                    User selectedUser = (User) cmbUsers.getSelectedItem();
                    body.add("idUser", selectedUser.getUserId().toString());

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> response = restTemplate.postForEntity(ENCRYPT_URL, requestEntity, String.class);

                    txtEncryptionKey.setText(response.getBody());

                    JOptionPane.showMessageDialog(frame, response.getBody());

                    txtFilePath.setText("");
                    cmbUsers.setSelectedIndex(0);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error encriptando archivo: " + ex.getMessage());
                }
            }
        });
        btnEncrypt.setBounds(180, 190, 100, 20);
        frame.getContentPane().add(btnEncrypt);

        // Botón para mostrar archivos encriptados
        JButton btnShowEncryptedFiles = new JButton("Mostrar Archivos Encriptados");
        btnShowEncryptedFiles.setBounds(180, 220, 250, 20);
        btnShowEncryptedFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEncryptedFiles();
            }
        });
        frame.getContentPane().add(btnShowEncryptedFiles);

        // Tabla para mostrar archivos encriptados
        tblModelEncryptedFiles = new DefaultTableModel(new Object[]{"Id de Archivo", "Nombre del archivo","Clave de Encriptación","Fecha"}, 0);
        tblEncryptedFiles = new JTable(tblModelEncryptedFiles);
        JScrollPane scrollPane = new JScrollPane(tblEncryptedFiles);
        scrollPane.setBounds(30, 260, 700, 200);
        frame.getContentPane().add(scrollPane);

        // Botón y tabla para mostrar archivos de usuario
        JButton btnShowUserFiles = new JButton("Mostrar Archivos de Usuario");
        btnShowUserFiles.setBounds(180, 420, 250, 20);
        btnShowUserFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUserFiles();
            }
        });
        frame.getContentPane().add(btnShowUserFiles);

        tblModelUserFiles = new DefaultTableModel(new Object[]{"ID de Archivo", "Nombre del Archivo", "ID de Usuario"}, 0);
        tblUserFiles = new JTable(tblModelUserFiles);
        JScrollPane scrollPaneUser = new JScrollPane(tblUserFiles);
        scrollPaneUser.setBounds(30, 450, 700, 100);
        frame.getContentPane().add(scrollPaneUser);
    }


    private void loadUsers() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<List<User>> response = restTemplate.exchange(
                    GET_USERS_URL,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<User>>() {}
            );

            List<User> userList = response.getBody();
            if (userList != null && !userList.isEmpty()) {
                for (User user : userList) {
                    cmbUsers.addItem(user);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error al cargar usuarios: " + ex.getMessage());
        }
    }

    private void showEncryptedFiles() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<List<EncryptedFile>> response = restTemplate.exchange(
                    GET_ENCRYPTED_FILES_URL,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<EncryptedFile>>() {}
            );

            List<EncryptedFile> encryptedFiles = response.getBody();
            tblModelEncryptedFiles.setRowCount(0); // Limpiar tabla antes de agregar nuevos datos
            if (encryptedFiles != null && !encryptedFiles.isEmpty()) {
                for (EncryptedFile encryptedFile : encryptedFiles) {
                    tblModelEncryptedFiles.addRow(new Object[]{
                            encryptedFile.getFile().getFileId(),
                            encryptedFile.getFile().getFileName(),
                            encryptedFile.getEncryptionKey(),
                            encryptedFile.getFile().getEncryptionDate()

                    });
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No hay archivos encriptados.", "Archivos Encriptados", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error al cargar archivos encriptados: " + ex.getMessage());
        }
    }

    private void showUserFiles() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<List<UserFile>> response = restTemplate.exchange(
                    GET_USER_FILES_URL,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<UserFile>>() {}
            );

            List<UserFile> userFiles = response.getBody();
            tblModelUserFiles.setRowCount(0); // Limpiar tabla antes de agregar nuevos datos
            if (userFiles != null && !userFiles.isEmpty()) {
                for (UserFile userFile : userFiles) {
                    tblModelUserFiles.addRow(new Object[]{
                            userFile.getIdUserFile(),
                            userFile.getFile().getFileName(),  // Nombre del archivo
                            userFile.getUser().getUserId()     // ID del usuario
                    });
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No hay archivos de usuario.", "Archivos de Usuario", JOptionPane.INFORMATION_MESSAGE);
            }

            // Actualizar la tabla visualmente
            tblUserFiles.revalidate();
            tblUserFiles.repaint();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error al cargar archivos de usuario: " + ex.getMessage());
        }
    }


}
