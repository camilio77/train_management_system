package co.edu.upb.ticketMachine.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import co.edu.upb.client.view.BuyTicketView.StationMapPanel;

public class TicketMachineView {

    private JFrame frame;
    private JComboBox<String> comboCategoria;
    private JTextField fieldPeso1;
    private JTextField fieldPeso2;
    private JCheckBox chkEquipaje1;
    private JCheckBox chkEquipaje2;
    private JLabel lblPrecio;
    private JLabel lblInfo;
    private JLabel lblRutaPreview;
    private JLabel lblUsuario;
    private JButton btnComprar;
    private JButton btnVolver;

    private StationMapPanel mapPanel;
    private String selectedOrigen;
    private String selectedDestino;

    private static final Color DARK_BLUE = new Color(20, 60, 110);
    private static final Color BG = new Color(245, 247, 250);
    private static final int PRECIO_PREMIUM = 150000;
    private static final int PRECIO_EJECUTIVA = 80000;
    private static final int PRECIO_ESTANDAR = 40000;

    public TicketMachineView(String nombreUsuario) {
        frame = new JFrame("Máquina de Tickets — Compra");
        frame.setSize(950, 640);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(DARK_BLUE);
        header.setBorder(new EmptyBorder(12, 24, 12, 24));

        JLabel title = new JLabel("🎟️ Máquina de Tickets");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(Color.WHITE);

        lblUsuario = new JLabel("👤 " + nombreUsuario);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 13));
        lblUsuario.setForeground(new Color(180, 200, 220));

        btnVolver = new JButton("← Volver");
        btnVolver.setBackground(new Color(80, 100, 130));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        headerRight.setOpaque(false);
        headerRight.add(lblUsuario);
        headerRight.add(btnVolver);

        header.add(title, BorderLayout.WEST);
        header.add(headerRight, BorderLayout.EAST);

        mapPanel = new StationMapPanel();
        mapPanel.setPreferredSize(new Dimension(530, 510));
        mapPanel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(12, 12, 12, 6),
                BorderFactory.createLineBorder(new Color(200, 210, 220))));
        mapPanel.setOnSelectionChanged((origen, destino) -> {
            this.selectedOrigen = origen;
            this.selectedDestino = destino;
            updateRoutePreview(origen, destino);
        });

        JPanel form = new JPanel();
        form.setBackground(BG);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(16, 10, 16, 16));
        form.setPreferredSize(new Dimension(330, 510));

        lblRutaPreview = new JLabel(
                "<html><i>Selecciona origen y destino en el mapa</i></html>");
        lblRutaPreview.setFont(new Font("Arial", Font.PLAIN, 12));
        lblRutaPreview.setForeground(new Color(80, 100, 130));
        lblRutaPreview.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblRutaPreview);
        form.add(Box.createVerticalStrut(14));

        form.add(sectionLabel("Categoría:"));
        form.add(Box.createVerticalStrut(5));
        comboCategoria = new JComboBox<>(new String[] { "ESTANDAR", "EJECUTIVA", "PREMIUM" });
        comboCategoria.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        comboCategoria.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(comboCategoria);

        form.add(Box.createVerticalStrut(5));
        lblInfo = new JLabel(getAsientoInfo("ESTANDAR"));
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 11));
        lblInfo.setForeground(new Color(100, 110, 130));
        lblInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblInfo);

        form.add(Box.createVerticalStrut(10));
        lblPrecio = new JLabel("💰 Precio por tramo: $" + formatPrecio(PRECIO_ESTANDAR));
        lblPrecio.setFont(new Font("Arial", Font.BOLD, 13));
        lblPrecio.setForeground(new Color(30, 130, 70));
        lblPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblPrecio);

        form.add(Box.createVerticalStrut(16));
        form.add(sectionLabel("Equipaje (máx. 2 maletas, 80 kg c/u):"));
        form.add(Box.createVerticalStrut(8));

        chkEquipaje1 = new JCheckBox("Maleta 1:");
        chkEquipaje1.setSelected(false);
        chkEquipaje1.setOpaque(false);
        chkEquipaje1.setFont(new Font("Arial", Font.PLAIN, 12));
        fieldPeso1 = weightField();
        fieldPeso1.setEnabled(false);
        chkEquipaje1.addActionListener(e -> fieldPeso1.setEnabled(chkEquipaje1.isSelected()));
        form.add(luggageRow(chkEquipaje1, fieldPeso1));
        form.add(Box.createVerticalStrut(6));

        chkEquipaje2 = new JCheckBox("Maleta 2:");
        chkEquipaje2.setSelected(false);
        chkEquipaje2.setOpaque(false);
        chkEquipaje2.setFont(new Font("Arial", Font.PLAIN, 12));
        fieldPeso2 = weightField();
        fieldPeso2.setEnabled(false);
        chkEquipaje2.addActionListener(e -> fieldPeso2.setEnabled(chkEquipaje2.isSelected()));
        form.add(luggageRow(chkEquipaje2, fieldPeso2));

        form.add(Box.createVerticalStrut(12));
        JLabel nota = new JLabel(
                "<html><small>* El precio se multiplica por cada tramo de la ruta</small></html>");
        nota.setForeground(new Color(130, 140, 155));
        nota.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(nota);

        form.add(Box.createVerticalGlue());

        btnComprar = new JButton("✔ Confirmar compra");
        btnComprar.setBackground(new Color(34, 139, 80));
        btnComprar.setForeground(Color.WHITE);
        btnComprar.setFocusPainted(false);
        btnComprar.setBorderPainted(false);
        btnComprar.setFont(new Font("Arial", Font.BOLD, 14));
        btnComprar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnComprar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnComprar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        form.add(btnComprar);

        comboCategoria.addActionListener(e -> {
            String cat = (String) comboCategoria.getSelectedItem();
            int precio = switch (cat) {
                case "PREMIUM" -> PRECIO_PREMIUM;
                case "EJECUTIVA" -> PRECIO_EJECUTIVA;
                default -> PRECIO_ESTANDAR;
            };
            lblPrecio.setText("💰 Precio por tramo: $" + formatPrecio(precio));
            lblInfo.setText(getAsientoInfo(cat));
        });

        JPanel center = new JPanel(new BorderLayout());
        center.add(mapPanel, BorderLayout.CENTER);
        center.add(form, BorderLayout.EAST);

        frame.add(header, BorderLayout.NORTH);
        frame.add(center, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JTextField weightField() {
        JTextField f = new JTextField("0", 6);
        f.setFont(new Font("Arial", Font.PLAIN, 12));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 220)),
                new EmptyBorder(3, 6, 3, 6)));
        return f;
    }

    private JPanel luggageRow(JCheckBox chk, JTextField field) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        row.add(chk);
        row.add(field);
        row.add(new JLabel("kg"));
        return row;
    }

    private JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setForeground(new Color(60, 70, 85));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void updateRoutePreview(String origen, String destino) {
        if (origen == null && destino == null)
            lblRutaPreview.setText(
                    "<html><i>Selecciona origen y destino en el mapa</i></html>");
        else if (destino == null)
            lblRutaPreview.setText("<html>🟢 <b>Origen:</b> " + origen
                    + "<br><i>Haz clic en el destino...</i></html>");
        else
            lblRutaPreview.setText("<html>🟢 <b>Origen:</b> " + origen
                    + "<br>🔴 <b>Destino:</b> " + destino + "</html>");
    }

    private String getAsientoInfo(String cat) {
        return switch (cat) {
            case "PREMIUM" -> "Zona Premium: asientos 1-4";
            case "EJECUTIVA" -> "Zona Ejecutiva: asientos 5-12";
            default -> "Zona Estándar: asientos 13-34";
        };
    }

    private String formatPrecio(int p) {
        return String.format("%,d", p).replace(",", ".");
    }

    public void loadStations(List<String> stations, List<int[]> connections) {
        mapPanel.setStations(stations, connections);
    }

    public void highlightPath(List<String> path) {
        mapPanel.highlightPath(path);
    }

    public String getOrigen() {
        return selectedOrigen;
    }

    public String getDestino() {
        return selectedDestino;
    }

    public String getCategoria() {
        return (String) comboCategoria.getSelectedItem();
    }

    public double getPeso1() {
        if (!chkEquipaje1.isSelected())
            return 0;
        try {
            return Double.parseDouble(fieldPeso1.getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public double getPeso2() {
        if (!chkEquipaje2.isSelected())
            return 0;
        try {
            return Double.parseDouble(fieldPeso2.getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void onComprar(Runnable handler) {
        btnComprar.addActionListener(e -> handler.run());
    }

    public void onVolver(Runnable handler) {
        btnVolver.addActionListener(e -> handler.run());
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "¡Compra exitosa!",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void close() {
        frame.dispose();
    }
}