package co.edu.upb.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.function.BiConsumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class LoginClientView {

    private JFrame         frame;
    private JTextField     fieldId;
    private JPasswordField fieldPassword;
    private JLabel         labelError;
    private JButton        btnLogin;
    private JButton        btnRegister;

    public LoginClientView() {
        build();
    }

    private void build() {
        frame = new JFrame("Train Management — Login");
        frame.setSize(900, 550);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // ── Panel izquierdo ───────────────────────────────────────
        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(new Color(30, 58, 95));
        left.setPreferredSize(new Dimension(380, 550));

        JLabel icon = new JLabel("🚆", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        icon.setForeground(Color.WHITE);

        JLabel appTitle = new JLabel("Train Management", SwingConstants.CENTER);
        appTitle.setFont(new Font("Arial", Font.BOLD, 22));
        appTitle.setForeground(Color.WHITE);

        JLabel appSub = new JLabel("Portal de pasajeros", SwingConstants.CENTER);
        appSub.setFont(new Font("Arial", Font.PLAIN, 13));
        appSub.setForeground(new Color(180, 200, 220));

        JPanel leftContent = new JPanel();
        leftContent.setOpaque(false);
        leftContent.setLayout(new BoxLayout(leftContent, BoxLayout.Y_AXIS));
        leftContent.setBorder(new EmptyBorder(120, 40, 40, 40));
        leftContent.add(icon);
        leftContent.add(Box.createVerticalStrut(16));
        leftContent.add(appTitle);
        leftContent.add(Box.createVerticalStrut(8));
        leftContent.add(appSub);
        left.add(leftContent, BorderLayout.CENTER);

        // ── Panel derecho — formulario ────────────────────────────
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(new Color(245, 247, 250));
        right.setBorder(new EmptyBorder(40, 50, 40, 50));

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JLabel loginTitle = label("Iniciar Sesión", 24, Font.BOLD, new Color(30, 58, 95));
        JLabel loginSub   = label("Ingresa tus credenciales", 13, Font.PLAIN, new Color(120, 130, 145));

        JLabel lblId   = label("Identificación", 12, Font.BOLD, new Color(60, 70, 85));
        fieldId = styledField();

        JLabel lblPass = label("Contraseña", 12, Font.BOLD, new Color(60, 70, 85));
        fieldPassword  = new JPasswordField();
        styleComponent(fieldPassword);

        labelError = new JLabel(" ", SwingConstants.LEFT);
        labelError.setFont(new Font("Arial", Font.PLAIN, 12));
        labelError.setForeground(new Color(200, 50, 50));
        labelError.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnLogin = styledButton("Ingresar", new Color(30, 58, 95), Color.WHITE);
        btnRegister = new JButton("Registrarse");
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

        JLabel orLabel = label("¿No tienes cuenta?", 12, Font.PLAIN, new Color(120, 130, 145));

        form.add(loginTitle);      form.add(Box.createVerticalStrut(4));
        form.add(loginSub);        form.add(Box.createVerticalStrut(28));
        form.add(lblId);           form.add(Box.createVerticalStrut(6));
        form.add(fieldId);         form.add(Box.createVerticalStrut(16));
        form.add(lblPass);         form.add(Box.createVerticalStrut(6));
        form.add(fieldPassword);   form.add(Box.createVerticalStrut(8));
        form.add(labelError);      form.add(Box.createVerticalStrut(8));
        form.add(btnLogin);        form.add(Box.createVerticalStrut(16));
        form.add(orLabel);         form.add(Box.createVerticalStrut(8));
        form.add(btnRegister);

        right.add(form);

        frame.add(left, BorderLayout.WEST);
        frame.add(right, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // ── Métodos para que el controller enlace acciones ────────────

    public void onLogin(BiConsumer<String, String> handler) {
        btnLogin.addActionListener(e -> {
            String id  = fieldId.getText().trim();
            String pwd = new String(fieldPassword.getPassword());
            handler.accept(id, pwd);
        });
        // Enter en password también dispara login
        fieldPassword.addActionListener(e -> {
            String id  = fieldId.getText().trim();
            String pwd = new String(fieldPassword.getPassword());
            handler.accept(id, pwd);
        });
    }

    public void onRegister(Runnable handler) {
        btnRegister.addActionListener(e -> handler.run());
    }

    public void showError(String msg)   { labelError.setText(msg); }
    public void close()                 { frame.dispose(); }

    // ── Helpers de estilo ─────────────────────────────────────────

    private JLabel label(String text, int size, int style, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", style, size));
        lbl.setForeground(color);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField styledField() {
        JTextField f = new JTextField();
        styleComponent(f);
        return f;
    }

    private void styleComponent(javax.swing.JComponent c) {
        c.setFont(new Font("Arial", Font.PLAIN, 14));
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220)),
            new EmptyBorder(6, 10, 6, 10)
        ));
    }

    private JButton styledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }
}