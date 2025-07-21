import koneksi.connectt; 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterForm extends JFrame {
    private JTextField userField;
    private JPasswordField passField, confirmPassField;
    private JButton registerButton;

    public RegisterForm() {
        setTitle("Register - Lifestyle Hidup Sehat");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false); // Biarkan tetap false jika tidak ingin diubah ukurannya
        setLayout(null); // Gunakan layout manager yang lebih fleksibel jika memungkinkan, tapi null layout tidak masalah untuk tata letak tetap

        JPanel panel = new JPanel();
        panel.setBackground(new Color(20, 22, 43));
        panel.setBounds(0, 0, 450, 500);
        panel.setLayout(null); // Panel juga menggunakan null layout
        add(panel);

        JLabel titleLabel = new JLabel("REGISTER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28)); // Perbesar ukuran font
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(230, 210, 230));
        titleLabel.setBounds(100, 30, 250, 60);
        panel.add(titleLabel);

        userField = new JTextField();
        setupLabelAndField(panel, "USERNAME:", userField, 120);

        passField = new JPasswordField();
        setupLabelAndField(panel, "PASSWORD:", passField, 170);

        confirmPassField = new JPasswordField();
        setupLabelAndField(panel, "CONFIRM PASSWORD:", confirmPassField, 220);

        JLabel loginLabel = new JLabel("Sudah punya akun?");
        loginLabel.setForeground(Color.WHITE);
        loginLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // Sesuaikan font
        loginLabel.setBounds(100, 280, 150, 20); // Sesuaikan posisi
        panel.add(loginLabel);

        JLabel loginLink = new JLabel("Login di sini."); // Ubah teks agar lebih jelas
        loginLink.setForeground(new Color(173, 216, 230));
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.setFont(new Font("Arial", Font.BOLD, 12)); // Tebalkan link
        loginLink.setBounds(240, 280, 100, 20); // Sesuaikan posisi
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LoginForm().setVisible(true); // Pastikan LoginForm terlihat
                dispose(); // Tutup form register ini
            }
        });
        panel.add(loginLink);

        registerButton = new JButton("REGISTER");
        registerButton.setBounds(150, 330, 150, 45); // Sesuaikan ukuran dan posisi
        registerButton.setBackground(new Color(230, 210, 230));
        registerButton.setFocusPainted(false); // Hilangkan border fokus
        registerButton.setFont(new Font("Arial", Font.BOLD, 16)); // Perbesar font tombol
        panel.add(registerButton);

        // Tambahkan tombol kembali ke halaman utama atau dashboard jika diperlukan
        JButton backButton = new JButton("KEMBALI");
        backButton.setBounds(150, 385, 150, 45); // Posisikan di bawah tombol register
        backButton.setBackground(new Color(150, 150, 150)); // Warna abu-abu
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.addActionListener(_ -> {
            // Asumsi ada MainPage atau Dashboard yang ingin dituju
            // new MainPage().setVisible(true); // Contoh: kembali ke MainPage
            new LoginForm().setVisible(true); // Untuk sementara kembali ke Login
            dispose();
        });
        panel.add(backButton);


        registerButton.addActionListener(_ -> registerUser());
        setVisible(true);
    }

    // Mengubah nama method agar lebih deskriptif
    private void setupLabelAndField(JPanel panel, String text, JComponent comp, int y) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14)); // Sesuaikan font label
        label.setBounds(50, y, 150, 25); // Sesuaikan posisi
        panel.add(label);
        comp.setBounds(200, y, 200, 30); // Sesuaikan posisi field
        comp.setBackground(new Color(230, 210, 230));
        comp.setFont(new Font("Arial", Font.PLAIN, 14)); // Sesuaikan font field
        panel.add(comp);
    }

    private void registerUser() {
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword());
        String confirm = new String(confirmPassField.getPassword());

        if (user.isEmpty() || pass.isEmpty() || confirm.isEmpty()) { // Pastikan semua field terisi
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Konfirmasi password tidak cocok!", "Password Mismatch", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // PENTING: Untuk keamanan yang lebih baik, jangan menyimpan plain text password.
        // Gunakan hashing (misalnya BCrypt) sebelum menyimpan password ke database.
        // Untuk tujuan saat ini, saya akan tetap menggunakan plain text sesuai kode asli Anda.
        // String hashedPassword = hashPassword(pass); // Contoh jika menggunakan hashing

        // Perbaiki nama kolom database jika 'confirm_password' tidak diperlukan
        // Jika 'confirm_password' tidak ada di database, gunakan SQL berikut:
        // String sql = "INSERT INTO register(username, password) VALUES(?, ?)";
        
        // Asumsi kolom 'confirm_password' memang ada di tabel 'register' Anda
        String sql = "INSERT INTO register(username, password, confirm_password) VALUES(?, ?, ?)";

        // Menggunakan try-with-resources untuk memastikan koneksi dan statement ditutup
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = (Connection) connectt.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, user);
            stmt.setString(2, pass); // Atau hashedPassword jika Anda mengimplementasikan hashing
            stmt.setString(3, confirm); // Jika memang perlu menyimpan confirm_password

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Registrasi berhasil! Silakan login.", "Registrasi Berhasil", JOptionPane.INFORMATION_MESSAGE);
                new LoginForm().setVisible(true); // Pindah ke form Login
                dispose(); // Tutup form register ini
            } else {
                JOptionPane.showMessageDialog(this, "Registrasi gagal, coba lagi.", "Registrasi Gagal", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLIntegrityConstraintViolationException dup) {
            // Ini akan terpicu jika username adalah UNIQUE KEY dan sudah ada
            JOptionPane.showMessageDialog(this, "Username '" + user + "' sudah digunakan. Mohon gunakan username lain.", "Username Duplikat", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) { // Tangani exception lain yang mungkin terjadi dari connectt.getConnection()
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan tak terduga: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (conn != null)
                conn.close();
        }
    }
    
 

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterForm::new);
    }
}