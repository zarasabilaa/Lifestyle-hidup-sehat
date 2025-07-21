// File: InputBeritaForm.java
import javax.swing.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Types;

public class InputBeritaForm extends JFrame {
    private File selectedFile;

    public InputBeritaForm() {
        setTitle("Input Berita");
        setSize(500, 500);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel lblJudul = new JLabel("Judul:");
        JLabel lblPenulis = new JLabel("Penulis:");
        JLabel lblTanggal = new JLabel("Tanggal:");
        JLabel lblDeskripsi = new JLabel("Deskripsi:");
        JLabel lblKategori = new JLabel("Kategori:");
        JLabel lblLink = new JLabel("Link:");
        JLabel lblGambar = new JLabel("Gambar:");

        JTextField txtJudul = new JTextField();
        JTextField txtPenulis = new JTextField();
        JTextField txtTanggal = new JTextField("2025-07-05");
        JTextArea txtDeskripsi = new JTextArea();
        JComboBox<String> txtKategori = new JComboBox<>(new String[]{"Umum", "Kesehatan", "Teknologi"});
        JTextField txtLink = new JTextField();
        JButton btnBrowse = new JButton("Pilih Gambar");

        JButton btnSubmit = new JButton("Simpan");

        lblJudul.setBounds(20, 20, 100, 25);
        txtJudul.setBounds(130, 20, 300, 25);
        lblPenulis.setBounds(20, 60, 100, 25);
        txtPenulis.setBounds(130, 60, 300, 25);
        lblTanggal.setBounds(20, 100, 100, 25);
        txtTanggal.setBounds(130, 100, 300, 25);
        lblDeskripsi.setBounds(20, 140, 100, 25);
        txtDeskripsi.setBounds(130, 140, 300, 60);
        lblKategori.setBounds(20, 210, 100, 25);
        txtKategori.setBounds(130, 210, 300, 25);
        lblLink.setBounds(20, 250, 100, 25);
        txtLink.setBounds(130, 250, 300, 25);
        lblGambar.setBounds(20, 290, 100, 25);
        btnBrowse.setBounds(130, 290, 300, 25);
        btnSubmit.setBounds(180, 330, 120, 30);

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

        btnSubmit.addActionListener(_ -> {
            if (txtJudul.getText().isEmpty() || txtDeskripsi.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Judul dan deskripsi tidak boleh kosong.");
                return;
            }

   
            PreparedStatement ps = null;
            Connection conn = null;
            try {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/database", "root", "");
                ps = conn.prepareStatement("INSERT INTO berita (judul, penulis, tanggal, deskripsi, kategori, link, picture) VALUES (?, ?, ?, ?, ?, ?, ?)");

                ps.setString(1, txtJudul.getText());
                ps.setString(2, txtPenulis.getText());
                ps.setDate(3, Date.valueOf(txtTanggal.getText()));
                ps.setString(4, txtDeskripsi.getText());
                ps.setString(5, txtKategori.getSelectedItem().toString());
                ps.setString(6, txtLink.getText());

                if (selectedFile != null) {
                    FileInputStream fis = new FileInputStream(selectedFile);
                    ps.setBinaryStream(7, fis, (int) selectedFile.length());
                } else {
                    ps.setNull(7, Types.BLOB);
                }

                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Berita berhasil disimpan.");
                dispose();
                new BeritaPage();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal menyimpan berita.");
            } finally {
                try {
                    if (ps != null) ps.close();
                    if (conn != null) conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new InputBeritaForm();
    }
}