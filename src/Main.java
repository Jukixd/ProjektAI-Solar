public class Main {
    public static void main(String[] args) {
        SolarModel mujModel = new SolarModel("solarni_model.onnx");
        new SolarGUI(mujModel);
    }
}