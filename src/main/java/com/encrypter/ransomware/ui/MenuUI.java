package com.encrypter.ransomware.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuUI {
    private JFrame frame;
    private FileEncryptorUI fileEncryptionUI;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MenuUI window = new MenuUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public MenuUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu menuEncryptor = new JMenu("Encriptador");
        menuBar.add(menuEncryptor);

        // Botón para gestionar usuarios
        JMenuItem menuItemUsers = new JMenuItem("Gestión de Usuarios");
        menuItemUsers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserUI userManagementUI = new UserUI();
                userManagementUI.show();
            }
        });
        menuEncryptor.add(menuItemUsers);

        // Botón para encriptar
        JMenuItem menuItemEncrypt = new JMenuItem("Encriptar");
        menuItemEncrypt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fileEncryptionUI == null) {
                    fileEncryptionUI = new FileEncryptorUI();
                }
                fileEncryptionUI.show();
            }
        });
        menuEncryptor.add(menuItemEncrypt);

        // Botón para desencriptar
        JMenuItem menuItemDecrypt = new JMenuItem("Desencriptar");
        menuItemDecrypt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                decryptData();
            }
        });
        menuEncryptor.add(menuItemDecrypt);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JLabel lblTitle = new JLabel("Encriptador", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(lblTitle);

        // Botón para gestionar usuarios
        JButton btnUserManagement = new JButton("Gestión de Usuarios");
        btnUserManagement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserUI userManagementUI = new UserUI();
                userManagementUI.show();
            }
        });
        panel.add(btnUserManagement);

        // Botón para encriptar
        JButton btnEncrypt = new JButton("Encriptar");
        btnEncrypt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fileEncryptionUI == null) {
                    fileEncryptionUI = new FileEncryptorUI();
                }
                fileEncryptionUI.show();
            }
        });
        panel.add(btnEncrypt);

        // Botón para desencriptar
        JButton btnDecrypt = new JButton("Desencriptar");
        btnDecrypt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                decryptData();
            }
        });
        panel.add(btnDecrypt);

        frame.getContentPane().add(panel, BorderLayout.CENTER);
    }

    private void decryptData() {
        JOptionPane.showMessageDialog(frame, "Implementar la funcionalidad de desencriptación aquí.");
    }
}
