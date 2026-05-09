package ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;
import service.ApplicationService;
import model.User;

public class ViewApplicationsUI {

    private static final Color BG       = new Color(10,  10,  14);
    private static final Color CARD     = new Color(18,  18,  24);
    private static final Color BORDER   = new Color(40,  40,  55);
    private static final Color GOLD     = new Color(212, 175, 55);
    private static final Color GOLD_DIM = new Color(140, 110, 30);
    private static final Color FG       = new Color(220, 220, 230);
    private static final Color FG_DIM   = new Color(120, 120, 140);
    private static final Color INPUT_BG = new Color(24,  24,  32);
    private static final Color SUCCESS  = new Color(80,  200, 120);
    private static final Color ERROR    = new Color(220, 70,  70);
    private static final Color ROW_ALT  = new Color(22,  22,  30);

    // Status badge colors
    private static Color statusColor(String s) {
        switch (s) {
            case "SHORTLISTED": return new Color(80,  160, 220);
            case "INTERVIEW":   return new Color(180, 120, 220);
            case "SELECTED":    return new Color(80,  200, 120);
            case "REJECTED":    return new Color(220, 70,  70);
            default:            return new Color(180, 180, 100); // APPLIED
        }
    }

    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel statusLabel;
    private ApplicationService as = new ApplicationService();

    public ViewApplicationsUI(User user) {
        JFrame f = new JFrame("Job Portal — Applications");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setSize(900, 640);
        f.setLocationRelativeTo(null);
        f.setResizable(true);

        LoginUI.BackgroundPanel root = new LoginUI.BackgroundPanel();
        root.setLayout(new GridBagLayout());
        f.setContentPane(root);

        JPanel card = createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(32, 40, 32, 40));

        // ── Header ────────────────────────────────────────────────────────────
        JLabel logo = new JLabel("⬡  JOB PORTAL");
        logo.setFont(new Font("SansSerif", Font.BOLD, 11));
        logo.setForeground(GOLD);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Application Dashboard");
        title.setFont(new Font("Georgia", Font.BOLD, 24));
        title.setForeground(FG);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Select an application to update its status");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(FG_DIM);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setBorder(BorderFactory.createEmptyBorder(6, 0, 20, 0));

        // ── Table ─────────────────────────────────────────────────────────────
        String[] cols = {"App ID", "Candidate", "Email", "Job Title", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (isRowSelected(row)) {
                    c.setBackground(new Color(212, 175, 55, 45));
                    c.setForeground(GOLD);
                } else {
                    c.setBackground(row % 2 == 0 ? INPUT_BG : ROW_ALT);
                    // Color the status column
                    if (col == 4) {
                        String val = (String) tableModel.getValueAt(row, 4);
                        c.setForeground(statusColor(val));
                    } else {
                        c.setForeground(FG);
                    }
                }
                return c;
            }
        };
        table.setBackground(INPUT_BG);
        table.setForeground(FG);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(30, 30, 40));
        header.setForeground(GOLD);
        header.setFont(new Font("SansSerif", Font.BOLD, 11));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        header.setReorderingAllowed(false);

        table.getColumnModel().getColumn(0).setPreferredWidth(55);
        table.getColumnModel().getColumn(1).setPreferredWidth(110);
        table.getColumnModel().getColumn(2).setPreferredWidth(160);
        table.getColumnModel().getColumn(3).setPreferredWidth(160);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);

        JScrollPane sp = new JScrollPane(table);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getViewport().setBackground(INPUT_BG);
        sp.setBorder(new LineBorder(BORDER, 1));
        sp.setPreferredSize(new Dimension(0, 320));
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        sp.setAlignmentX(Component.CENTER_ALIGNMENT);

        loadData(); // populate table

        // ── Status update row ─────────────────────────────────────────────────
        JPanel controlRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        controlRow.setOpaque(false);
        controlRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        controlRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] statusOptions = {"SHORTLISTED", "INTERVIEW", "SELECTED", "REJECTED"};
        JComboBox<String> statusBox = styledCombo(statusOptions);

        JButton updateBtn = goldButton("Update Status");
        JButton refreshBtn = ghostButton("↻ Refresh");

        controlRow.add(new JLabel("   ") {{setForeground(FG_DIM);}});
        controlRow.add(statusBox);
        controlRow.add(updateBtn);
        controlRow.add(refreshBtn);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setForeground(SUCCESS);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        // ── Actions ───────────────────────────────────────────────────────────
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                showStatus("Please select an application first.", false);
                return;
            }
            int appId = Integer.parseInt((String) tableModel.getValueAt(row, 0));
            String newStatus = (String) statusBox.getSelectedItem();
            String result = as.updateStatus(appId, newStatus);
            switch (result) {
                case "OK":
                    showStatus("✓  Status updated to " + newStatus, true);
                    loadData();
                    break;
                case "INVALID_TRANSITION":
                    showStatus("Invalid transition! Check the allowed flow.", false);
                    break;
                case "NOT_FOUND":
                    showStatus("Application not found.", false);
                    break;
                default:
                    showStatus("Something went wrong.", false);
            }
        });

        refreshBtn.addActionListener(e -> { loadData(); showStatus("Refreshed.", true); });

        // ── Transition hint ───────────────────────────────────────────────────
        JLabel hint = new JLabel("Flow:  APPLIED → SHORTLISTED → INTERVIEW → SELECTED / REJECTED");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 11));
        hint.setForeground(FG_DIM);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        hint.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // ── Assemble ──────────────────────────────────────────────────────────
        card.add(logo);
        card.add(Box.createVerticalStrut(8));
        card.add(title);
        card.add(sub);
        card.add(sp);
        card.add(Box.createVerticalStrut(16));
        card.add(labelFor("SELECT ROW  →  CHOOSE STATUS  →  CLICK UPDATE"));
        card.add(Box.createVerticalStrut(10));
        card.add(controlRow);
        card.add(statusLabel);
        card.add(hint);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 24, 0, 24);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        root.add(card, gbc);
        f.setVisible(true);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<String[]> rows = as.getAllApplications();
        for (String[] row : rows) tableModel.addRow(row);
    }

    private void showStatus(String msg, boolean ok) {
        statusLabel.setForeground(ok ? SUCCESS : ERROR);
        statusLabel.setText(msg);
        Timer t = new Timer(2500, e -> statusLabel.setText(" "));
        t.setRepeats(false); t.start();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private JPanel createCard() {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 20, 20));
                g2.dispose();
            }
            @Override public boolean isOpaque() { return false; }
        };
    }

    private JComboBox<String> styledCombo(String[] options) {
        JComboBox<String> box = new JComboBox<>(options);
        box.setBackground(INPUT_BG);
        box.setForeground(FG);
        box.setFont(new Font("SansSerif", Font.PLAIN, 13));
        box.setBorder(BorderFactory.createLineBorder(BORDER));
        box.setFocusable(false);
        return box;
    }

    private JButton goldButton(String text) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c1 = getModel().isRollover() ? new Color(230, 190, 60) : GOLD;
                Color c2 = getModel().isRollover() ? new Color(180, 140, 20) : GOLD_DIM;
                g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose(); super.paintComponent(g);
            }
            @Override public boolean isOpaque() { return false; }
        };
        b.setForeground(new Color(10, 10, 14));
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBorderPainted(false); b.setContentAreaFilled(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(140, 36));
        return b;
    }

    private JButton ghostButton(String text) {
        JButton b = new JButton(text);
        b.setForeground(FG_DIM);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setBorderPainted(false); b.setContentAreaFilled(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(100, 36));
        return b;
    }

    private JLabel labelFor(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 10));
        l.setForeground(FG_DIM);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }
}
