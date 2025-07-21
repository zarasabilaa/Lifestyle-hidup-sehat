import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class EditTipsOlahragaForm extends JFrame {
    private JTextField tfJudul;
    private JTextArea taIsi;
    private JLabel lblGambar;
    private JButton btnPilihGambar;
    private JButton btnSimpan;

    private int idTips;
    private byte[] gambarBytes = null;

    public EditTipsOlahragaForm(int idTips, String judul, String isi, byte[] gambarAwal) {
        this.idTips = idTips;
        setTitle("Edit Tips Olahraga");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel lblJudul = new JLabel("Judul:");
        lblJudul.setBounds(30, 30, 100, 25);
        add(lblJudul);

        tfJudul = new JTextField(judul);
        tfJudul.setBounds(140, 30, 300, 25);
        add(tfJudul);

        JLabel lblIsi = new JLabel("Isi:");
        lblIsi.setBounds(30, 70, 100, 25);
        add(lblIsi);

        taIsi = new JTextArea(isi);
        JScrollPane scrollIsi = new JScrollPane(taIsi);
        scrollIsi.setBounds(140, 70, 300, 150);
        add(scrollIsi);

        lblGambar = new JLabel("Gambar:");
        lblGambar.setBounds(30, 240, 100, 25);
        add(lblGambar);

        JLabel previewGambar = new JLabel();
        previewGambar.setBounds(140, 230, 150, 100);
        previewGambar.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(previewGambar);

        if (gambarAwal != null) {
            ImageIcon icon = new ImageIcon(gambarAwal);
            Image img = icon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
            previewGambar.setIcon(new ImageIcon(img));
            gambarBytes = gambarAwal;
        }

        btnPilihGambar = new JButton("Pilih Gambar");
        btnPilihGambar.setBounds(300, 260, 140, 25);
        btnPilihGambar.addActionListener(_ -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try (FileInputStream fis = new FileInputStream(file)) {
                    gambarBytes = fis.readAllBytes();
                    ImageIcon icon = new ImageIcon(gambarBytes);
                    Image img = icon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
                    previewGambar.setIcon(new ImageIcon(img));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Gagal memuat gambar.");
                }
            }
        });
        add(btnPilihGambar);

        btnSimpan = new JButton("Simpan Perubahan");
        btnSimpan.setBounds(160, 370, 180, 30);
        btnSimpan.setBackground(new Color(160, 120, 170));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSimpan.addActionListener(_ -> simpanEdit());
        add(btnSimpan);

        setVisible(true);
    }

    private void simpanEdit() {
        String judul = tfJudul.getText().trim();
        String isi = taIsi.getText().trim();

        if (judul.isEmpty() || isi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua harus diisi.");
            return;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/database", "root", "");
            String sql = "UPDATE tips_olahraga SET judul=?, isi=?, gambar=? WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, judul);
            stmt.setString(2, isi);
            stmt.setBytes(3, gambarBytes);
            stmt.setInt(4, idTips);

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Tips berhasil diperbarui.");
                new TipsOlahragaPage();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui tips.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan.");
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
