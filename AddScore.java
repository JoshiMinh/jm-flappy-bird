import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class AddScore {

    public AddScore(String Player, float Score) {
        // Database connection details
        String url = "jdbc:sqlserver://JoshiNitro5\\MSSQLSERVER02:1433;database=master";
        String username = "Joshi";
        String password = "StrongPassword";

        // SQL query
        String insertQuery = "INSERT INTO FlappyBird (PlayerName, Score) VALUES (?, ?)";

        try {
            // Step 1: Load the SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Step 2: Establish the database connection
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 // Step 3: Create a PreparedStatement for the query
                 PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

                // Set parameters for the query
                preparedStatement.setString(1, Player);
                preparedStatement.setInt(2, (int) Score);

                // Step 4: Execute the query
                preparedStatement.executeUpdate();
                System.out.println("Score added successfully!");

            } catch (SQLException e) {
                // Handle database connection or query execution errors
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ClassNotFoundException e) {
            // Handle missing JDBC driver errors
            JOptionPane.showMessageDialog(null, "SQL Server JDBC driver not found: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
