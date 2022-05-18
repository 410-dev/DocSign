package docsign.ui;

import javax.swing.JFrame;

import docsign.ui.guiscenes.Login;
import docsign.ui.guiscenes.Scene;

public class GraphicUI {

    public static JFrame frame;

    public static void run() {
        frame = new JFrame("DocSign II");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 160);
        frame.setResizable(false);

        loadScene(new Login());

        frame.setVisible(true);
    }

    public static void loadScene(Scene scene) {
        frame.setContentPane(scene.getScene());
        frame.revalidate();
        frame.repaint();
    }

}
