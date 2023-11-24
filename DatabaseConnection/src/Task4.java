import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Task4 extends JFrame {
    private JTextField textFieldEmployeeID, textFieldNewAge;

    public Task4() {
        setTitle("Update Employee Age");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create and place components
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Employee ID:"));
        textFieldEmployeeID = new JTextField();
        panel.add(textFieldEmployeeID);

        panel.add(new JLabel("New Age:"));
        textFieldNewAge = new JTextField();
        panel.add(textFieldNewAge);

        JButton updateButton = new JButton("Update Age");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAge();
            }
        });
        panel.add(updateButton);

        // Add the panel to the frame
        add(panel);

        // Make the frame visible
        setVisible(true);
    }

    private void updateAge() {
        String employeeID = textFieldEmployeeID.getText();
        String newAgeStr = textFieldNewAge.getText();

        if (employeeID.isEmpty() || newAgeStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Empty Field Detected!", "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int newAge = Integer.parseInt(newAgeStr);

            String url = "jdbc:mysql://localhost:3306/lab10";
            String username = "root";
            String password = "fast1234";

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String query = "UPDATE employees SET age = ? WHERE employeeID = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, newAge);
                    preparedStatement.setString(2, employeeID);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Age Updated Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        textFieldEmployeeID.setText("");
                        textFieldNewAge.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Employee ID not found or Age not updated.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "New Age is Invalid", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Task4::new);
    }
}
