import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Task6 extends JFrame {
    private JTextField textFieldID, textFieldName, textFieldAge;

    public Task6() {
        setTitle("Transaction Demo GUI");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create and place components
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("ID:"));
        textFieldID = new JTextField();
        panel.add(textFieldID);

        panel.add(new JLabel("Name:"));
        textFieldName = new JTextField();
        panel.add(textFieldName);

        panel.add(new JLabel("Age:"));
        textFieldAge = new JTextField();
        panel.add(textFieldAge);

        JButton executeButton = new JButton("Execute Transaction");
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeTransaction();
            }
        });
        panel.add(executeButton);

        // Add the panel to the frame
        add(panel);

        // Make the frame visible
        setVisible(true);
    }

    private void executeTransaction() {
        Connection connection = null;

        try {
            // Database connection details
            String url = "jdbc:mysql://localhost:3306/lab10";
            String username = "root";
            String password = "fast1234";

            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the database connection
            connection = DriverManager.getConnection(url, username, password);

            // Start the transaction
            connection.setAutoCommit(false);

            // Perform multiple operations within the transaction
            int id = Integer.parseInt(textFieldID.getText());
            String name = textFieldName.getText();
            int age = Integer.parseInt(textFieldAge.getText());

            insertRecord(connection, id, name, age);
            updateRecord(connection, id, name + "_Updated", age + 1);
            deleteRecord(connection, id);

            // Commit the transaction if all operations are successful
            connection.commit();
            JOptionPane.showMessageDialog(this, "Transaction committed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (ClassNotFoundException | SQLException | NumberFormatException ex) {
            // Rollback the transaction if any operation fails
            try {
                if (connection != null) {
                    connection.rollback();
                    JOptionPane.showMessageDialog(this, "Transaction rolled back.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            ex.printStackTrace();

        } finally {
            // Close the connection
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void insertRecord(Connection connection, int id, String name, int age) throws SQLException {
        String query = "INSERT INTO student (studentid, name, age) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, age);
            preparedStatement.executeUpdate();
            System.out.println("Record inserted successfully!");
        }
    }

    private void updateRecord(Connection connection, int id, String name, int age) throws SQLException {
        String query = "UPDATE student SET name = ?, age = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
            System.out.println("Record updated successfully!");
        }
    }

    private void deleteRecord(Connection connection, int id) throws SQLException {
        String query = "DELETE FROM student WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Record deleted successfully!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Task6::new);
    }
}
