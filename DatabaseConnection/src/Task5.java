import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Task5 extends JFrame {
    private JTextField textFieldStudentID;

    public Task5() {
        setTitle("Delete Student Record");
        setSize(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create and place components
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Student ID:"));
        textFieldStudentID = new JTextField();
        panel.add(textFieldStudentID);

        JButton deleteButton = new JButton("Delete Record");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRecord();
            }
        });
        panel.add(deleteButton);

        // Add the panel to the frame
        add(panel);

        // Make the frame visible
        setVisible(true);
    }

    private void deleteRecord() {
        String studentID = textFieldStudentID.getText();

        if (studentID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Empty Field Detected!", "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String url = "jdbc:mysql://localhost:3306/lab10";
            String username = "root";
            String password = "fast1234";

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String query = "DELETE FROM student WHERE studentID = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, studentID);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Record Deleted Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        textFieldStudentID.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Student ID not found or Record not deleted.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Task5::new);
    }
}
