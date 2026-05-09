package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import model.User;

public class DashboardUI {

    private static final Color BG       = new Color(10,  10,  14);
    private static final Color CARD     = new Color(18,  18,  24);
    private static final Color BORDER   = new Color(40,  40,  55);
    private static final Color GOLD     = new Color(212, 175, 55);
    private static final Color GOLD_DIM = new Color(140, 110, 30);
    private static final Color FG       = new Color(220, 220, 230);
    private static final Color FG_DIM   = new Color(120, 120, 140);
    private static final Color ACCENT   = new Color(60,  130, 200);

    public DashboardUI(User user) {
        JFrame f = new JFrame("Job Portal — Dashboard");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(620, 580);
        f.setLocationRelativeTo(null);
        f.setResizable(true);

        LoginUI.BackgroundPanel root = new LoginUI.BackgroundPanel();
        root.setLayout(new GridBagLayout());
        f.setContentPane(root);

        JPanel card = createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(36, 52, 36, 52));

        // ── Header bar ────────────────────────────────────────────────────────
        JPanel headerBar = new JPanel(new BorderLayout());
        headerBar.setOpaque(false);
        headerBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel brand = new JLabel("⬡  JOB PORTAL");
        brand.setFont(new Font("SansSerif", Font.BOLD, 11));
        brand.setForeground(GOLD);

        boolean isRecruiter = "RECRUITER".equalsIgnoreCase(user.getRole());
        JLabel roleChip = roleChip(isRecruiter ? "RECRUITER" : "CANDIDATE", isRecruiter);
        headerBar.add(brand, BorderLayout.WEST);
        headerBar.add(roleChip, BorderLayout.EAST);

        // ── Avatar + welcome ──────────────────────────────────────────────────
        JPanel avatarRow = new JPanel();
        avatarRow.setOpaque(false);
        avatarRow.setLayout(new BoxLayout(avatarRow, BoxLayout.Y_AXIS));
        avatarRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatarRow.setBorder(BorderFactory.createEmptyBorder(24, 0, 16, 0));

        JPanel avatar = avatarCircle(user.getName());
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcome = new JLabel("Welcome back, " + firstName(user.getName()) + "!");
        welcome.setFont(new Font("Georgia", Font.BOLD, 22));
        welcome.setForeground(FG);
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcome.setBorder(BorderFactory.createEmptyBorder(12, 0, 4, 0));

        JLabel hint = new JLabel(isRecruiter
            ? "Post jobs, view applicants and manage statuses."
            : "Browse jobs, apply and track your applications.");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 13));
        hint.setForeground(FG_DIM);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);

        avatarRow.add(avatar);
        avatarRow.add(welcome);
        avatarRow.add(hint);

        // ── Divider ───────────────────────────────────────────────────────────
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        // ── Action buttons ────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new GridLayout(isRecruiter ? 2 : 2, 1, 0, 12));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(24, 0, 0, 0));

        if (isRecruiter) {
            JButton postBtn = actionButton("📋  Post a Job",
                "Create a new job listing", GOLD);
            postBtn.addActionListener(e -> new PostJobUI(user));

            JButton viewBtn = actionButton("👥  View Applications",
                "See all applicants and update their status", GOLD);
            viewBtn.addActionListener(e -> new ViewApplicationsUI(user));

            btnPanel.add(postBtn);
            btnPanel.add(viewBtn);
        } else {
            JButton applyBtn = actionButton("🔍  Browse & Apply",
                "Find and apply for open positions", GOLD);
            applyBtn.addActionListener(e -> new ApplyJobUI(user));

            JButton myAppsBtn = actionButton("📄  My Applications",
                "Track your application statuses", GOLD);
            myAppsBtn.addActionListener(e -> new MyApplicationsUI(user));

            btnPanel.add(applyBtn);
            btnPanel.add(myAppsBtn);
        }

        // ── Logout button ─────────────────────────────────────────────────────
        JButton logoutBtn = new JButton("⇠  Logout") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(220, 70, 70, 30) : new Color(0,0,0,0));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(getModel().isRollover() ? new Color(220, 70, 70) : new Color(120, 120, 140));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 10, 10));
                g2.dispose(); super.paintComponent(g);
            }
            @Override public boolean isOpaque() { return false; }
        };
        logoutBtn.setForeground(new Color(160, 160, 180));
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        logoutBtn.setBorderPainted(false); logoutBtn.setContentAreaFilled(false); logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.addActionListener(e -> {
            f.dispose();
            new LoginUI();
        });

        card.add(headerBar);
        card.add(avatarRow);
        card.add(sep);
        card.add(btnPanel);
        card.add(Box.createVerticalStrut(16));
        card.add(logoutBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 32, 0, 32);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        root.add(card, gbc);
        f.setVisible(true);
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

    private JPanel avatarCircle(String name) {
        return new JPanel() {
            { setPreferredSize(new Dimension(64, 64)); setMaximumSize(new Dimension(64, 64)); setOpaque(false); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, GOLD_DIM, 64, 64, GOLD));
                g2.fillOval(0, 0, 64, 64);
                g2.setColor(new Color(10, 10, 14));
                g2.setFont(new Font("Georgia", Font.BOLD, 22));
                FontMetrics fm = g2.getFontMetrics();
                String init = name.isEmpty() ? "?" : String.valueOf(name.charAt(0)).toUpperCase();
                g2.drawString(init, (64 - fm.stringWidth(init)) / 2, (64 - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
    }

    private JLabel roleChip(String text, boolean isRecruiter) {
        JLabel l = new JLabel(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = isRecruiter ? new Color(212, 175, 55, 25) : new Color(60, 130, 200, 25);
                Color border = isRecruiter ? GOLD : ACCENT;
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(border); g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 20, 20));
                g2.dispose(); super.paintComponent(g);
            }
            @Override public boolean isOpaque() { return false; }
        };
        l.setFont(new Font("SansSerif", Font.BOLD, 10));
        l.setForeground(isRecruiter ? GOLD : ACCENT);
        l.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        return l;
    }

    private JButton actionButton(String title, String subtitle, Color accent) {
        JButton b = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg     = getModel().isRollover() ? new Color(212, 175, 55, 20) : new Color(24, 24, 32);
                Color border = getModel().isRollover() ? GOLD : BORDER;
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(border);
                g2.setStroke(new BasicStroke(getModel().isRollover() ? 1.5f : 1f));
                g2.draw(new RoundRectangle2D.Float(0.75f, 0.75f, getWidth()-1.5f, getHeight()-1.5f, 12, 12));
                g2.setFont(new Font("SansSerif", Font.BOLD, 15));
                g2.setColor(FG);
                g2.drawString(title, 20, getHeight() / 2 - 4);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                g2.setColor(FG_DIM);
                g2.drawString(subtitle, 20, getHeight() / 2 + 14);
                g2.setFont(new Font("SansSerif", Font.BOLD, 18));
                g2.setColor(accent);
                g2.drawString("→", getWidth() - 36, getHeight() / 2 + 6);
                g2.dispose();
            }
            @Override public boolean isOpaque() { return false; }
        };
        b.setBorderPainted(false); b.setContentAreaFilled(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(0, 72));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        return b;
    }

    private String firstName(String full) {
        if (full == null || full.isEmpty()) return "User";
        return full.split("\\s+")[0];
    }
}
