import javax.swing.*;
import java.awt.*;
import java.io.*; 

public class Launcher {
    private static final String FILE_PATH = "LastPlay.txt";

    public static void restartLauncher() {
        main(new String[]{});
    }

    public static void main(String[] args) {
        // Initialize components
        JTextField nameField = new JTextField(10);

        JComboBox<String> themesComboBox = new JComboBox<>(new String[]{"Original", "Red Night", "Under Water", "9-11"});
        themesComboBox.setSelectedItem("Original");
        JComboBox<String> difficulty = new JComboBox<>(new String[]{"Easy", "Normal", "Hard", "Impossible"});
        difficulty.setSelectedItem("Normal");

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Player Name:"));
        panel.add(nameField);

        // Read the first line from the file and set it to the text field
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String firstLine = reader.readLine();
            if (firstLine != null) {
                nameField.setText(firstLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        panel.add(new JLabel("Select Theme:"));
        panel.add(themesComboBox);

        panel.add(new JLabel("Select Difficulty:"));
        panel.add(difficulty);

        panel.add(new JLabel("Copyright@JoshiMinh"));
        String[] buttonLabels = {"PLAY", "EXIT"};

        // Create and scale bird icon
        ImageIcon birdIcon = new ImageIcon("Themes/Flappy_Bird_icon.png");
        birdIcon = new ImageIcon(birdIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));

        // Create a hidden parent frame
        JFrame parentFrame = new JFrame();
        parentFrame.setUndecorated(true);
        parentFrame.setLocationRelativeTo(null);
        parentFrame.setIconImage(birdIcon.getImage());
        parentFrame.setVisible(true);

        // Show option dialog with the hidden parent frame
        int result = JOptionPane.showOptionDialog(
                parentFrame,
                panel,
                "Flappy Bird",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                birdIcon,
                buttonLabels,
                buttonLabels[0]);

        parentFrame.dispose(); // Dispose of the parent frame after the dialog is closed.

        if (result == 0) {
            // Get player name and selected theme
            String playerName = nameField.getText();
            String selectedTheme = (String) themesComboBox.getSelectedItem();
            int selectedDifficulty = difficulty.getSelectedIndex();

            // Create and configure game frame
            JFrame frame = new JFrame("Flappy Bird");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setResizable(false);
            frame.add(new FlappyBird(selectedTheme, selectedDifficulty));
            frame.setVisible(true);

            // Center the frame on the screen
            frame.setLocationRelativeTo(null);

            // Load custom icons
            frame.setIconImage(birdIcon.getImage());

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                writer.write(playerName);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.exit(0);
        }
    }
}
