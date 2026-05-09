package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import model.User;
import service.UserService;

public class RegisterUI {

    private static final Color BG       = new Color(10,  10,  14);
    private static final Color CARD     = new Color(18,  18,  24);
    private static final Color BORDER   = new Color(40,  40,  55);
    private static final Color GOLD     = new Color(212, 175, 55);
    private static final Color GOLD_DIM = new Color(140, 110, 30);
    private static final Color FG       = new Color(220, 220, 230);
    private static final Color FG_DIM   = new Color(120, 120, 140);
    private static final Color INPUT_BG = new Color(24,  24,  32);
    private static final Color ERROR    = new Color(220, 70,  70);

    public RegisterUI() {
        JFrame f = new JFrame("Job Portal — Register");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setSize(580, 720);
        f.setLocationRelativeTo(null);
        f.setResizable(true);

        LoginUI.BackgroundPanel root = new LoginUI.BackgroundPanel();
        root.setLayout(new GridBagLayout());
        f.setContentPane(root);

        JPanel card = createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(36, 44, 36, 44));

        JLabel logo = new JLabel("⬡  JOB PORTAL");
        logo.setFont(new Font("SansSerif", Font.BOLD, 12));
        logo.setForeground(GOLD);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setBorder(BorderFactory.createEmptyBorder(0,0,8,0));

        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(FG);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Join the portal today");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(FG_DIM);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setBorder(BorderFactory.createEmptyBorder(6,0,28,0));

        JTextField nameField  = styledField();
        JTextField emailField = styledField();
        JPasswordField passField  = styledPass();

        // Role selector
        JPanel roleRow = new JPanel(new GridLayout(1,2,8,0));
        roleRow.setOpaque(false);
        roleRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        roleRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        JToggleButton btnCandidate = roleToggle("CANDIDATE", true);
        JToggleButton btnRecruiter = roleToggle("RECRUITER", false);
        ButtonGroup bg = new ButtonGroup();
        bg.add(btnCandidate); bg.add(btnRecruiter);
        roleRow.add(btnCandidate); roleRow.add(btnRecruiter);

        JButton regBtn  = goldButton("Create Account");
        JButton backBtn = ghostButton("← Back to Sign In");

        JLabel errLabel = new JLabel(" ");
        errLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        errLabel.setForeground(ERROR);
        errLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        UserService service = new UserService();

        regBtn.addActionListener(e -> {
            String n  = nameField.getText().trim();
            String em = emailField.getText().trim();
            String pw = new String(passField.getPassword());
            if (n.isEmpty() || em.isEmpty() || pw.isEmpty()) {
                errLabel.setText("All fields are required!"); return;
            }
            String role = btnRecruiter.isSelected() ? "RECRUITER" : "CANDIDATE";
            User u = new User(n, em, pw, role);
            service.register(u);
            JOptionPane.showMessageDialog(f, "Account created! Welcome, " + n + ".");
            f.dispose(); new LoginUI();
        });
        backBtn.addActionListener(e -> { f.dispose(); new LoginUI(); });

        card.add(logo); card.add(title); card.add(sub);
        card.add(labelFor("FULL NAME")); card.add(Box.createVerticalStrut(6));
        card.add(nameField); card.add(Box.createVerticalStrut(14));
        card.add(labelFor("EMAIL ADDRESS")); card.add(Box.createVerticalStrut(6));
        card.add(emailField); card.add(Box.createVerticalStrut(14));
        card.add(labelFor("PASSWORD")); card.add(Box.createVerticalStrut(6));
        card.add(passField); card.add(Box.createVerticalStrut(14));
        card.add(labelFor("I AM A")); card.add(Box.createVerticalStrut(8));
        card.add(roleRow); card.add(Box.createVerticalStrut(24));
        card.add(regBtn); card.add(Box.createVerticalStrut(10));
        card.add(backBtn); card.add(errLabel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0,32,0,32);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
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
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f,0.5f,getWidth()-1,getHeight()-1,20,20));
                g2.dispose();
            }
            @Override public boolean isOpaque() { return false; }
        };
    }

    private JTextField styledField() {
        JTextField f = new JTextField() {
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
        f.setForeground(FG); f.setCaretColor(GOLD);
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createEmptyBorder(10,14,10,14));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        f.setAlignmentX(Component.CENTER_ALIGNMENT);
        return f;
    }

    private JPasswordField styledPass() {
        JPasswordField f = new JPasswordField() {
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
        f.setForeground(FG); f.setCaretColor(GOLD);
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createEmptyBorder(10,14,10,14));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        f.setAlignmentX(Component.CENTER_ALIGNMENT);
        return f;
    }

    private JToggleButton roleToggle(String text, boolean selected) {
        JToggleButton b = new JToggleButton(text, selected) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isSelected()) {
                    g2.setColor(new Color(212,175,55,30));
                    g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),10,10));
                    g2.setColor(GOLD);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.draw(new RoundRectangle2D.Float(0.75f,0.75f,getWidth()-1.5f,getHeight()-1.5f,10,10));
                } else {
                    g2.setColor(INPUT_BG);
                    g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),10,10));
                    g2.setColor(BORDER);
                    g2.setStroke(new BasicStroke(1f));
                    g2.draw(new RoundRectangle2D.Float(0.5f,0.5f,getWidth()-1,getHeight()-1,10,10));
                }
                g2.dispose();
                super.paintComponent(g);
            }
            @Override public boolean isOpaque() { return false; }
        };
        b.setForeground(selected ? GOLD : FG_DIM);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setBorderPainted(false); b.setContentAreaFilled(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addItemListener(e -> b.setForeground(b.isSelected() ? GOLD : FG_DIM));
        return b;
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

    private JButton ghostButton(String text) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
            @Override public boolean isOpaque() { return false; }
        };
        b.setForeground(FG_DIM); b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setBorderPainted(false); b.setContentAreaFilled(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
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
