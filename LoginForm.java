import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginForm extends JFrame {

    public LoginForm() {
        setTitle("Login - Lifestyle Hidup Sehat");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(20, 22, 43));
        panel.setBounds(0, 0, 400, 350);
        panel.setLayout(null);
        add(panel);

        JLabel titleLabel = new JLabel("LOGIN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(230, 210, 230));
        titleLabel.setBounds(100, 30, 200, 50);
        panel.add(titleLabel);

        JLabel userLabel = new JLabel("USERNAME:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(50, 100, 100, 25);
        panel.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(150, 100, 180, 30);
        panel.add(userField);

        JLabel passLabel = new JLabel("PASSWORD:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(50, 150, 100, 25);
        panel.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(150, 150, 180, 30);
        panel.add(passField);

        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBounds(140, 200, 120, 35);
        loginBtn.setBackground(new Color(230, 210, 230));
        panel.add(loginBtn);

        // Event tombol login
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText().trim();
                String password = new String(passField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username dan Password tidak boleh kosong.");
                    return;
                }

                String sql = "SELECT * FROM register WHERE username = ? AND password = ?";
                try (java.sql.Connection conn = koneksi.connectt.getConnection();  // PASTIKAN CLASS connectt SUDAH BENAR
                     PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Login Berhasil!");
                        new MainPage();
                        dispose();
                    } else {
                        int pilihan = JOptionPane.showConfirmDialog(null,
                                "Akun tidak ditemukan. Apakah Anda ingin register terlebih dahulu?",
                                "Login Gagal",
                                JOptionPane.YES_NO_OPTION);

                        if (pilihan == JOptionPane.YES_OPTION) {
                            new RegisterForm();
                            dispose();
                        }
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Gagal login: " + ex.getMessage());
                }
            }
        });

        // Label "belum punya akun?"
        JLabel noAccountLabel = new JLabel("APAKAH ANDA TIDAK MEMILIKI AKUN?");
        noAccountLabel.setForeground(Color.WHITE);
        noAccountLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        noAccountLabel.setBounds(70, 250, 230, 20);
        panel.add(noAccountLabel);

        // Label "REGISTER" bisa diklik
        JLabel registerLink = new JLabel("REGISTER");
        registerLink.setForeground(new Color(173, 216, 230)); // warna biru muda
        registerLink.setFont(new Font("Arial", Font.BOLD, 11));
        registerLink.setBounds(290, 250, 60, 20);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(registerLink);

        registerLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new RegisterForm();
                dispose();
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}
