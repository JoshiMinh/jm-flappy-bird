import java.awt.Font;
import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ScoreBoard {
    private static final String URL = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12725771";
    private static final String USERNAME = "sql12725771";
    private static final String PASSWORD = "ee6FTnsA97";
    
    public ScoreBoard() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
                String sqlQuery = "SELECT PlayerName, MAX(Score) AS MaxScore " +
                                  "FROM flappy_bird " +
                                  "GROUP BY PlayerName " +
                                  "ORDER BY MaxScore DESC";
                
                try (PreparedStatement ps = connection.prepareStatement(sqlQuery);
                     ResultSet rs = ps.executeQuery()) {
                    
                    DefaultTableModel model = new DefaultTableModel(new Object[]{"Player", "Max Score"}, 0) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };
                    
                    while (rs.next()) {
                        model.addRow(new Object[]{rs.getString("PlayerName"), rs.getInt("MaxScore")});
                    }
                    
                    JTable table = new JTable(model);
                    table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD));
                    table.getTableHeader().setReorderingAllowed(false);
                    
                    ImageIcon star = new ImageIcon(new ImageIcon("Themes/star.png").getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
                    JScrollPane scrollPane = new JScrollPane(table);
                    JOptionPane.showMessageDialog(null, scrollPane, "Player Scores", JOptionPane.INFORMATION_MESSAGE, star);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}