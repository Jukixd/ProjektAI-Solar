import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OnnxTensor;
import java.util.Collections;



public class SolarModel {

    private String cestaKModelu;
    public SolarModel(String cestaKModelu) {
        this.cestaKModelu = cestaKModelu;
    }

    public String predikuj(float teplota, float vlhkost, float tlak, float oblacnost, float hodina) {
        try {
            OrtEnvironment env = OrtEnvironment.getEnvironment();
            OrtSession.SessionOptions opts = new OrtSession.SessionOptions();
            OrtSession session = env.createSession(this.cestaKModelu, opts);

            float[][] vstupy = new float[][]{{teplota, vlhkost, tlak, oblacnost, hodina}};
            OnnxTensor tensor = OnnxTensor.createTensor(env, vstupy);

            OrtSession.Result vysledek = session.run(Collections.singletonMap("float_input", tensor));

            long[] labely = (long[]) vysledek.get(0).getValue();
            long kategorie = labely[0];

            tensor.close();
            session.close();
            env.close();

            if (kategorie == 0) return "Nízká (Zataženo/Tma) - Nedoporučuji zapínat spotřebiče.";
            if (kategorie == 1) return "Střední (Polojasno) - Panely vyrábí, lze nabíjet baterie.";
            if (kategorie == 2) return "Vysoká (Jasno) - Ideální podmínky pro max. spotřebu!";

        } catch (Exception e) {
            return "Chyba modelu: " + e.getMessage();
        }
        return "Neznámý výsledek";
    }
}