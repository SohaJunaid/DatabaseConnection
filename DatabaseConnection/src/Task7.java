import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Task7 extends JFrame {
    private JTextField textFieldID, textFieldName, textFieldAge;
    private JTextArea textAreaDisplay;

    public Task7() {
        setTitle("Database Swing App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create and place components
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.add(new JLabel("ID:"));
        textFieldID = new JTextField();
        inputPanel.add(textFieldID);

        inputPanel.add(new JLabel("Name:"));
        textFieldName = new JTextField();
        inputPanel.add(textFieldName);

        inputPanel.add(new JLabel("Age:"));
        textFieldAge = new JTextField();
        inputPanel.add(textFieldAge);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRecord();
            }
        });
        inputPanel.add(addButton);

        JButton displayButton = new JButton("Display");
        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayRecords();
            }
        });
        inputPanel.add(displayButton);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRecord();
            }
        });
        inputPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRecord();
            }
        });
        inputPanel.add(deleteButton);

        textAreaDisplay = new JTextArea();
        textAreaDisplay.setEditable(false);

        // Create a split pane to divide inputPanel and textAreaDisplay
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, new JScrollPane(textAreaDisplay));
        splitPane.setResizeWeight(0.3);

        // Add the split pane to the frame
        add(splitPane);

        // Make the frame visible
        setVisible(true);
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/lab10";
        String username = "root";
        String password = "fast1234";

        return DriverManager.getConnection(url, username, password);
    }

    private void addRecord() {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO employees (employeeID, fname, age) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                int id = Integer.parseInt(textFieldID.getText());
                String name = textFieldName.getText();
                int age = Integer.parseInt(textFieldAge.getText());

                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, name);
                preparedStatement.setInt(3, age);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Record added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add record.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayRecords() {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM employees";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                StringBuilder displayText = new StringBuilder("Records:\n");
                while (resultSet.next()) {
                    int id = resultSet.getInt("employeeID");
                    String name = resultSet.getString("fname");
                    int age = resultSet.getInt("age");

                    displayText.append(String.format("ID: %d, Name: %s, Age: %d\n", id, name, age));
                }

                textAreaDisplay.setText(displayText.toString());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRecord() {
        try (Connection connection = getConnection()) {
            String query = "UPDATE employees SET fname = ?, age = ? WHERE employeeID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                String name = textFieldName.getText();
                int age = Integer.parseInt(textFieldAge.getText());
                int id = Integer.parseInt(textFieldID.getText());

                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, age);
                preparedStatement.setInt(3, id);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Record updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update record.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRecord() {
        try (Connection connection = getConnection()) {
            String query = "DELETE FROM employees WHERE employeeID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                int id = Integer.parseInt(textFieldID.getText());

                preparedStatement.setInt(1, id);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Record deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete record.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        textFieldID.setText("");
        textFieldName.setText("");
        textFieldAge.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Task7::new);
    }
}
