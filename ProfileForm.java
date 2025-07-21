import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.*;

public class ProfileForm extends JFrame {
    private int userId;
    private JTextField txtNama;
    private JTextField txtEmail;
    private JLabel lblFoto;
    private File fotoTerpilih;

    public ProfileForm(int userId) {
        this.userId = userId;

        setTitle("Profil Pengguna");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblJudul = new JLabel("Profil Saya", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 22));
        lblJudul.setBounds(100, 20, 200, 30);
        add(lblJudul);

        lblFoto = new JLabel();
        lblFoto.setBounds(140, 70, 120, 120);
        lblFoto.setOpaque(true);
        lblFoto.setBackground(Color.LIGHT_GRAY);
        add(lblFoto);

        JButton btnGantiFoto = new JButton("Ganti Foto");
        btnGantiFoto.setBounds(140, 200, 120, 25);
        btnGantiFoto.addActionListener(_ -> pilihFoto());
        add(btnGantiFoto);

        JLabel lblNama = new JLabel("Nama:");
        lblNama.setBounds(50, 240, 100, 25);
        add(lblNama);

        txtNama = new JTextField();
        txtNama.setBounds(150, 240, 180, 25);
        add(txtNama);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 280, 100, 25);
        add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(150, 280, 180, 25);
        txtEmail.setEditable(false); // tidak bisa diganti
        add(txtEmail);

        JButton btnSimpan = new JButton("Simpan Perubahan");
        btnSimpan.setBounds(120, 340, 160, 30);
        btnSimpan.addActionListener(_ -> updateProfil());
        add(btnSimpan);

        loadData();
        setVisible(true);
    }

    private void updateProfil() {
        Connection conn = null;
        PreparedStatement ps = null;
        FileInputStream fis = null;
        try {
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/database", "root", "");
            String sql = "UPDATE users SET nama = ?, foto_profil = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, txtNama.getText());

            if (fotoTerpilih != null) {
                fis = new FileInputStream(fotoTerpilih);
                ps.setBinaryStream(2, fis, (int) fotoTerpilih.length());
            } else {
                ps.setNull(2, Types.BLOB);
            }

            ps.setInt(3, userId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Profil berhasil diperbarui.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan perubahan.");
        } finally {
            try { if (fis != null) fis.close(); } catch (Exception ex) {}
            try { if (ps != null) ps.close(); } catch (Exception ex) {}
            try { if (conn != null) conn.close(); } catch (Exception ex) {}
        }
    }

    private void loadData() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/database", "root", "");
            ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                txtNama.setText(rs.getString("nama"));
                txtEmail.setText(rs.getString("email"));
                Blob fotoBlob = rs.getBlob("foto_profil");
                if (fotoBlob != null) {
                    byte[] imgBytes = fotoBlob.getBytes(1, (int) fotoBlob.length());
                    ImageIcon icon = new ImageIcon(imgBytes);
                    Image scaled = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    lblFoto.setIcon(new ImageIcon(scaled));
                    lblFoto.setText("");
                } else {
                    lblFoto.setIcon(null);
                    lblFoto.setText("Tidak ada foto");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data profil.");
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ex) {}
            try { if (ps != null) ps.close(); } catch (Exception ex) {}
            try { if (conn != null) conn.close(); } catch (Exception ex) {}
        }
    }

    private void pilihFoto() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            fotoTerpilih = chooser.getSelectedFile();
            ImageIcon icon = new ImageIcon(fotoTerpilih.getAbsolutePath());
            Image scaled = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lblFoto.setIcon(new ImageIcon(scaled));
            lblFoto.setText("");
        }
    }

    public static void main(String[] args) {
        new ProfileForm(1); // contoh id user 1
    }
}
