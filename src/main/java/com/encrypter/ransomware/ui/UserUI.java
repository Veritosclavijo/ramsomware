package com.encrypter.ransomware.ui;

import com.encrypter.ransomware.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class UserUI {
    private JFrame frame;
    private JTextField txtUserName;
    private JTextField txtUserType;
    private JTextField txtIpUserVictim;
    private JTextField txtOrganization;
    private JTextField txtUserId;
    private JTable tableUsers;
    private DefaultTableModel tableModel;

    private static final String BASE_URL = "http://localhost:8080/api/users";
    private ObjectMapper objectMapper = new ObjectMapper();


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UserUI window = new UserUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public UserUI() {
        initialize();
    }

    public void show() {
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(50, 50, 400, 200); // Ajusta el tamaño inicial
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelInputs = new JPanel();
        panelInputs.setLayout(new GridLayout(6, 2));

        // Labels and text fields
        panelInputs.add(new JLabel("User Name:"));
        txtUserName = new JTextField();
        panelInputs.add(txtUserName);

        panelInputs.add(new JLabel("User Type:"));
        txtUserType = new JTextField();
        panelInputs.add(txtUserType);

        panelInputs.add(new JLabel("IP User Victim:"));
        txtIpUserVictim = new JTextField();
        panelInputs.add(txtIpUserVictim);

        panelInputs.add(new JLabel("Organization:"));
        txtOrganization = new JTextField();
        panelInputs.add(txtOrganization);

        panelInputs.add(new JLabel("User ID (for update/delete):"));
        txtUserId = new JTextField();
        panelInputs.add(txtUserId);

        frame.getContentPane().add(panelInputs, BorderLayout.NORTH);

        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Alineación y espaciado entre botones

        // Botones con tamaño fijo y centrados
        JButton btnCreate = new JButton("Crear");
        btnCreate.setPreferredSize(new Dimension(100, 30)); // Tamaño fijo
        btnCreate.addActionListener(e -> createUser());
        panelButtons.add(btnCreate);

        JButton btnUpdate = new JButton("Actualizar");
        btnUpdate.setPreferredSize(new Dimension(100, 30)); // Tamaño fijo
        btnUpdate.addActionListener(e -> updateUserFromTable());
        panelButtons.add(btnUpdate);

        JButton btnDelete = new JButton("Eliminar");
        btnDelete.setPreferredSize(new Dimension(100, 30)); // Tamaño fijo
        btnDelete.addActionListener(e -> deleteUser());
        panelButtons.add(btnDelete);

        JButton btnGetAll = new JButton("Listar");
        btnGetAll.setPreferredSize(new Dimension(100, 30)); // Tamaño fijo
        btnGetAll.addActionListener(e -> getAllUsers());
        panelButtons.add(btnGetAll);

        frame.getContentPane().add(panelButtons, BorderLayout.CENTER);

        // Tabla para mostrar usuarios
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // Suponiendo que la columna 0 es el ID del usuario y no es editable
            }
        };
        tableModel.setColumnIdentifiers(new String[]{"User ID", "User Name", "User Type", "IP User Victim", "Organization"});
        tableUsers = new JTable(tableModel);
        tableUsers.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE); // Termina la edición al perder el foco
        JScrollPane scrollPane = new JScrollPane(tableUsers);
        frame.getContentPane().add(scrollPane, BorderLayout.SOUTH);

        tableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                if (column >= 0) { // Ignora los eventos de columna -1
                    updateUserFromTable();
                }
            }
        });

        // Ajuste para que la tabla se expanda y se muestre completamente
        tableUsers.setFillsViewportHeight(true); // La tabla ocupa toda la altura del JScrollPane
    }

    private void createUser() {
        try {
            // Verificación de campos vacíos
            if (txtUserName.getText().isEmpty() || txtUserType.getText().isEmpty() ||
                    txtIpUserVictim.getText().isEmpty() || txtOrganization.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Debe diligenciar todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            URL url = new URL(BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            User user = new User();
            user.setUserName(txtUserName.getText());
            user.setUserType(txtUserType.getText());
            user.setIpUserVictim(txtIpUserVictim.getText());
            user.setOrganization(txtOrganization.getText());

            String jsonInputString = objectMapper.writeValueAsString(user);

            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
                writer.write(jsonInputString);
                writer.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                txtUserName.setText("");
                txtUserType.setText("");
                txtIpUserVictim.setText("");
                txtOrganization.setText("");
                getAllUsers(); // Actualizar la tabla después de crear usuario
            } else {
                JOptionPane.showMessageDialog(frame, "Error al crear usuario", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void updateUserFromTable() {
        int selectedRow = tableUsers.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Por favor, selecciona una fila para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String userIdStr = tableModel.getValueAt(selectedRow, 0).toString(); // Obtener el User ID de la fila seleccionada como String
            Integer userId = Integer.parseInt(userIdStr); // Convertir String a Integer

            String userName = tableModel.getValueAt(selectedRow, 1).toString();
            String userType = tableModel.getValueAt(selectedRow, 2).toString();
            String ipUserVictim = tableModel.getValueAt(selectedRow, 3).toString();
            String organization = tableModel.getValueAt(selectedRow, 4).toString();

            txtUserId.setText(userIdStr); // Mostrar el User ID en el campo de texto correspondiente
            txtUserName.setText(userName);
            txtUserType.setText(userType);
            txtIpUserVictim.setText(ipUserVictim);
            txtOrganization.setText(organization);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "El ID de usuario debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void deleteUser() {
        try {
            String userId = txtUserId.getText();
            if (userId.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "El id del usuario es requerido para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(frame, "¿Estás seguro de que deseas eliminar al usuario con ID: " + userId + "?", "Confirmación de eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return; // Salir si el usuario cancela la eliminación
            }

            RestTemplate restTemplate = new RestTemplate();
            String deleteUrl = BASE_URL + "/" + userId;

            ResponseEntity<Void> response = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, null, Void.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                txtUserId.setText("");
                getAllUsers(); // Actualizar la tabla después de eliminar usuario
                JOptionPane.showMessageDialog(frame, "Usuario eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Error al eliminar el usuario", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void getAllUsers() {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            List<User> users = objectMapper.readValue(response.toString(), new TypeReference<List<User>>() {});

            DefaultTableModel model = (DefaultTableModel) tableUsers.getModel();
            model.setRowCount(0); // Limpiar la tabla antes de agregar nuevos datos

            for (User user : users) {
                model.addRow(new Object[]{
                        user.getUserId(),
                        user.getUserName(),
                        user.getUserType(),
                        user.getIpUserVictim(),
                        user.getOrganization()
                });
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
