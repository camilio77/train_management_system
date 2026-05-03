package co.edu.upb.ticketMachine.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class UserDataView {

        private JFrame frame;
        private JTextField fieldId;
        private JTextField fieldNombre;
        private JLabel labelError;
        private JButton btnContinuar;

        private static final Color DARK_BLUE = new Color(20, 60, 110);
        private static final Color BG = new Color(245, 247, 250);

        public UserDataView() {
                frame = new JFrame("Máquina de Tickets — Identificación");
                frame.setSize(850, 500);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setLayout(new BorderLayout());

                JPanel left = new JPanel(new BorderLayout());
                left.setBackground(DARK_BLUE);
                left.setPreferredSize(new Dimension(340, 500));

                JPanel leftContent = new JPanel();
                leftContent.setOpaque(false);
                leftContent.setLayout(new BoxLayout(leftContent, BoxLayout.Y_AXIS));
                leftContent.setBorder(new EmptyBorder(80, 40, 40, 40));

                JLabel icon = new JLabel("🎟️", SwingConstants.CENTER);
                icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
                icon.setForeground(Color.WHITE);
                icon.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel appTitle = new JLabel("Máquina de Tickets", SwingConstants.CENTER);
                appTitle.setFont(new Font("Arial", Font.BOLD, 20));
                appTitle.setForeground(Color.WHITE);
                appTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel appSub = new JLabel(
                                "<html><center>Compra tu tiquete de tren<br>sin necesidad de registro</center></html>",
                                SwingConstants.CENTER);
                appSub.setFont(new Font("Arial", Font.PLAIN, 13));
                appSub.setForeground(new Color(160, 190, 220));
                appSub.setAlignmentX(Component.CENTER_ALIGNMENT);

                leftContent.add(icon);
                leftContent.add(Box.createVerticalStrut(16));
                leftContent.add(appTitle);
                leftContent.add(Box.createVerticalStrut(10));
                leftContent.add(appSub);
                left.add(leftContent, BorderLayout.CENTER);

                JPanel right = new JPanel(new GridBagLayout());
                right.setBackground(BG);
                right.setBorder(new EmptyBorder(40, 50, 40, 50));

                JPanel form = new JPanel();
                form.setOpaque(false);
                form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

                JLabel titulo = styledLabel("Ingresa tus datos", 22, Font.BOLD,
                                new Color(20, 60, 110));
                titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel sub = styledLabel("Tu ticket quedará asociado a esta identificación",
                                12, Font.PLAIN, new Color(120, 130, 145));
                sub.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel lblId = styledLabel("Número de identificación", 12, Font.BOLD,
                                new Color(60, 70, 85));
                lblId.setAlignmentX(Component.LEFT_ALIGNMENT);

                fieldId = new JTextField();
                styleInput(fieldId);

                JLabel lblNombre = styledLabel("Nombre completo", 12, Font.BOLD,
                                new Color(60, 70, 85));
                lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

                fieldNombre = new JTextField();
                styleInput(fieldNombre);

                labelError = new JLabel(" ");
                labelError.setFont(new Font("Arial", Font.PLAIN, 12));
                labelError.setForeground(new Color(200, 50, 50));
                labelError.setAlignmentX(Component.LEFT_ALIGNMENT);

                btnContinuar = new JButton("Continuar →");
                btnContinuar.setBackground(DARK_BLUE);
                btnContinuar.setForeground(Color.WHITE);
                btnContinuar.setFocusPainted(false);
                btnContinuar.setBorderPainted(false);
                btnContinuar.setFont(new Font("Arial", Font.BOLD, 14));
                btnContinuar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
                btnContinuar.setAlignmentX(Component.LEFT_ALIGNMENT);
                btnContinuar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                form.add(titulo);
                form.add(Box.createVerticalStrut(4));
                form.add(sub);
                form.add(Box.createVerticalStrut(28));
                form.add(lblId);
                form.add(Box.createVerticalStrut(6));
                form.add(fieldId);
                form.add(Box.createVerticalStrut(14));
                form.add(lblNombre);
                form.add(Box.createVerticalStrut(6));
                form.add(fieldNombre);
                form.add(Box.createVerticalStrut(8));
                form.add(labelError);
                form.add(Box.createVerticalStrut(8));
                form.add(btnContinuar);

                right.add(form);

                frame.add(left, BorderLayout.WEST);
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

        public void onContinuar(Runnable handler) {
                btnContinuar.addActionListener(e -> handler.run());
                fieldNombre.addActionListener(e -> handler.run());
        }

        public String getId() {
                return fieldId.getText().trim();
        }

        public String getNombre() {
                return fieldNombre.getText().trim();
        }

        public void showError(String msg) {
                labelError.setText(msg);
        }

        public void close() {
                frame.dispose();
        }
}