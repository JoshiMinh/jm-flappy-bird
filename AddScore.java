import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.*;

public class AddScore {

    public AddScore(String player, double score) {
        String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12725771";
        String username = "sql12725771";
        String password = "ee6FTnsA97";
        String insertQuery = "INSERT INTO flappy_bird (PlayerName, Score) VALUES (?, ?)";

        if (player.isBlank()) {
            JOptionPane.showMessageDialog(null, "Player Name is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ImageIcon star = new ImageIcon("Themes/star.png");
        star = new ImageIcon(star.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement stmt = connection.prepareStatement(insertQuery)) {

                stmt.setString(1, player);
                stmt.setInt(2, (int) score);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Successfully Added Score: " + (int) score, "Score Added!",
                        JOptionPane.INFORMATION_MESSAGE, star);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "MySQL JDBC driver not found: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}