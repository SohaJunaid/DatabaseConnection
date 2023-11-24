import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Task2 extends JFrame {
    private JTextField textFieldStudentID, textFieldName, textFieldAge;

    public Task2() {
        setTitle("Student Record Entry");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create and place components
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Student ID:"));
        textFieldStudentID = new JTextField();
        panel.add(textFieldStudentID);

        panel.add(new JLabel("Name:"));
        textFieldName = new JTextField();
        panel.add(textFieldName);

        panel.add(new JLabel("Age:"));
        textFieldAge = new JTextField();
        panel.add(textFieldAge);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertRecord();
            }
        });
        panel.add(submitButton);

        // Add the panel to the frame
        add(panel);

        // Make the frame visible
        setVisible(true);
    }

    private void insertRecord() {
        String studentID = textFieldStudentID.getText();
        String name = textFieldName.getText();
        String ageStr = textFieldAge.getText();

        if (studentID.isEmpty() || name.isEmpty() || ageStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Empty Field Detected!", "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);

            String url = "jdbc:mysql://localhost:3306/lab10";
            String username = "root";
            String password = "fast1234";

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String query = "INSERT INTO student (studentID, name, age) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, studentID);
                    preparedStatement.setString(2, name);
                    preparedStatement.setInt(3, age);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Success!!!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        textFieldStudentID.setText("");
                        textFieldName.setText("");
                        textFieldAge.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed!!!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age is Invalid", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Task2();
            }
        });
    }
}