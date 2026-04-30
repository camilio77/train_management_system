package co.edu.upb.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class RegisterClientView {

    @FunctionalInterface
    public interface RegisterHandler {
        void handle(String id, String names, String lastNames,
                    String idType, String address,
                    String password, String confirm);
    }

    private JFrame         frame;
    private JTextField     fieldId, fieldNames, fieldLastNames, fieldAddress;
    private JComboBox<String> fieldIdType;
    private JPasswordField fieldPassword, fieldConfirm;
    private JLabel         labelError;
    private JButton        btnRegister, btnBack;

    public RegisterClientView() {
        build();
    }

    private void build() {
        frame = new JFrame("Train Management — Registro");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // ── Panel izquierdo ───────────────────────────────────────
        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(new Color(30, 58, 95));
        left.setPreferredSize(new Dimension(320, 600));

        JLabel icon = new JLabel("🚆", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        icon.setForeground(Color.WHITE);

        JLabel appTitle = new JLabel("Nuevo Pasajero", SwingConstants.CENTER);
        appTitle.setFont(new Font("Arial", Font.BOLD, 22));
        appTitle.setForeground(Color.WHITE);

        JLabel appSub = new JLabel("Crea tu cuenta aquí", SwingConstants.CENTER);
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
        right.setBorder(new EmptyBorder(30, 50, 30, 50));

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JLabel title = label("Crear Cuenta", 24, Font.BOLD, new Color(30, 58, 95));
        JLabel sub   = label("Completa tus datos para registrarte", 13, Font.PLAIN, new Color(120, 130, 145));

        // Campos en dos columnas
        JPanel fields = new JPanel(new GridLayout(0, 2, 16, 12));
        fields.setOpaque(false);
        fields.setAlignmentX(Component.LEFT_ALIGNMENT);

        fieldId        = addField(fields, "Identificación");
        fieldNames     = addField(fields, "Nombres");
        fieldLastNames = addField(fields, "Apellidos");
        fields.add(styledLabel("Tipo de identificación"));
        fieldIdType = new JComboBox<>(new String[]{"CC", "TI", "CE"});
        fields.add(fieldIdType);
        fieldAddress = addField(fields, "Dirección");

        JLabel lblPass    = label("Contraseña", 12, Font.BOLD, new Color(60, 70, 85));
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPassword = new JPasswordField();
        styleComp(fieldPassword);

        JLabel lblConfirm = label("Confirmar contraseña", 12, Font.BOLD, new Color(60, 70, 85));
        lblConfirm.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldConfirm = new JPasswordField();
        styleComp(fieldConfirm);

        labelError = new JLabel(" ", SwingConstants.LEFT);
        labelError.setFont(new Font("Arial", Font.PLAIN, 12));
        labelError.setForeground(new Color(200, 50, 50));
        labelError.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnRegister = styledButton("Crear cuenta", new Color(30, 58, 95), Color.WHITE);

        btnBack = new JButton("← Volver al login");
        btnBack.setFont(new Font("Arial", Font.PLAIN, 12));
        btnBack.setForeground(new Color(30, 58, 95));
        btnBack.setBackground(new Color(245, 247, 250));
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(title);         form.add(Box.createVerticalStrut(4));
        form.add(sub);           form.add(Box.createVerticalStrut(20));
        form.add(fields);        form.add(Box.createVerticalStrut(12));
        form.add(lblPass);       form.add(Box.createVerticalStrut(6));
        form.add(fieldPassword); form.add(Box.createVerticalStrut(12));
        form.add(lblConfirm);    form.add(Box.createVerticalStrut(6));
        form.add(fieldConfirm);  form.add(Box.createVerticalStrut(8));
        form.add(labelError);    form.add(Box.createVerticalStrut(8));
        form.add(btnRegister);   form.add(Box.createVerticalStrut(8));
        form.add(btnBack);

        right.add(form);

        frame.add(left, BorderLayout.WEST);
        frame.add(right, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // ── Métodos para que el controller enlace acciones ────────────

    public void onRegister(RegisterHandler handler) {
        btnRegister.addActionListener(e -> handler.handle(
            fieldId.getText().trim(),
            fieldNames.getText().trim(),
            fieldLastNames.getText().trim(),
            (String) fieldIdType.getSelectedItem(),
            fieldAddress.getText().trim(),
            new String(fieldPassword.getPassword()),
            new String(fieldConfirm.getPassword())
        ));
    }

    public void onBack(Runnable handler) {
        btnBack.addActionListener(e -> handler.run());
    }

    public void showError(String msg) { labelError.setText(msg); }
    public void showSuccess(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    public void close() { frame.dispose(); }

    // ── Helpers de estilo ─────────────────────────────────────────

    private JTextField addField(JPanel panel, String labelText) {
        panel.add(styledLabel(labelText));
        JTextField f = new JTextField();
        styleComp(f);
        panel.add(f);
        return f;
    }

    private JLabel styledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setForeground(new Color(60, 70, 85));
        return lbl;
    }

    private JLabel label(String text, int size, int style, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", style, size));
        lbl.setForeground(color);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void styleComp(JComponent c) {
        c.setFont(new Font("Arial", Font.PLAIN, 13));
        c.setPreferredSize(new Dimension(0, 34));
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220)),
            new EmptyBorder(5, 10, 5, 10)
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