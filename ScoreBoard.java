import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.*;
import java.awt.Font;

public class ScoreBoard {
    
    public ScoreBoard(){
        // Database connection details
        String url = "jdbc:sqlserver://JoshiNitro5\\MSSQLSERVER02:1433;database=master";
        String username = "Joshi";
        String password = "StrongPassword";
        
        ImageIcon star = new ImageIcon("Themes/star.png");
        star = new ImageIcon(star.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));

        try {
            //Load the SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            //Establish the database connection
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                System.out.println("Fetching ScoreBoard Successful!");

                //Execute the SQL query
                String sqlQuery = "SELECT PlayerName, MAX(Score) AS MaxScore " +
                                  "FROM FlappyBird " +
                                  "GROUP BY PlayerName " +
                                  "ORDER BY MaxScore DESC";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

                    //Process the result set
                    DefaultTableModel model = new DefaultTableModel() {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            // Set all cells to be non-editable
                            return false;
                        }
                    };
                    model.addColumn("Player");
                    model.addColumn("Max Score");

                    while (resultSet.next()) {
                        String playerName = resultSet.getString("PlayerName");
                        int maxScore = resultSet.getInt("MaxScore");
                        model.addRow(new Object[]{playerName, maxScore});
                    }

                    //Display the result in a JTable within a JOptionPane
                    JTable table = new JTable(model);

                    // Set header font to bold
                    JTableHeader header = table.getTableHeader();
                    Font headerFont = header.getFont();
                    header.setFont(headerFont.deriveFont(Font.BOLD));

                    // Disable column reordering
                    table.getTableHeader().setReorderingAllowed(false);

                    JScrollPane scrollPane = new JScrollPane(table);
                    JOptionPane.showMessageDialog(null, scrollPane, "Player Scores", JOptionPane.INFORMATION_MESSAGE, star);
                }
            }

        } catch (SQLException e) {
            // Handle database connection or query execution errors
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            // Handle missing JDBC driver errors
            JOptionPane.showMessageDialog(null, "SQL Server JDBC driver not found: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
