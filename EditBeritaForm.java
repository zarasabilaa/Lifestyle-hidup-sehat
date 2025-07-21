// File: EditBeritaForm.java
import javax.swing.*;
import java.io.*;
import java.sql.*;

public class EditBeritaForm extends JFrame {
    private File selectedFile;
    private int beritaId;

    public EditBeritaForm(int id) {
        this.beritaId = id;
        setTitle("Edit Berita");
        setSize(500, 550);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel lblJudul = new JLabel("Judul:");
        JLabel lblPenulis = new JLabel("Penulis:");
        JLabel lblTanggal = new JLabel("Tanggal:");
        JLabel lblDeskripsi = new JLabel("Deskripsi:");
        JLabel lblKategori = new JLabel("Kategori:");
        JLabel lblLink = new JLabel("Link:");
        JLabel lblGambar = new JLabel("Gambar Baru (opsional):");

        JTextField txtJudul = new JTextField();
        JTextField txtPenulis = new JTextField();
        JTextField txtTanggal = new JTextField();
        JTextArea txtDeskripsi = new JTextArea();
        JComboBox<String> txtKategori = new JComboBox<>(new String[]{"Umum", "Kesehatan", "Teknologi"});
        JTextField txtLink = new JTextField();
        JButton btnBrowse = new JButton("Pilih Gambar Baru");
        JButton btnSubmit = new JButton("Update");

        lblJudul.setBounds(20, 20, 150, 25);
        txtJudul.setBounds(180, 20, 280, 25);
        lblPenulis.setBounds(20, 60, 150, 25);
        txtPenulis.setBounds(180, 60, 280, 25);
        lblTanggal.setBounds(20, 100, 150, 25);
        txtTanggal.setBounds(180, 100, 280, 25);
        lblDeskripsi.setBounds(20, 140, 150, 25);
        txtDeskripsi.setBounds(180, 140, 280, 60);
        lblKategori.setBounds(20, 210, 150, 25);
        txtKategori.setBounds(180, 210, 280, 25);
        lblLink.setBounds(20, 250, 150, 25);
        txtLink.setBounds(180, 250, 280, 25);
        lblGambar.setBounds(20, 290, 150, 25);
        btnBrowse.setBounds(180, 290, 280, 25);
        btnSubmit.setBounds(180, 340, 120, 30);

        add(lblJudul); add(txtJudul);
        add(lblPenulis); add(txtPenulis);
        add(lblTanggal); add(txtTanggal);
        add(lblDeskripsi); add(txtDeskripsi);
        add(lblKategori); add(txtKategori);
        add(lblLink); add(txtLink);
        add(lblGambar); add(btnBrowse);
        add(btnSubmit);

        btnBrowse.addActionListener(_ -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
            }
        });

        // Load berita lama
        final Connection[] conn = new Connection[1];
        try {
            conn[0] = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/database", "root", "");
            PreparedStatement ps = conn[0].prepareStatement("SELECT * FROM berita WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtJudul.setText(rs.getString("judul"));
                txtPenulis.setText(rs.getString("penulis"));
                txtTanggal.setText(rs.getString("tanggal"));
                txtDeskripsi.setText(rs.getString("deskripsi"));
                txtKategori.setSelectedItem(rs.getString("kategori"));
                txtLink.setText(rs.getString("link"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data berita.");
        } finally {
            if (conn[0] != null) {
                conn[0].close();
            }
        }

        btnSubmit.addActionListener(_ -> {
            Connection conn2 = null;
            PreparedStatement ps = null;
            FileInputStream fis = null;
            try {
                conn2 = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/database", "root", "");
                String sql;

                if (selectedFile != null) {
                    sql = "UPDATE berita SET judul=?, penulis=?, tanggal=?, deskripsi=?, kategori=?, link=?, picture=? WHERE id=?";
                    ps = conn2.prepareStatement(sql);
                    ps.setString(1, txtJudul.getText());
                    ps.setString(2, txtPenulis.getText());
                    ps.setDate(3, Date.valueOf(txtTanggal.getText()));
                    ps.setString(4, txtDeskripsi.getText());
                    ps.setString(5, txtKategori.getSelectedItem().toString());
                    ps.setString(6, txtLink.getText());
                    fis = new FileInputStream(selectedFile);
                    ps.setBinaryStream(7, fis, (int) selectedFile.length());
                    ps.setInt(8, beritaId);
                } else {
                    sql = "UPDATE berita SET judul=?, penulis=?, tanggal=?, deskripsi=?, kategori=?, link=? WHERE id=?";
                    ps = conn2.prepareStatement(sql);
                    ps.setString(1, txtJudul.getText());
                    ps.setString(2, txtPenulis.getText());
                    ps.setDate(3, Date.valueOf(txtTanggal.getText()));
                    ps.setString(4, txtDeskripsi.getText());
                    ps.setString(5, txtKategori.getSelectedItem().toString());
                    ps.setString(6, txtLink.getText());
                    ps.setInt(7, beritaId);
                }

                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Berita berhasil diupdate.");
                dispose();
                new BeritaPage();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal update berita.");
            } finally {
                try {
                    if (fis != null) fis.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    if (ps != null) ps.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                if (conn2 != null) conn2.close();
            }
        });

        setVisible(true);
    }
}