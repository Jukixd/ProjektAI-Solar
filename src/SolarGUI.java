import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SolarGUI {

    private SolarModel model;
    private JFrame frame;

    public SolarGUI(SolarModel model) {
        this.model = model;
        vytvorOkno();
    }

    private void vytvorOkno() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        frame = new JFrame("SolarSense AI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 450);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        JPanel mainPanel = new JPanel(new GridLayout(6, 2, 15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        Font labelFont = new Font("SansSerif", Font.BOLD, 12);
        Font inputFont = new Font("SansSerif", Font.PLAIN, 14);

        JTextField txtTeplota = vytvorPole("", inputFont);
        JTextField txtVlhkost = vytvorPole("", inputFont);
        JTextField txtTlak = vytvorPole("", inputFont);
        JTextField txtOblacnost = vytvorPole("", inputFont);
        JTextField txtHodina = vytvorPole("", inputFont);
        JTextField txtMesic = vytvorPole("", inputFont);

        mainPanel.add(vytvorLabel("Teplota (°C):", labelFont)); mainPanel.add(txtTeplota);
        mainPanel.add(vytvorLabel("Vlhkost (%):", labelFont)); mainPanel.add(txtVlhkost);
        mainPanel.add(vytvorLabel("Tlak (hPa):", labelFont)); mainPanel.add(txtTlak);
        mainPanel.add(vytvorLabel("Oblačnost (%):", labelFont)); mainPanel.add(txtOblacnost);
        mainPanel.add(vytvorLabel("Hodina (0-23):", labelFont)); mainPanel.add(txtHodina);
        mainPanel.add(vytvorLabel("Měsíc (0-12):", labelFont)); mainPanel.add(txtMesic);

        JButton btnPredikuj = new JButton("VYPOČÍTAT VÝKON");
        btnPredikuj.setBackground(new Color(0, 102, 204));
        btnPredikuj.setForeground(Color.BLACK);
        btnPredikuj.setFocusPainted(false);
        btnPredikuj.setFont(new Font("SansSerif", Font.BOLD, 13));

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(new Color(245, 245, 245));
        JLabel lblVysledek = new JLabel("Připraven k výpočtu", SwingConstants.CENTER);
        lblVysledek.setBorder(new EmptyBorder(15, 10, 15, 10));
        lblVysledek.setFont(new Font("SansSerif", Font.ITALIC, 13));
        resultPanel.add(lblVysledek, BorderLayout.CENTER);

        btnPredikuj.addActionListener(e -> {
            try {
                float t = Float.parseFloat(txtTeplota.getText());
                float v = Float.parseFloat(txtVlhkost.getText());
                float tl = Float.parseFloat(txtTlak.getText());
                float o = Float.parseFloat(txtOblacnost.getText());
                int h = Integer.parseInt(txtHodina.getText());
                int m = Integer.parseInt(txtMesic.getText());
                String chyba = "";

                if (t < -50 || t > 60) chyba += "- Teplota musí být mezi -50 a 60 °C.\n";
                if (v < 0 || v > 100) chyba += "- Vlhkost musí být mezi 0 a 100 %.\n";
                if (tl < 800 || tl > 1100) chyba += "- Tlak musí být mezi 800 a 1100 hPa.\n";
                if (o < 0 || o > 100) chyba += "- Oblačnost musí být mezi 0 a 100 %.\n";
                if (h < 0 || h > 23) chyba += "- Hodina musí být mezi 0 a 23.\n";
                if (m < 1 || m > 12) chyba += "- Měsíc: 1 až 12.\n";
                if (!chyba.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Zadali jste hodnoty mimo reálný rozsah:\n\n" + chyba,
                            "Neplatná data", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String vysledek = model.predikuj(t, v, tl, o, h,m);
                String barva = vysledek.contains("Vysoká") ? "green" : (vysledek.contains("Střední") ? "orange" : "red");
                lblVysledek.setText("<html><div style='text-align: center;'>Výkon: <b style='color:" + barva + ";'>" + vysledek + "</b></div></html>");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "všechna pole musí obsahovat platná čísla!", "Špatný formát", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Kritická chyba: " + ex.getMessage(), "Chyba", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(btnPredikuj, BorderLayout.NORTH);
        frame.add(resultPanel, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JLabel vytvorLabel(String text, Font font) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        return l;
    }

    private JTextField vytvorPole(String text, Font font) {
        JTextField f = new JTextField(text);
        f.setFont(font);
        f.setHorizontalAlignment(JTextField.CENTER);
        return f;
    }
}