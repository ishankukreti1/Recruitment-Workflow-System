package ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;
import model.*;
import service.*;

public class ApplyJobUI {

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

    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel statusLabel;
    private final JobService js = new JobService();
    private final ApplicationService as = new ApplicationService();

    public ApplyJobUI(User user) {
        JFrame f = new JFrame("Job Portal — Browse & Apply");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setSize(700, 640);
        f.setLocationRelativeTo(null);
        f.setResizable(true);

        LoginUI.BackgroundPanel root = new LoginUI.BackgroundPanel();
        root.setLayout(new GridBagLayout());
        f.setContentPane(root);

        JPanel card = createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(36, 44, 36, 44));

        // ── Header ────────────────────────────────────────────────────────────
        JLabel logo = new JLabel("⬡  JOB PORTAL");
        logo.setFont(new Font("SansSerif", Font.BOLD, 11));
        logo.setForeground(GOLD);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Open Positions");
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(FG);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Click Refresh to load latest jobs, then apply");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(FG_DIM);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setBorder(BorderFactory.createEmptyBorder(6, 0, 20, 0));

        // ── Table ─────────────────────────────────────────────────────────────
        String[] cols = {"ID", "Job Title"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                c.setBackground(isRowSelected(row)
                    ? new Color(212, 175, 55, 45)
                    : (row % 2 == 0 ? INPUT_BG : ROW_ALT));
                c.setForeground(isRowSelected(row) ? GOLD : FG);
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

        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(340);

        JScrollPane sp = new JScrollPane(table);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getViewport().setBackground(INPUT_BG);
        sp.setBorder(new LineBorder(BORDER, 1));
        sp.setPreferredSize(new Dimension(0, 280));
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        sp.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Load jobs immediately on open
        loadJobs();

        // ── Input + Buttons ───────────────────────────────────────────────────
        JTextField jobIdField = styledField();

        // Click row → auto fill job ID
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0)
                jobIdField.setText((String) tableModel.getValueAt(table.getSelectedRow(), 0));
        });

        JButton refreshBtn = ghostButton("↻  Refresh Jobs");
        JButton applyBtn   = goldButton("Apply Now");

        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        refreshBtn.addActionListener(e -> {
            loadJobs();
            jobIdField.setText("");
            table.clearSelection();
            showStatus("Jobs refreshed!", true);
        });

        applyBtn.addActionListener(e -> {
            String input = jobIdField.getText().trim();
            if (input.isEmpty()) {
                showStatus("Please select a job or enter a Job ID.", false);
                return;
            }
            try {
                int id = Integer.parseInt(input);
                String result = as.apply(user.getId(), id);
                switch (result) {
                    case "OK":
                        showStatus("✓  Application submitted successfully!", true);
                        jobIdField.setText("");
                        table.clearSelection();
                        break;
                    case "DUPLICATE":
                        showStatus("You already applied to this job!", false);
                        break;
                    default:
                        showStatus("Something went wrong. Try again.", false);
                }
            } catch (NumberFormatException ex) {
                showStatus("Job ID must be a number.", false);
            }
        });

        // ── Assemble ──────────────────────────────────────────────────────────
        card.add(logo);
        card.add(Box.createVerticalStrut(8));
        card.add(title);
        card.add(sub);
        card.add(refreshBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(sp);
        card.add(Box.createVerticalStrut(16));
        card.add(labelFor("ENTER JOB ID  (or click a row above)"));
        card.add(Box.createVerticalStrut(8));
        card.add(jobIdField);
        card.add(Box.createVerticalStrut(16));
        card.add(applyBtn);
        card.add(statusLabel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 28, 0, 28);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        root.add(card, gbc);
        f.setVisible(true);
    }

    // ── Load jobs from DB into table ──────────────────────────────────────────
    private void loadJobs() {
        System.out.println("Loading jobs...");
        tableModel.setRowCount(0);
        List<Job> jobs = js.getJobs();
        System.out.println("Found " + jobs.size() + " jobs");
        if (jobs.isEmpty()) {
            tableModel.addRow(new String[]{"—", "No jobs available yet"});
            System.out.println("Added 'no jobs' row");
        } else {
            for (Job j : jobs) {
                System.out.println("Job: " + j.getId() + " - " + j.getTitle());
                tableModel.addRow(new String[]{String.valueOf(j.getId()), j.getTitle()});
                System.out.println("Added row to table model, now has " + tableModel.getRowCount() + " rows");
            }
        }
        // Force table repaint
        table.revalidate();
        table.repaint();
        System.out.println("Table refreshed");
    }

    private void showStatus(String msg, boolean ok) {
        statusLabel.setForeground(ok ? SUCCESS : ERROR);
        statusLabel.setText(msg);
        Timer t = new Timer(2500, e -> statusLabel.setText(" "));
        t.setRepeats(false);
        t.start();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private JPanel createCard() {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(BORDER); g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 20, 20));
                g2.dispose();
            }
            @Override public boolean isOpaque() { return false; }
        };
    }

    private JTextField styledField() {
        JTextField fld = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(isFocusOwner() ? GOLD_DIM : BORDER);
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(0.75f, 0.75f, getWidth()-1.5f, getHeight()-1.5f, 10, 10));
                super.paintComponent(g); g2.dispose();
            }
            @Override public boolean isOpaque() { return false; }
        };
        fld.setForeground(FG); fld.setCaretColor(GOLD);
        fld.setFont(new Font("SansSerif", Font.PLAIN, 14));
        fld.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        fld.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        fld.setAlignmentX(Component.CENTER_ALIGNMENT);
        return fld;
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
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setBorderPainted(false); b.setContentAreaFilled(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        return b;
    }

    private JButton ghostButton(String text) {
        JButton b = new JButton(text);
        b.setForeground(FG_DIM);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setBorderPainted(false); b.setContentAreaFilled(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
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
