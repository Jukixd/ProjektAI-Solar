import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SolarGUI {
    private JFrame frame;
    private JTextField txtTeplota, txtVlhkost, txtTlak, txtOblacnost, txtHodina, txtMesic;
    private JLabel lblVysledek;


    public SolarGUI(SolarLogika logika) {


        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("Solární Prediktor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 550);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font boldFont = new Font("Segoe UI", Font.BOLD, 14);
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        formPanel.add(createLabel("Teplota (°C):", mainFont));
        txtTeplota = createTextField(mainFont);
        formPanel.add(txtTeplota);

        formPanel.add(createLabel("Vlhkost (%):", mainFont));
        txtVlhkost = createTextField(mainFont);
        formPanel.add(txtVlhkost);

        formPanel.add(createLabel("Tlak (hPa):", mainFont));
        txtTlak = createTextField(mainFont);
        formPanel.add(txtTlak);

        formPanel.add(createLabel("Oblačnost (%):", mainFont));
        txtOblacnost = createTextField(mainFont);
        formPanel.add(txtOblacnost);

        formPanel.add(createLabel("Hodina (0-23):", mainFont));
        txtHodina = createTextField(mainFont);
        formPanel.add(txtHodina);

        formPanel.add(createLabel("Měsíc (1-12):", mainFont));
        txtMesic = createTextField(mainFont);
        formPanel.add(txtMesic);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 15));

        JButton btnPredikuj = new JButton("Vypočítat výkon");
        btnPredikuj.setFont(boldFont);
        btnPredikuj.setBackground(new Color(0, 120, 215));
        btnPredikuj.setForeground(Color.BLACK);
        btnPredikuj.setFocusPainted(false);
        btnPredikuj.setCursor(new Cursor(Cursor.HAND_CURSOR));

        lblVysledek = new JLabel("Čekám na zadání dat...", SwingConstants.CENTER);
        lblVysledek.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblVysledek.setBorder(new EmptyBorder(10, 0, 10, 0));

        bottomPanel.add(btnPredikuj, BorderLayout.NORTH);
        bottomPanel.add(lblVysledek, BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        frame.add(mainPanel);

        btnPredikuj.addActionListener(e -> {
            VysledekZpracovani vysledek = logika.zpracujVstup(
                    txtTeplota.getText(),
                    txtVlhkost.getText(),
                    txtTlak.getText(),
                    txtOblacnost.getText(),
                    txtHodina.getText(),
                    txtMesic.getText()
            );

            if (!vysledek.jeUspesny) {
                JOptionPane.showMessageDialog(frame, vysledek.zprava, "Chyba zadání", JOptionPane.WARNING_MESSAGE);
            } else {
                String barva = vysledek.zprava.contains("Vysoká") ? "green" : (vysledek.zprava.contains("Střední") ? "orange" : "red");
                lblVysledek.setText("<html><div style='text-align: center;'>Predikce: <span style='color:" + barva + ";'>" + vysledek.zprava + "</span></div></html>");
            }
        });
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    private JTextField createTextField(Font font) {
        JTextField textField = new JTextField();
        textField.setFont(font);
        return textField;
    }

    public void zobraz() {
        if (frame != null) {
            frame.setVisible(true);
        } else {
            System.err.println("Kritická chyba GUI: Okno (frame) nebylo vytvořeno!");
        }
    }
}