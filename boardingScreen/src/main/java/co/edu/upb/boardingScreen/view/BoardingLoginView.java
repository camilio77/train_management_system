package co.edu.upb.boardingScreen.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BoardingLoginView {

    private JFrame         frame;
    private JTextField     fieldId;
    private JPasswordField fieldPassword;
    private JLabel         labelError;
    private JButton        btnLogin;

    private static final Color DARK_BLUE = new Color(20, 60, 100);
    private static final Color BG        = new Color(245, 247, 250);

    public BoardingLoginView() {
        frame = new JFrame("Sistema de Abordaje — Login");
        frame.setSize(850, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Panel izquierdo decorativo
        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(DARK_BLUE);
        left.setPreferredSize(new Dimension(340, 500));

        JPanel leftContent = new JPanel();
        leftContent.setOpaque(false);
        leftContent.setLayout(new BoxLayout(leftContent, BoxLayout.Y_AXIS));
        leftContent.setBorder(new EmptyBorder(100, 40, 40, 40));

        JLabel icon = new JLabel("🚂", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        icon.setForeground(Color.WHITE);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appTitle = new JLabel("Sistema de Abordaje", SwingConstants.CENTER);
        appTitle.setFont(new Font("Arial", Font.BOLD, 20));
        appTitle.setForeground(Color.WHITE);
        appTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appSub = new JLabel("Control de pasajeros", SwingConstants.CENTER);
        appSub.setFont(new Font("Arial", Font.PLAIN, 13));
        appSub.setForeground(new Color(160, 190, 220));
        appSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftContent.add(icon);
        leftContent.add(Box.createVerticalStrut(16));
        leftContent.add(appTitle);
        leftContent.add(Box.createVerticalStrut(8));
        leftContent.add(appSub);
        left.add(leftContent, BorderLayout.CENTER);

        // Panel derecho con formulario
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(BG);
        right.setBorder(new EmptyBorder(40, 50, 40, 50));

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JLabel loginTitle = styledLabel("Iniciar Sesión", 22, Font.BOLD,
                new Color(20, 60, 100));
        loginTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel loginSub = styledLabel("Empleado o Administrador", 13, Font.PLAIN,
                new Color(120, 130, 145));
        loginSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblId = styledLabel("Identificación", 12, Font.BOLD,
                new Color(60, 70, 85));
        lblId.setAlignmentX(Component.LEFT_ALIGNMENT);

        fieldId = new JTextField();
        styleInput(fieldId);

        JLabel lblPass = styledLabel("Contraseña", 12, Font.BOLD,
                new Color(60, 70, 85));
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        fieldPassword = new JPasswordField();
        styleInput(fieldPassword);

        labelError = new JLabel(" ");
        labelError.setFont(new Font("Arial", Font.PLAIN, 12));
        labelError.setForeground(new Color(200, 50, 50));
        labelError.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnLogin = new JButton("Ingresar");
        btnLogin.setBackground(DARK_BLUE);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        form.add(loginTitle);
        form.add(Box.createVerticalStrut(4));
        form.add(loginSub);
        form.add(Box.createVerticalStrut(24));
        form.add(lblId);
        form.add(Box.createVerticalStrut(6));
        form.add(fieldId);
        form.add(Box.createVerticalStrut(14));
        form.add(lblPass);
        form.add(Box.createVerticalStrut(6));
        form.add(fieldPassword);
        form.add(Box.createVerticalStrut(8));
        form.add(labelError);
        form.add(Box.createVerticalStrut(8));
        form.add(btnLogin);

        right.add(form);

        frame.add(left,  BorderLayout.WEST);
        frame.add(right, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JLabel styledLabel(String text, int size, int style, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", style, size));
        lbl.setForeground(color);
        return lbl;
    }

    private void styleInput(JComponent field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 220)),
                new EmptyBorder(6, 10, 6, 10)));
    }

    public void onLogin(Runnable handler) {
        btnLogin.addActionListener(e -> handler.run());
        fieldPassword.addActionListener(e -> handler.run());
    }

    public String getId()       { return fieldId.getText().trim(); }
    public String getPassword() { return new String(fieldPassword.getPassword()); }
    public void showError(String msg) { labelError.setText(msg); }
    public void close()               { frame.dispose(); }
}