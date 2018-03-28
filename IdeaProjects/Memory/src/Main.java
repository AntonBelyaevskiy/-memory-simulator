import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Field field = new Field("Тренажер | тренировка памяти");
            field.setVisible(true);
        });
    }

}
