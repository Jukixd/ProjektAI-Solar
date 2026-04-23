public class SolarLogika {
    private SolarModel model;

    public SolarLogika(SolarModel model) {
        this.model = model;
    }

    public VysledekZpracovani zpracujVstup(String temp, String hum, String press, String cloud, String hour, String month) {
        try {

            float t = Float.parseFloat(temp);
            float v = Float.parseFloat(hum);
            float tl = Float.parseFloat(press);
            float o = Float.parseFloat(cloud);
            int h = Integer.parseInt(hour);
            int m = Integer.parseInt(month);

            String chyba = "";
            if (t < -50 || t > 60) chyba += "- Teplota: -50 až 60 °C.\n";
            if (v < 0 || v > 100) chyba += "- Vlhkost: 0 až 100 %.\n";
            if (tl < 800 || tl > 1100) chyba += "- Tlak: 800 až 1100 hPa.\n";
            if (o < 0 || o > 100) chyba += "- Oblačnost: 0 až 100 %.\n";
            if (h < 0 || h > 23) chyba += "- Hodina: 0 až 23.\n";
            if (m < 1 || m > 12) chyba += "- Měsíc: 1 až 12.\n";

            if (!chyba.isEmpty()) {
                return new VysledekZpracovani(false, "Opravte prosím tyto údaje:\n" + chyba);
            }

            String predikce = model.predikuj(t, v, tl, o, h, m);
            return new VysledekZpracovani(true, predikce);

        } catch (NumberFormatException e) {
            return new VysledekZpracovani(false, "Vyplňte všechna pole platnými čísly!");
        } catch (Exception e) {
            return new VysledekZpracovani(false, "Kritická chyba modelu: " + e.getMessage());
        }
    }
}