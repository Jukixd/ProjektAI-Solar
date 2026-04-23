import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                String cestaKModelu = "solarni_model.onnx";
                SolarModel model = new SolarModel(cestaKModelu);
                SolarLogika logika = new SolarLogika(model);

                SolarGUI gui = new SolarGUI(logika);
                gui.zobraz();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Nepodařilo se načíst AI model.\nDetail: " + e.getMessage(),
                        "Fatální Chyba",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}