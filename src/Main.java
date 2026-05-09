import ui.LoginUI;

public class Main {
    public static void main(String[] args) {
        // Launch on the Event Dispatch Thread (Swing best practice)
        javax.swing.SwingUtilities.invokeLater(() -> new LoginUI());
    }
}
