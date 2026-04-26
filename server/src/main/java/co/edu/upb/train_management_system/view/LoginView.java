package co.edu.upb.train_management_system.view;

import co.edu.upb.train_management_system.model.user.AbstractUserWithPower;
import co.edu.upb.train_management_system.model.user.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import co.edu.upb.train_management_system.view.RegisterView;

public class LoginView {
    private JFrame frame;
    private JTextField fieldId;
    private JPasswordField fieldPassword;
    private JLabel labelError;

    public LoginView() {
        frame = new JFrame("Train Management System — Login");
        frame.setSize(900, 550);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Panel izquierdo — decorativo
        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(new Color(30, 58, 95));
        left.setPreferredSize(new Dimension(380, 550));

        JLabel icon = new JLabel("🚆", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        icon.setForeground(Color.WHITE);

        JLabel appTitle = new JLabel("Train Management", SwingConstants.CENTER);
        appTitle.setFont(new Font("Arial", Font.BOLD, 22));
        appTitle.setForeground(Color.WHITE);

        JLabel appSubtitle = new JLabel("Sistema de administración", SwingConstants.CENTER);
        appSubtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        appSubtitle.setForeground(new Color(180, 200, 220));

        JPanel leftContent = new JPanel();
        leftContent.setOpaque(false);
        leftContent.setLayout(new BoxLayout(leftContent, BoxLayout.Y_AXIS));
        leftContent.setBorder(new EmptyBorder(120, 40, 40, 40));
        leftContent.add(icon);
        leftContent.add(Box.createVerticalStrut(16));
        leftContent.add(appTitle);
        leftContent.add(Box.createVerticalStrut(8));
        leftContent.add(appSubtitle);

        left.add(leftContent, BorderLayout.CENTER);

        // Panel derecho — formulario
        JPanel right = new JPanel();
        right.setBackground(new Color(245, 247, 250));
        right.setLayout(new GridBagLayout());
        right.setBorder(new EmptyBorder(40, 50, 40, 50));

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setMaximumSize(new Dimension(320, 400));

        JLabel loginTitle = new JLabel("Iniciar Sesión");
        loginTitle.setFont(new Font("Arial", Font.BOLD, 24));
        loginTitle.setForeground(new Color(30, 58, 95));
        loginTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel loginSubtitle = new JLabel("Ingresa tus credenciales de administrador");
        loginSubtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        loginSubtitle.setForeground(new Color(120, 130, 145));
        loginSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Campo ID
        JLabel lblId = new JLabel("Identificación");
        lblId.setFont(new Font("Arial", Font.BOLD, 12));
        lblId.setForeground(new Color(60, 70, 85));
        lblId.setAlignmentX(Component.LEFT_ALIGNMENT);

        fieldId = new JTextField();
        fieldId.setFont(new Font("Arial", Font.PLAIN, 14));
        fieldId.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        fieldId.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldId.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 220)),
                new EmptyBorder(6, 10, 6, 10)
        ));

        // Campo password
        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(new Font("Arial", Font.BOLD, 12));
        lblPass.setForeground(new Color(60, 70, 85));
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        fieldPassword = new JPasswordField();
        fieldPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        fieldPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        fieldPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 220)),
                new EmptyBorder(6, 10, 6, 10)
        ));

        // Botón login
        JButton btnLogin = new JButton("Ingresar");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(30, 58, 95));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Separador
        JPanel divider = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        divider.setOpaque(false);
        divider.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel orLabel = new JLabel("¿No tienes cuenta?");
        orLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        orLabel.setForeground(new Color(120, 130, 145));
        divider.add(orLabel);

        // Botón register
        JButton btnRegister = new JButton("Registrarse");
        btnRegister.setFont(new Font("Arial", Font.BOLD, 13));
        btnRegister.setForeground(new Color(30, 58, 95));
        btnRegister.setBackground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 58, 95)),
                new EmptyBorder(8, 0, 8, 0)
        ));
        btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRegister.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnRegister.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Label error
        labelError = new JLabel(" ", SwingConstants.LEFT);
        labelError.setFont(new Font("Arial", Font.PLAIN, 12));
        labelError.setForeground(new Color(200, 50, 50));
        labelError.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Armar formulario
        form.add(loginTitle);
        form.add(Box.createVerticalStrut(4));
        form.add(loginSubtitle);
        form.add(Box.createVerticalStrut(28));
        form.add(lblId);
        form.add(Box.createVerticalStrut(6));
        form.add(fieldId);
        form.add(Box.createVerticalStrut(16));
        form.add(lblPass);
        form.add(Box.createVerticalStrut(6));
        form.add(fieldPassword);
        form.add(Box.createVerticalStrut(8));
        form.add(labelError);
        form.add(Box.createVerticalStrut(8));
        form.add(btnLogin);
        form.add(Box.createVerticalStrut(16));
        form.add(divider);
        form.add(Box.createVerticalStrut(8));
        form.add(btnRegister);

        right.add(form);

        // Acciones
        btnLogin.addActionListener(e -> handleLogin());
        fieldPassword.addActionListener(e -> handleLogin()); // Enter en password
        btnRegister.addActionListener(e -> {
            frame.dispose();
            new RegisterView();
        });

        frame.add(left, BorderLayout.WEST);
        frame.add(right, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void handleLogin() {
        String id = fieldId.getText().trim();
        String password = new String(fieldPassword.getPassword());

        if (id.isEmpty() || password.isEmpty()) {
            labelError.setText("Completa todos los campos.");
            return;
        }

        try {
            AbstractUserWithPower user = UserService.getInstance().login(id, password);
            if (user != null) {
                frame.dispose();
                new AdminPanelView(user);
            } else {
                labelError.setText("Credenciales incorrectas.");
            }
        } catch (Exception ex) {
            labelError.setText("Error: " + ex.getMessage());
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    }
}