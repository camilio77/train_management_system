package co.edu.upb.client.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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

public class BuyTicketView {
    private JFrame frame;
    private JComboBox<String> comboCategoria;
    private JTextField fieldPeso1;
    private JTextField fieldPeso2;
    private JCheckBox chkEquipaje1;
    private JCheckBox chkEquipaje2;
    private JLabel lblPrecio;
    private JLabel lblInfo;
    private JLabel lblRutaPreview;
    private JButton btnComprar;
    private JButton btnCancelar;

    // Mapa visual
    private StationMapPanel mapPanel;
    private String selectedOrigen;
    private String selectedDestino;

    private static final Color DARK_BLUE  = new Color(30, 58, 95);
    private static final Color BG         = new Color(245, 247, 250);
    private static final int PRECIO_PREMIUM   = 150000;
    private static final int PRECIO_EJECUTIVA = 80000;
    private static final int PRECIO_ESTANDAR  = 40000;

    public BuyTicketView() {
        frame = new JFrame("Comprar Ticket");
        frame.setSize(900, 620);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setLayout(new BorderLayout());

        // ── Header ────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(DARK_BLUE);
        header.setBorder(new EmptyBorder(12, 24, 12, 24));
        JLabel title = new JLabel("🎫 Comprar Ticket");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        JLabel instruccion = new JLabel("Haz clic en dos estaciones para elegir origen y destino");
        instruccion.setFont(new Font("Arial", Font.PLAIN, 12));
        instruccion.setForeground(new Color(180, 200, 220));
        header.add(title, BorderLayout.WEST);
        header.add(instruccion, BorderLayout.EAST);

        // ── Panel izquierdo: mapa ──────────────────────────────────
        mapPanel = new StationMapPanel();
        mapPanel.setPreferredSize(new Dimension(520, 500));
        mapPanel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(12, 12, 12, 6),
            BorderFactory.createLineBorder(new Color(200, 210, 220))));
        mapPanel.setOnSelectionChanged((origen, destino) -> {
            this.selectedOrigen  = origen;
            this.selectedDestino = destino;
            updateRoutePreview(origen, destino);
        });

        // ── Panel derecho: formulario ──────────────────────────────
        JPanel form = new JPanel();
        form.setBackground(BG);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(16, 10, 16, 16));
        form.setPreferredSize(new Dimension(320, 500));

        // Selección actual
        lblRutaPreview = new JLabel("<html><i>Selecciona origen y destino en el mapa</i></html>");
        lblRutaPreview.setFont(new Font("Arial", Font.PLAIN, 12));
        lblRutaPreview.setForeground(new Color(80, 100, 130));
        lblRutaPreview.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblRutaPreview);
        form.add(Box.createVerticalStrut(14));

        // Categoría
        form.add(styledLabel("Categoría:"));
        form.add(Box.createVerticalStrut(5));
        comboCategoria = new JComboBox<>(new String[]{"ESTANDAR", "EJECUTIVA", "PREMIUM"});
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

        // ── Equipaje ──────────────────────────────────────────────
        form.add(styledLabel("Equipaje (máx. 2 maletas, 80 kg c/u):"));
        form.add(Box.createVerticalStrut(8));

        // Equipaje 1
        // ── Equipaje 1 ────────────────────────────────────────────────
        chkEquipaje1 = new JCheckBox("Maleta 1:");
        chkEquipaje1.setSelected(true);
        chkEquipaje1.setOpaque(false);
        chkEquipaje1.setFont(new Font("Arial", Font.PLAIN, 12));

        fieldPeso1 = new JTextField("0", 6);
        fieldPeso1.setEnabled(true);
        fieldPeso1.setFont(new Font("Arial", Font.PLAIN, 12));
        fieldPeso1.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220)),
            new EmptyBorder(3, 6, 3, 6)));
        chkEquipaje1.addActionListener(e -> fieldPeso1.setEnabled(chkEquipaje1.isSelected()));

        JPanel eq1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        eq1Panel.setOpaque(false);
        eq1Panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        eq1Panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        eq1Panel.add(chkEquipaje1);
        eq1Panel.add(fieldPeso1);
        eq1Panel.add(new JLabel("kg"));
        form.add(eq1Panel);
        form.add(Box.createVerticalStrut(6));

        // ── Equipaje 2 ────────────────────────────────────────────────
        chkEquipaje2 = new JCheckBox("Maleta 2:");
        chkEquipaje2.setSelected(false);
        chkEquipaje2.setOpaque(false);
        chkEquipaje2.setFont(new Font("Arial", Font.PLAIN, 12));

        fieldPeso2 = new JTextField("0", 6);
        fieldPeso2.setEnabled(false);
        fieldPeso2.setFont(new Font("Arial", Font.PLAIN, 12));
        fieldPeso2.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220)),
            new EmptyBorder(3, 6, 3, 6)));
        chkEquipaje2.addActionListener(e -> fieldPeso2.setEnabled(chkEquipaje2.isSelected()));

        JPanel eq2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        eq2Panel.setOpaque(false);
        eq2Panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        eq2Panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        eq2Panel.add(chkEquipaje2);
        eq2Panel.add(fieldPeso2);
        eq2Panel.add(new JLabel("kg"));
        form.add(eq2Panel);

        form.add(Box.createVerticalStrut(20));

        // ── Resumen precio total ───────────────────────────────────
        JLabel lblNota = new JLabel("<html><small>* El precio se multiplica por cada tramo de la ruta</small></html>");
        lblNota.setForeground(new Color(130, 140, 155));
        lblNota.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblNota);

        form.add(Box.createVerticalGlue());

        // ── Botones ───────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> frame.dispose());

        btnComprar = new JButton("✔ Confirmar compra");
        btnComprar.setBackground(DARK_BLUE);
        btnComprar.setForeground(Color.WHITE);
        btnComprar.setFocusPainted(false);
        btnComprar.setBorderPainted(false);
        btnComprar.setFont(new Font("Arial", Font.BOLD, 13));
        btnComprar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnPanel.add(btnCancelar);
        btnPanel.add(btnComprar);
        form.add(btnPanel);

        // Listener categoría
        comboCategoria.addActionListener(e -> {
            String cat = (String) comboCategoria.getSelectedItem();
            int precio = switch (cat) {
                case "PREMIUM"   -> PRECIO_PREMIUM;
                case "EJECUTIVA" -> PRECIO_EJECUTIVA;
                default          -> PRECIO_ESTANDAR;
            };
            lblPrecio.setText("💰 Precio por tramo: $" + formatPrecio(precio));
            lblInfo.setText(getAsientoInfo(cat));
        });

        // ── Armar ventana ─────────────────────────────────────────
        JPanel center = new JPanel(new BorderLayout());
        center.add(mapPanel, BorderLayout.CENTER);
        center.add(form, BorderLayout.EAST);

        frame.add(header, BorderLayout.NORTH);
        frame.add(center, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Fila de equipaje con checkbox + label + campo peso
    private JPanel buildLuggageRow(String label, boolean enabledByDefault) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JCheckBox chk = new JCheckBox(label);
        chk.setSelected(enabledByDefault);
        chk.setOpaque(false);
        chk.setFont(new Font("Arial", Font.PLAIN, 12));

        JTextField field = new JTextField("0", 6);
        field.setEnabled(enabledByDefault);
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220)),
            new EmptyBorder(3, 6, 3, 6)));

        JLabel kgLabel = new JLabel("kg");
        kgLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        chk.addActionListener(e -> field.setEnabled(chk.isSelected()));

        row.add(chk);
        row.add(field);
        row.add(kgLabel);
        return row;
    }

    private void updateRoutePreview(String origen, String destino) {
        if (origen == null && destino == null) {
            lblRutaPreview.setText("<html><i>Selecciona origen y destino en el mapa</i></html>");
        } else if (destino == null) {
            lblRutaPreview.setText("<html>🟢 <b>Origen:</b> " + origen +
                "<br><i>Haz clic en el destino...</i></html>");
        } else {
            lblRutaPreview.setText("<html>🟢 <b>Origen:</b> " + origen +
                "<br>🔴 <b>Destino:</b> " + destino + "</html>");
        }
    }

    // ── Carga estaciones en el mapa ───────────────────────────────
    public void loadStations(List<String> stations,
                             List<int[]> connections) { // connections: {idxA, idxB, km}
        mapPanel.setStations(stations, connections);
    }

    // ── Getters ───────────────────────────────────────────────────
    public String getOrigen()    { return selectedOrigen; }
    public String getDestino()   { return selectedDestino; }
    public String getCategoria() { return (String) comboCategoria.getSelectedItem(); }

    public double getPesoEquipaje1() {
        if (!chkEquipaje1.isSelected()) return 0;
        try { return Double.parseDouble(fieldPeso1.getText().trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    public double getPesoEquipaje2() {
        if (!chkEquipaje2.isSelected()) return 0;
        try { return Double.parseDouble(fieldPeso2.getText().trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    public void onBuy(Runnable handler) {
        btnComprar.addActionListener(e -> handler.run());
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Compra exitosa",
            JOptionPane.INFORMATION_MESSAGE);
    }

    public void highlightPath(List<String> path) {
        mapPanel.highlightPath(path);
    }

    public void close() { frame.dispose(); }

    // ── Helpers ───────────────────────────────────────────────────
    private String getAsientoInfo(String cat) {
        return switch (cat) {
            case "PREMIUM"   -> "Zona Premium: asientos 1-4";
            case "EJECUTIVA" -> "Zona Ejecutiva: asientos 5-12";
            default          -> "Zona Estándar: asientos 13-34";
        };
    }

    private String formatPrecio(int p) {
        return String.format("%,d", p).replace(",", ".");
    }

    private JLabel styledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setForeground(new Color(60, 70, 85));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    // ══════════════════════════════════════════════════════════════
    // Panel del mapa de estaciones (clase interna)
    // ══════════════════════════════════════════════════════════════
    public static class StationMapPanel extends JPanel {

        private List<String> stations = new ArrayList<>();
        private List<int[]>  connections = new ArrayList<>(); // {idxA, idxB, km}
        private Map<Integer, Point> positions = new HashMap<>();

        private Integer selectedOrigenIdx  = null;
        private Integer selectedDestinoIdx = null;
        private List<String> highlightedPath = new ArrayList<>();

        private SelectionListener listener;

        private static final int NODE_R   = 20;
        private static final Color C_NODE       = new Color(30, 58, 95);
        private static final Color C_ORIGEN     = new Color(34, 139, 80);
        private static final Color C_DESTINO    = new Color(200, 50, 50);
        private static final Color C_EDGE       = new Color(180, 190, 200);
        private static final Color C_EDGE_PATH  = new Color(255, 160, 0);
        private static final Color C_HOVER      = new Color(60, 100, 160);

        private Integer hoverIdx = null;

        public interface SelectionListener {
            void onChanged(String origen, String destino);
        }

        public StationMapPanel() {
            setBackground(Color.WHITE);
            setOpaque(true);

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override public void mouseMoved(MouseEvent e) {
                    Integer prev = hoverIdx;
                    hoverIdx = findNearestNode(e.getX(), e.getY());
                    if (!Objects.equals(prev, hoverIdx)) repaint();
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    Integer idx = findNearestNode(e.getX(), e.getY());
                    if (idx == null) return;
                    handleClick(idx);
                }
            });
        }

        public void setOnSelectionChanged(SelectionListener l) { this.listener = l; }

        public void setStations(List<String> stations, List<int[]> connections) {
            this.stations    = stations;
            this.connections = connections;
            this.selectedOrigenIdx  = null;
            this.selectedDestinoIdx = null;
            this.highlightedPath    = new ArrayList<>();
            layoutNodes();
            repaint();
        }

        public void highlightPath(List<String> path) {
            this.highlightedPath = path != null ? path : new ArrayList<>();
            repaint();
        }

        // Distribuye los nodos en círculo
        private void layoutNodes() {
            positions.clear();
            int n = stations.size();
            if (n == 0) return;
            int cx = 240, cy = 220, rx = 180, ry = 160;
            for (int i = 0; i < n; i++) {
                double angle = 2 * Math.PI * i / n - Math.PI / 2;
                int x = cx + (int)(rx * Math.cos(angle));
                int y = cy + (int)(ry * Math.sin(angle));
                positions.put(i, new Point(x, y));
            }
        }

        private void handleClick(int idx) {
            if (selectedOrigenIdx == null) {
                selectedOrigenIdx = idx;
                selectedDestinoIdx = null;
                highlightedPath = new ArrayList<>();
                notify(stations.get(idx), null);
            } else if (idx == selectedOrigenIdx) {
                // Deseleccionar origen
                selectedOrigenIdx = null;
                selectedDestinoIdx = null;
                highlightedPath = new ArrayList<>();
                notify(null, null);
            } else {
                selectedDestinoIdx = idx;
                notify(stations.get(selectedOrigenIdx), stations.get(idx));
            }
            repaint();
        }

        private void notify(String origen, String destino) {
            if (listener != null) listener.onChanged(origen, destino);
        }

        private Integer findNearestNode(int mx, int my) {
            for (Map.Entry<Integer, Point> e : positions.entrySet()) {
                Point p = e.getValue();
                if (Math.hypot(mx - p.x, my - p.y) <= NODE_R + 4)
                    return e.getKey();
            }
            return null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Construir set de aristas en la ruta resaltada
            Set<String> pathEdges = new HashSet<>();
            if (highlightedPath.size() >= 2) {
                for (int i = 0; i < highlightedPath.size() - 1; i++) {
                    int a = stations.indexOf(highlightedPath.get(i));
                    int b = stations.indexOf(highlightedPath.get(i + 1));
                    pathEdges.add(a + "-" + b);
                    pathEdges.add(b + "-" + a);
                }
            }

            // Dibujar aristas
            g2.setStroke(new BasicStroke(2));
            for (int[] conn : connections) {
                if (conn.length < 2) continue;
                Point pa = positions.get(conn[0]);
                Point pb = positions.get(conn[1]);
                if (pa == null || pb == null) continue;

                boolean enRuta = pathEdges.contains(conn[0] + "-" + conn[1]);
                g2.setColor(enRuta ? C_EDGE_PATH : C_EDGE);
                g2.setStroke(new BasicStroke(enRuta ? 3 : 1.5f));
                g2.drawLine(pa.x, pa.y, pb.x, pb.y);

                // Distancia en km
                int mx = (pa.x + pb.x) / 2;
                int my = (pa.y + pb.y) / 2;
                g2.setColor(new Color(100, 110, 130));
                g2.setFont(new Font("Arial", Font.PLAIN, 10));
                if (conn.length > 2)
                    g2.drawString(conn[2] + " km", mx + 3, my - 3);
            }

            // Dibujar nodos
            for (Map.Entry<Integer, Point> e : positions.entrySet()) {
                int idx = e.getKey();
                Point p = e.getValue();

                Color nodeColor;
                if (Integer.valueOf(idx).equals(selectedOrigenIdx))   nodeColor = C_ORIGEN;
                else if (Integer.valueOf(idx).equals(selectedDestinoIdx)) nodeColor = C_DESTINO;
                else if (Integer.valueOf(idx).equals(hoverIdx))        nodeColor = C_HOVER;
                else if (highlightedPath.contains(stations.get(idx))) nodeColor = C_EDGE_PATH;
                else                                                   nodeColor = C_NODE;

                // Sombra
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillOval(p.x - NODE_R + 2, p.y - NODE_R + 2, NODE_R * 2, NODE_R * 2);

                // Nodo
                g2.setColor(nodeColor);
                g2.fillOval(p.x - NODE_R, p.y - NODE_R, NODE_R * 2, NODE_R * 2);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(p.x - NODE_R, p.y - NODE_R, NODE_R * 2, NODE_R * 2);

                // Nombre de la estación
                String name = stations.get(idx);
                g2.setFont(new Font("Arial", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                int tw = fm.stringWidth(name);
                g2.setColor(new Color(30, 40, 60));
                g2.drawString(name, p.x - tw / 2, p.y + NODE_R + 14);
            }

            // Leyenda
            drawLegend(g2);
        }

        private void drawLegend(Graphics2D g2) {
            int x = 10, y = getHeight() - 60;
            g2.setFont(new Font("Arial", Font.PLAIN, 11));

            g2.setColor(C_ORIGEN);
            g2.fillOval(x, y, 12, 12);
            g2.setColor(Color.DARK_GRAY);
            g2.drawString("Origen", x + 16, y + 11);

            g2.setColor(C_DESTINO);
            g2.fillOval(x, y + 18, 12, 12);
            g2.setColor(Color.DARK_GRAY);
            g2.drawString("Destino", x + 16, y + 29);

            g2.setColor(C_EDGE_PATH);
            g2.fillOval(x, y + 36, 12, 12);
            g2.setColor(Color.DARK_GRAY);
            g2.drawString("Ruta calculada", x + 16, y + 47);
        }
    }
}