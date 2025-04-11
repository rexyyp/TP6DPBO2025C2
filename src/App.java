import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // Membuat jendela utama aplikasi dengan judul "Flappy Bird"
        JFrame frame = new JFrame("Flappy Bird");

        // Mengatur agar aplikasi berhenti saat jendela ditutup
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Mengatur ukuran jendela menjadi 360x640 piksel
        frame.setSize(360, 640);

        // Menempatkan jendela di tengah layar
        frame.setLocationRelativeTo(null);

        // Menonaktifkan kemampuan untuk mengubah ukuran jendela
        frame.setResizable(false);

        MainMenu menu = new MainMenu(frame);
        frame.setContentPane(menu);
        frame.pack();
        frame.setVisible(true);

        // Membuat Objek Jpanel
        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack();
        flappyBird.requestFocus();
        frame.setVisible(true);
    }
}
