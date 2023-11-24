import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Task3 extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public Task3() {
        setTitle("Employee Viewer");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create table model
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new Object[]{"Employee ID", "Name", "Age", "Salary"});

        // Create table and scroll pane
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add scroll pane to the frame
        add(scrollPane);

        // Fetch and display records from the "employees" table
        fetchAndDisplayData();
    }

    private void fetchAndDisplayData() {
        String url = "jdbc:mysql://localhost:3306/lab10";
        String username = "root";
        String password = "fast1234";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM employees");
            ResultSet resultSet = preparedStatement.executeQuery();

            // Add rows to the model
            while (resultSet.next()) {
                Object[] row = new Object[4];
                row[0] = resultSet.getInt("employeeID");
                row[1] = resultSet.getString("fname");
                row[2] = resultSet.getInt("age");
                row[3] = resultSet.getBigDecimal("salary");
                tableModel.addRow(row);
            }

            // Close resources
            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Task3 employeeViewer = new Task3();
            employeeViewer.setVisible(true);
        });
    }
}
