import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.sound.sampled.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {

    int frameWidth = 360; // lebar frame
    int frameHeight = 640; // tinggi frame

    // image
    Image backgroundImage;
    Image birdImage;
    Image lowerPipeImage;
    Image upperImage;

    // player
    int playerStartPostX = frameWidth / 8;
    int playerStartPostY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;
    Player player;

    // pipe
    int pipeStartPostX = frameWidth;
    int pipeStartPostY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    ArrayList<Pipe> pipes;

    // Timer
    Timer gameLoop;
    Timer pipesCooldown;

    int gravity = 1;

    boolean gameOver = false;
    int score = 0;
    JLabel scoreLabel;
    JLabel gameOverLabel;
    JLabel restartLabel;




    // construktor
    public FlappyBird() {
        // Mengatur ukuran panel menjadi 360x640 piksel
        setPreferredSize(new Dimension(360, 640));

        // setBackground(Color.BLUE);

        // Menambahkan KeyListener untuk mendeteksi penekanan tombol
        setFocusable(true);
        addKeyListener(this);

        // load Image
        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();

        player = new Player(playerStartPostX, playerStartPostY, playerWidth, playerHeight, birdImage);
        pipes = new ArrayList<Pipe>();

         pipesCooldown = new Timer(2500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("pipa");
                placesPipes();
            }
        });
        pipesCooldown.start();

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(Color.WHITE);
        this.setLayout(null);
        scoreLabel.setBounds(10, 10, 100, 30);
        this.add(scoreLabel);

        gameOverLabel = new JLabel("GAME OVER", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 36));
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setBounds(30, 250, 300, 50);
        gameOverLabel.setVisible(false); // baru muncul saat gameOver
        add(gameOverLabel);

        restartLabel = new JLabel("Press 'R' to Restart", SwingConstants.CENTER);
        restartLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        restartLabel.setForeground(Color.RED);
        restartLabel.setBounds(30, 300, 300, 30);
        restartLabel.setVisible(false);
        add(restartLabel);




    }

    public void draw(Graphics g){
        g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, null);
        g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
        }
    }

    public void move() {

        if (gameOver) {
            return;
        }

        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        int i = 0;
        while (i < pipes.size()) {
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipe.getVelocityY());

            // Cek tabrakan
            Rectangle playerRect = new Rectangle(player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());
            Rectangle pipeRect = new Rectangle(pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight());
            if (playerRect.intersects(pipeRect) || player.getPosY() + player.getHeight() > frameHeight) {
                gameOver = true;
                gameOverLabel.setVisible(true);
                restartLabel.setVisible(true);

                gameLoop.stop();
                pipesCooldown.stop();
                break; // langsung keluar dari while
            }

            // Tambah skor saat melewati pipa atas (1 kali saja)
            if (!pipe.getScored() && pipe.getImage() == upperImage && pipe.getPosX() + pipe.getWidth() < player.getPosX()) {
                score++;
                pipe.setScored(true);
                scoreLabel.setText("Score: " + score);
            }

            i++;
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

        // Tambahkan overlay hitam transparan jika game over
        if (gameOver) {
            Graphics2D g2d = (Graphics2D) g;
            Color transparentBlack = new Color(0, 0, 0, 150); // alpha 150 dari 255
            g2d.setColor(transparentBlack);
            g2d.fillRect(0, 0, frameWidth, frameHeight);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Tidak digunakan
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.setVelocityY(-10);
            playSound("jump.wav");
        }

        if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            restartGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void playSound(String soundFileName) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("Assets/" + soundFileName));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void placesPipes() {
        int randomPosY = (int) (pipeStartPostY - pipeHeight/4 - Math.random() * (pipeHeight/2));
        int openingSpace = frameHeight/4;

        Pipe upperPipe = new Pipe(pipeStartPostX, randomPosY, pipeWidth, pipeHeight, upperImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPostX, (randomPosY + openingSpace + pipeHeight), pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    public void restartGame() {
        gameOver = false;
        restartLabel.setVisible(false);
        gameOverLabel.setVisible(false);
        score = 0;
        scoreLabel.setText("Score: 0");

        player.setPosY(playerStartPostY);
        player.setVelocityY(0);

        pipes.clear();

        gameLoop.start();
        pipesCooldown.start();
    }
}
