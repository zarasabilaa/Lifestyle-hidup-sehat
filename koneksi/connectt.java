package koneksi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectt {
    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/database"; // ganti dengan nama database kamu
            String user = "root";
            String pass = "";

            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
