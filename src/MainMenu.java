import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {

    JButton startButton;
    JButton exitButton;
    JLabel titleLabel;
    Image background;

    public MainMenu(JFrame frame) {
        setPreferredSize(new Dimension(360, 640));
        setLayout(null); // custom layout
        setFocusable(true);

        // Load background image
        background = new ImageIcon(getClass().getResource("assets/background.png")).getImage();

        // Title Label
        titleLabel = new JLabel("FLAPPY BIRD", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
        titleLabel.setBounds(30, 100, 300, 50);
        add(titleLabel);

        // Start Button
        startButton = new JButton("START");
        startButton.setBounds(100, 250, 160, 40);
        startButton.setFocusPainted(false);
        startButton.setFont(new Font("Comic Sans Ms", Font.BOLD, 24));
        startButton.setForeground(Color.WHITE);
        startButton.setContentAreaFilled(false); // menghilangkan background
        startButton.setBorderPainted(false);     // menghilangkan border
        startButton.setOpaque(false);            // transparan
        add(startButton);

        // Exit Button
        exitButton = new JButton("EXIT");
        exitButton.setBounds(100, 310, 160, 40);  // lebar ditambah
        exitButton.setFocusPainted(false);
        exitButton.setFont(new Font("Comic Sans Ms", Font.BOLD, 24));
        exitButton.setForeground(Color.WHITE);
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.setOpaque(false);
        add(exitButton);


        // Start Button Action
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FlappyBird gamePanel = new FlappyBird();
                frame.setContentPane(gamePanel);
                frame.revalidate();
                gamePanel.requestFocus();
            }
        });

        // Exit Button Action
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Gambar background
        g.drawImage(background, 0, 0, 360, 640, this);
    }
}
