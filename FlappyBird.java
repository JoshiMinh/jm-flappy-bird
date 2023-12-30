import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList; 
import java.util.List;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private int birdX, birdY, birdVelocity, rotationAngle;
    private List<Rectangle> obstacles;
    private Random random;
    private float score, gameVol = 0.8f;
    private boolean endGame = false, startGame = false, Down = true;

    private int Tick, space, distance, velocity, gravity;
    private int Diff, DefaultDiff, prevScore = 0;
    private String playerName;

    private ImageIcon base;
    private ImageIcon deadBird;
    private ImageIcon flappyBirdIcon;
    private ImageIcon backgroundImage;
    private ImageIcon upperPipeIcon;
    private ImageIcon lowerPipeIcon;

    public FlappyBird(String theme, int Difficulty, String playername) {
    	this.playerName = playername;
        this.Diff = Difficulty;
        DefaultDiff = Diff;
        setDifficulty(Diff);

        flappyBirdIcon = new ImageIcon("Themes/" + theme + "/bird.png");
        backgroundImage = new ImageIcon("Themes/" + theme + "/background.png");
        upperPipeIcon = new ImageIcon("Themes/" + theme + "/obsdown.png");
        lowerPipeIcon = new ImageIcon("Themes/" + theme + "/obs.png");
        base = new ImageIcon("Themes/" + theme + "/base.png");
        deadBird = new ImageIcon("Themes/" + theme + "/dead_bird.png");

        backgroundImage = new ImageIcon(backgroundImage.getImage().getScaledInstance(800, 600, Image.SCALE_DEFAULT));
        flappyBirdIcon = new ImageIcon(flappyBirdIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
        base = new ImageIcon(base.getImage().getScaledInstance(800, 100, Image.SCALE_DEFAULT));

        // Values
        score = 0;
        birdX = 370;
        birdY = 225;
        birdVelocity = 0;
        rotationAngle = 0;
        obstacles = new ArrayList<>();
        random = new Random();

        addKeyListener(this);
        setFocusable(true);

        timer = new Timer(150, this);
        timer.start();

        generateObstacle();
    }

    public void setDifficulty(int difficulty){
        switch(difficulty) {
            case 0:
                Tick = 20;
                space = 220;
                distance = 580;
                velocity = 3;
                gravity = 1;
                break;
            case 1:
                Tick = 16;
                space = 190;
                distance = 530;
                velocity = 4;
                gravity = 1;
                break;
            case 2:
                Tick = 12;
                space = 150;
                distance = 470;
                velocity = 6;
                gravity = 1;
                break;
            case 3:
                Tick = 8;
                space = 110;
                distance = 410;
                velocity = 8;
                gravity = 2;
                break;
        }
    }

    public void getLevel(int level){
        if(level == (prevScore + 4*(Diff+1)) && Diff < 3){
            prevScore = level;
            setDifficulty(Diff);
            timer.setDelay(Tick);
            Diff += 1;
        }
    }

    public void playSound(String soundFilePath, float volume) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(soundFilePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float range = gainControl.getMaximum() - gainControl.getMinimum();
                float gain = (range * volume) + gainControl.getMinimum();
                gainControl.setValue(gain);
            }

            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateObstacle() {
        int Space = space; // space between upper and lower obstacles
        int width = 55;
        int height = random.nextInt(300) + 50; // random height for the lower obstacle
    
        obstacles.add(new Rectangle(800, 0, width, height)); // upper obstacle
        obstacles.add(new Rectangle(800, height + Space, width, 600 - height - Space)); // lower obstacle
    }
    
    public void move() {
        if (!endGame && startGame) {
            // Apply gravity
            birdVelocity += gravity;
            birdY += birdVelocity;
    
            // Move obstacles
            for (Rectangle obstacle : obstacles) {
                obstacle.x -= velocity; // Adjust the speed of obstacles
            }
    
            // Check for collisions and generate new obstacles
            Rectangle bird = new Rectangle(birdX, birdY, 50, 40);
            for (Rectangle obstacle : obstacles) {
                if (obstacle.intersects(bird) || birdY < 0 || birdY > 475) {
                    playSound("Sound/bird-hit.wav", gameVol);
                    endGame = true;
                }
    
                else if (birdX > obstacle.x && birdX < obstacle.x + velocity) {
                    // Bird has passed the obstacle without colliding
                    score += 0.5;
                    playSound("Sound/point.wav", gameVol); // Play a sound for scoring a point
                }
            }
    
            if (obstacles.get(obstacles.size() - 1).x < distance) {
                generateObstacle();
            }
    
            // Remove off-screen obstacles
            obstacles.removeIf(obstacle -> obstacle.x + obstacle.width < 0);
        }
        else if(!endGame && !startGame){
            // Apply gravity
            if(Down){
                birdY += 10;
                Down = false;
            } else{
                birdY -= 10;
                Down = true;
            }
    
            // Move obstacles (no movement when the game is over)
            for (Rectangle obstacle : obstacles) {
                obstacle.x -= 0; // Adjust the speed of obstacles
            }
        }
        else if (endGame && startGame){
            // Apply gravity
            birdVelocity += gravity;
            birdY += birdVelocity;
    
            // Move obstacles (no movement when the game is over)
            for (Rectangle obstacle : obstacles) {
                obstacle.x -= 0; // Adjust the speed of obstacles

                if (birdY > 475) {
                    gameOver();
                }
            }
    
            if (obstacles.get(obstacles.size() - 1).x < distance) {
                generateObstacle();
            }
    
            // Remove off-screen obstacles
            obstacles.removeIf(obstacle -> obstacle.x + obstacle.width < 0);
        }
    }   

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    
        // Draw background image
        backgroundImage.paintIcon(this, g, 0, 0);
    
        // Draw obstacles
        for (Rectangle obstacle : obstacles) {
            int pipeSpace = space; // space between upper and lower pipes
            int lowerPipeHeight = 600 - obstacle.height - pipeSpace;
    
            // Draw lower pipe
            g.drawImage(lowerPipeIcon.getImage(), obstacle.x, obstacle.y + obstacle.height + pipeSpace, obstacle.width, lowerPipeHeight, this);

            // Draw upper pipe
            g.drawImage(upperPipeIcon.getImage(), obstacle.x, obstacle.y, obstacle.width, obstacle.height, this);
        }
    
        // Draw ground
        base.paintIcon(this, g, 0, 520);

        // Rotate and draw Flappy Bird image
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(Math.toRadians(rotationAngle), birdX + 20, birdY + 22); // Adjust the rotation point
        flappyBirdIcon.paintIcon(this, g2d, birdX, birdY);
        g2d.rotate(-Math.toRadians(rotationAngle), birdX + 20, birdY + 22); // Reset rotation
    
        // Display score with black stroke
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 60));
        String scoreString = Integer.toString((int) score);
        FontMetrics metrics = g.getFontMetrics();

        // Center horizontally
        int x = (getWidth() - metrics.stringWidth(scoreString)) / 2;
        int y = 100; // Adjust the vertical position

        // Draw black outline
        int strokeWidth = 2; // Adjust the stroke width as needed
        ((Graphics2D) g).setStroke(new BasicStroke(strokeWidth));
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                g.drawString(scoreString, x + i * strokeWidth, y + j * strokeWidth);
            }
        }

        // Draw white fill
        g.setColor(Color.WHITE);
        g.drawString(scoreString, x, y);

        if(!endGame && !startGame){
            ImageIcon START = new ImageIcon("Themes/StartButton.png");
            START = new ImageIcon(START.getImage().getScaledInstance(150, 60, Image.SCALE_DEFAULT));
            START.paintIcon(this, g, birdX-55, 400);
        }

        getLevel((int) score);
    }
    
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    public void closeGame() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.dispose();  // Close the current game window
    }    
    
    public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) && !endGame) {
            startGame = true;
            timer.setDelay(Tick);
            birdVelocity = -13;
            rotationAngle = -20; // Adjust the rotation angle
            playSound("Sound/flap.wav", gameVol);
        }
        else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            timer.stop();
        
            ImageIcon pause = new ImageIcon("Themes/pause.png");
            pause = new ImageIcon(pause.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
        
            // Show pause dialog with three options
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Game Paused",
                    "Pause",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    pause,  // No icon for pause
                    new Object[]{"Continue", "Restart", "Quit"},
                    "Continue"
            );
        
            // Handle user choice
            if (choice == JOptionPane.YES_OPTION) {
                // Continue the game
                timer.start();
            } else if (choice == JOptionPane.NO_OPTION) {
                // Close the current game and restart launcher
                restartGame();
            } else {
                closeGame();
                Launcher.restartLauncher();
            }
        }
    }
    
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            rotationAngle = 30; // Adjust the rotation angle when releasing
        }
    }
    
    public void keyTyped(KeyEvent e) {}    

    public void gameOver() {
        timer.stop();

        // Resize the dead bird image
        deadBird = new ImageIcon(deadBird.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));

        // Show game over dialog with options
        int choice = JOptionPane.showOptionDialog(
                this,
                "You Lose",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                deadBird,
                new Object[]{"Retry", "Save Score", "Quit"},
                "Retry"
        );

        // Handle user choice
        if (choice == 0) {
            // Retry the game
            restartGame();
        } else if (choice == 1) {        	
            if(Diff > 0){
                new AddScore(playerName, score*Math.pow(Diff, Diff-1));
            }
            else if (Diff == 0) {
                new AddScore(playerName, score);
            }
        } else {
            // Quit the game
            closeGame();
            Launcher.restartLauncher();
        }
    }

    public void restartGame() {
        // Reset game variables
        score = 0;
        prevScore = 0;
        birdY = 225;
        birdVelocity = 0;
        rotationAngle = 0;
        startGame = false;
        endGame = false;
        Diff = DefaultDiff;

        // Clear obstacles and generate a new one
        setDifficulty(DefaultDiff);
        obstacles.clear();
        generateObstacle();

        // Restart the timer
        timer.setDelay(150);
        timer.start();
    }
}