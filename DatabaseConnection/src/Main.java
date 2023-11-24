import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "root", "fast1234");
            System.out.println(con);
            System.out.println("Connection Established Succssfully!!");
        }
        catch(Exception e){

        }

    }
}