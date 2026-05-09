package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import model.User;
import service.JobService;

public class PostJobUI {

    private static final Color BG       = new Color(10,  10,  14);
    private static final Color CARD     = new Color(18,  18,  24);
    private static final Color BORDER   = new Color(40,  40,  55);
    private static final Color GOLD     = new Color(212, 175, 55);
    private static final Color GOLD_DIM = new Color(140, 110, 30);
    private static final Color FG       = new Color(220, 220, 230);
    private static final Color FG_DIM   = new Color(120, 120, 140);
    private static final Color INPUT_BG = new Color(24,  24,  32);
    private static final Color SUCCESS  = new Color(80,  200, 120);

    public PostJobUI(User user) {
        JFrame f = new JFrame("Job Portal — Post a Job");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setSize(580, 520);
        f.setLocationRelativeTo(null);
        f.setResizable(true);

        LoginUI.BackgroundPanel root = new LoginUI.BackgroundPanel();
        root.setLayout(new GridBagLayout());
        f.setContentPane(root);

        JPanel card = createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(40, 44, 40, 44));

        JLabel logo = new JLabel("⬡  JOB PORTAL");
        logo.setFont(new Font("SansSerif", Font.BOLD, 11));
        logo.setForeground(GOLD);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Post a New Job");
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(FG);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Create a listing to attract candidates");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(FG_DIM);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setBorder(BorderFactory.createEmptyBorder(6,0,28,0));

        JTextField titleField = styledField();
        JButton postBtn = goldButton("Publish Job Listing");

        JLabel statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setForeground(SUCCESS);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8,0,0,0));

        JobService service = new JobService();

        postBtn.addActionListener(e -> {
            String t = titleField.getText().trim();
            if (t.isEmpty()) {
                statusLabel.setForeground(new Color(220,70,70));
                statusLabel.setText("Job title cannot be empty!"); return;
            }
            service.postJob(user.getId(), t);
            statusLabel.setForeground(SUCCESS);
            statusLabel.setText("✓  Job published successfully!");
            titleField.setText("");
            // flash effect
            Timer flash = new Timer(500, ev -> statusLabel.setText(" "));
            flash.setRepeats(false); flash.start();
        });

        card.add(logo);
        card.add(Box.createVerticalStrut(8));
        card.add(title);
        card.add(sub);
        card.add(labelFor("JOB TITLE"));
        card.add(Box.createVerticalStrut(8));
        card.add(titleField);
        card.add(Box.createVerticalStrut(24));
        card.add(postBtn);
        card.add(statusLabel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0,32,0,32);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        root.add(card, gbc);
        f.setVisible(true);
    }

    private JPanel createCard() {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),20,20));
                g2.setColor(BORDER); g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f,0.5f,getWidth()-1,getHeight()-1,20,20));
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
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),10,10));
                g2.setColor(isFocusOwner() ? GOLD_DIM : BORDER);
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(0.75f,0.75f,getWidth()-1.5f,getHeight()-1.5f,10,10));
                super.paintComponent(g);
                g2.dispose();
            }
            @Override public boolean isOpaque() { return false; }
        };
        fld.setForeground(FG); fld.setCaretColor(GOLD);
        fld.setFont(new Font("SansSerif", Font.PLAIN, 14));
        fld.setBorder(BorderFactory.createEmptyBorder(10,14,10,14));
        fld.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        fld.setAlignmentX(Component.CENTER_ALIGNMENT);
        return fld;
    }

    private JButton goldButton(String text) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c1 = getModel().isRollover() ? new Color(230,190,60) : GOLD;
                Color c2 = getModel().isRollover() ? new Color(180,140,20) : GOLD_DIM;
                g2.setPaint(new GradientPaint(0,0,c1,getWidth(),getHeight(),c2));
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),10,10));
                g2.dispose(); super.paintComponent(g);
            }
            @Override public boolean isOpaque() { return false; }
        };
        b.setForeground(new Color(10,10,14)); b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setBorderPainted(false); b.setContentAreaFilled(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
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
