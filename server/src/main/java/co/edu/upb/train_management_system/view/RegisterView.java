package co.edu.upb.train_management_system.view;

import java.awt.*;

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

import co.edu.upb.train_management_system.model.user.Passenger;
import co.edu.upb.train_management_system.model.user.UserService;

public class RegisterView {

    private JFrame frame;
    private JTextField fieldId;
    private JTextField fieldNames;
    private JTextField fieldLastNames;
    private JComboBox<String> fieldIdType;
    private JTextField fieldAddress;
    private JPasswordField fieldPassword;
    private JPasswordField fieldConfirmPassword;
    private JLabel labelError;

    public RegisterView() {
        frame = new JFrame("Train Management System — Registro");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Panel izquierdo — decorativo (mismo estilo que login)
        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(new Color(30, 58, 95));
        left.setPreferredSize(new Dimension(320, 600));

        JLabel icon = new JLabel("🚆", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        icon.setForeground(Color.WHITE);

        JLabel appTitle = new JLabel("Nuevo Pasajero", SwingConstants.CENTER);
        appTitle.setFont(new Font("Arial", Font.BOLD, 22));
        appTitle.setForeground(Color.WHITE);

        JLabel appSubtitle = new JLabel("Crea tu cuenta aquí", SwingConstants.CENTER);
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
        right.setBorder(new EmptyBorder(30, 50, 30, 50));

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Crear Cuenta");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(30, 58, 95));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Completa tus datos para registrarte");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(new Color(120, 130, 145));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Campos en dos columnas
        JPanel fields = new JPanel(new GridLayout(0, 2, 16, 12));
        fields.setOpaque(false);
        fields.setAlignmentX(Component.LEFT_ALIGNMENT);

        fieldId = addField(fields, "Identificación");
        fieldNames = addField(fields, "Nombres");
        fieldLastNames = addField(fields, "Apellidos");
        fields.add(styledLabel("Tipo de identificación"));
        fieldIdType = new JComboBox<>(new String[]{"CC", "TI", "CE"});
        fields.add(fieldIdType);
        fieldAddress = addField(fields, "Dirección");

        // Passwords van aparte para ocupar ancho completo
        JLabel lblPass = styledLabel("Contraseña");
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPassword = new JPasswordField();
        styleInput(fieldPassword);
        fieldPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblConfirm = styledLabel("Confirmar contraseña");
        lblConfirm.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldConfirmPassword = new JPasswordField();
        styleInput(fieldConfirmPassword);
        fieldConfirmPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        labelError = new JLabel(" ", SwingConstants.LEFT);
        labelError.setFont(new Font("Arial", Font.PLAIN, 12));
        labelError.setForeground(new Color(200, 50, 50));
        labelError.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnRegister = new JButton("Crear cuenta");
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegister.setBackground(new Color(30, 58, 95));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRegister.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnRegister.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnBack = new JButton("← Volver al login");
        btnBack.setFont(new Font("Arial", Font.PLAIN, 12));
        btnBack.setForeground(new Color(30, 58, 95));
        btnBack.setBackground(new Color(245, 247, 250));
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(title);
        form.add(Box.createVerticalStrut(4));
        form.add(subtitle);
        form.add(Box.createVerticalStrut(20));
        form.add(fields);
        form.add(Box.createVerticalStrut(12));
        form.add(lblPass);
        form.add(Box.createVerticalStrut(6));
        form.add(fieldPassword);
        form.add(Box.createVerticalStrut(12));
        form.add(lblConfirm);
        form.add(Box.createVerticalStrut(6));
        form.add(fieldConfirmPassword);
        form.add(Box.createVerticalStrut(8));
        form.add(labelError);
        form.add(Box.createVerticalStrut(8));
        form.add(btnRegister);
        form.add(Box.createVerticalStrut(8));
        form.add(btnBack);

        right.add(form);

        btnRegister.addActionListener(e -> handleRegister());
        btnBack.addActionListener(e -> {
            frame.dispose();
            new LoginView();
        });

        frame.add(left, BorderLayout.WEST);
        frame.add(right, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JTextField addField(JPanel panel, String label) {
        panel.add(styledLabel(label));
        JTextField field = new JTextField();
        styleInput(field);
        panel.add(field);
        return field;
    }

    private JLabel styledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setForeground(new Color(60, 70, 85));
        return lbl;
    }

    private void styleInput(JComponent field) {
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(0, 34));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 220)),
                new EmptyBorder(5, 10, 5, 10)
        ));
    }

    private void handleRegister() {
        String id = fieldId.getText().trim();
        String names = fieldNames.getText().trim();
        String lastNames = fieldLastNames.getText().trim();
        String idType = (String) fieldIdType.getSelectedItem();
        String address = fieldAddress.getText().trim();
        String password = new String(fieldPassword.getPassword());
        String confirm = new String(fieldConfirmPassword.getPassword());

        if (id.isEmpty() || names.isEmpty() || lastNames.isEmpty()
                || address.isEmpty() || password.isEmpty()) {
            labelError.setText("Completa todos los campos.");
            return;
        }

        if (!password.equals(confirm)) {
            labelError.setText("Las contraseñas no coinciden.");
            return;
        }

        try {
            String identificacion = id;
            Passenger passenger = new Passenger(
                    identificacion, names, lastNames, idType, address, password
            );
            UserService.getInstance().registerPassenger(passenger);
            JOptionPane.showMessageDialog(frame,
                    "¡Cuenta creada exitosamente!",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );
            frame.dispose();
            new LoginView();
        } catch (NumberFormatException ex) {
            labelError.setText("La identificación debe ser un número.");
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
