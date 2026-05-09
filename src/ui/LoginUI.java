package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import model.User;
import service.UserService;

public class LoginUI {

    // ── Palette ──────────────────────────────────────────────────────────────
    private static final Color BG        = new Color(10,  10,  14);
    private static final Color CARD      = new Color(18,  18,  24);
    private static final Color BORDER    = new Color(40,  40,  55);
    private static final Color GOLD      = new Color(10, 10, 14);
    private static final Color GOLD_DIM  = new Color(140, 110, 30);
    private static final Color FG        = new Color(220, 220, 230);
    private static final Color FG_DIM    = new Color(120, 120, 140);
    private static final Color INPUT_BG  = new Color(24,  24,  32);
    private static final Color ERROR     = new Color(220, 70,  70);

    public LoginUI() {
        JFrame f = new JFrame("Job Portal — Sign In");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(580, 640);
        f.setLocationRelativeTo(null);
        f.setUndecorated(false);
        f.setResizable(true);

        // Root panel with animated background
        BackgroundPanel root = new BackgroundPanel();
        root.setLayout(new GridBagLayout());
        f.setContentPane(root);

        // Card
        JPanel card = createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(40, 44, 40, 44));

        // Logo / brand
        JLabel logo = new JLabel("⬡  JOB PORTAL");
        logo.setFont(loadFont(13f).deriveFont(Font.BOLD));
        logo.setForeground(GOLD);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JLabel title = new JLabel("Welcome Back");
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(FG);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Sign in to your account");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(FG_DIM);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setBorder(BorderFactory.createEmptyBorder(6, 0, 28, 0));

        JTextField emailField = styledField("Email address");
        JPasswordField passField  = styledPass("Password");

        JButton loginBtn = goldButton("Sign In");
        JButton regBtn   = ghostButton("Create an account →");

        JLabel errLabel = new JLabel(" ");
        errLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        errLabel.setForeground(ERROR);
        errLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        UserService service = new UserService();

        loginBtn.addActionListener(e -> {
            String em = emailField.getText().trim();
            String pw = new String(passField.getPassword());
            if (em.isEmpty() || pw.isEmpty()) {
                errLabel.setText("Please fill in all fields.");
                return;
            }
            User u = service.login(em, pw);
            if (u != null) {
                f.dispose();
                new DashboardUI(u);
            } else {
                errLabel.setText("Invalid email or password.");
                shake(card);
            }
        });

        regBtn.addActionListener(e -> { f.dispose(); new RegisterUI(); });

        card.add(logo);
        card.add(title);
        card.add(sub);
        card.add(labelFor("EMAIL"));
        card.add(Box.createVerticalStrut(6));
        card.add(emailField);
        card.add(Box.createVerticalStrut(16));
        card.add(labelFor("PASSWORD"));
        card.add(Box.createVerticalStrut(6));
        card.add(passField);
        card.add(Box.createVerticalStrut(24));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(regBtn);
        card.add(errLabel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 32, 0, 32);
        gbc.fill   = GridBagConstraints.BOTH;
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

    private JTextField styledField(String placeholder) {
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
        f.setForeground(FG);
        f.setCaretColor(GOLD);
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        f.setAlignmentX(Component.CENTER_ALIGNMENT);
        return f;
    }

    private JPasswordField styledPass(String placeholder) {
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
        f.setForeground(FG);
        f.setCaretColor(GOLD);
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        f.setAlignmentX(Component.CENTER_ALIGNMENT);
        return f;
    }

    private JButton goldButton(String text) {
        JButton b = new JButton(text) {
            private float hover = 0f;
            { 
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { animateHover(true); }
                    public void mouseExited(MouseEvent e)  { animateHover(false); }
                    private void animateHover(boolean in) {
                        Timer t = new Timer(10, null);
                        t.addActionListener(ev -> {
                            hover += in ? 0.08f : -0.08f;
                            hover  = Math.max(0f, Math.min(1f, hover));
                            repaint();
                            if ((in && hover>=1f) || (!in && hover<=0f)) ((Timer)ev.getSource()).stop();
                        });
                        t.start();
                    }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c1 = blend(new Color(180,140,20), GOLD, hover);
                Color c2 = blend(new Color(140,100,10), new Color(230,190,60), hover);
                GradientPaint gp = new GradientPaint(0,0,c1,getWidth(),getHeight(),c2);
                g2.setPaint(gp);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),10,10));
                g2.dispose();
                super.paintComponent(g);
            }
            @Override public boolean isOpaque() { return false; }
        };
        b.setForeground(new Color(10,10,14));
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        return b;
    }

    private JButton ghostButton(String text) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(212,175,55,20));
                    g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),10,10));
                }
                g2.dispose();
                super.paintComponent(g);
            }
            @Override public boolean isOpaque() { return false; }
        };
        b.setForeground(FG_DIM);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
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

    private Font loadFont(float size) {
        return new Font("SansSerif", Font.BOLD, (int) size);
    }

    private Color blend(Color a, Color b, float t) {
        return new Color(
            (int)(a.getRed()   + t*(b.getRed()   - a.getRed())),
            (int)(a.getGreen() + t*(b.getGreen() - a.getGreen())),
            (int)(a.getBlue()  + t*(b.getBlue()  - a.getBlue()))
        );
    }

    private void shake(JComponent c) {
        Point origin = c.getLocation();
        int[] dx = {-8, 8, -6, 6, -3, 3, 0};
        Timer t = new Timer(35, null);
        int[] i = {0};
        t.addActionListener(e -> {
            if (i[0] < dx.length) {
                c.setLocation(origin.x + dx[i[0]++], origin.y);
            } else {
                c.setLocation(origin);
                t.stop();
            }
        });
        t.start();
    }

    // Animated starfield background
    static class BackgroundPanel extends JPanel {
        private final float[][] stars;
        private final Timer animator;
        BackgroundPanel() {
            setBackground(BG);
            stars = new float[120][3];
            Random rnd = new Random(42);
            for (float[] s : stars) { s[0]=rnd.nextFloat(); s[1]=rnd.nextFloat(); s[2]=rnd.nextFloat()*1.5f+0.5f; }
            animator = new Timer(60, e -> repaint());
            animator.start();
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            // subtle grid
            g2.setColor(new Color(255,255,255,4));
            for (int x=0; x<getWidth(); x+=40)  g2.drawLine(x,0,x,getHeight());
            for (int y=0; y<getHeight(); y+=40) g2.drawLine(0,y,getWidth(),y);
            // stars
            long ms = System.currentTimeMillis();
            for (float[] s : stars) {
                double pulse = 0.5 + 0.5*Math.sin(ms/1000.0 + s[2]*6);
                int alpha = (int)(40 + 120*pulse);
                g2.setColor(new Color(212,175,55,alpha));
                int x = (int)(s[0]*getWidth()), y=(int)(s[1]*getHeight());
                float r = s[2]*1.2f;
                g2.fill(new Ellipse2D.Float(x-r, y-r, r*2, r*2));
            }
            // gold glow top-centre
            RadialGradientPaint rg = new RadialGradientPaint(
                new Point2D.Float(getWidth()/2f, 0),
                getWidth()*0.6f,
                new float[]{0f,1f},
                new Color[]{new Color(212,175,55,40), new Color(0,0,0,0)}
            );
            g2.setPaint(rg); g2.fillRect(0,0,getWidth(),getHeight());
            g2.dispose();
        }
        private static final java.util.Random Random = null;
        static { } // java.util.Random inline below
        static class Random extends java.util.Random { Random(long s){super(s);} }
    }
}
