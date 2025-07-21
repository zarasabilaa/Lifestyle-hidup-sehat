import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class InputMakanSehat extends JFrame {
    private File selectedFile;

    public InputMakanSehat() {
        setTitle("Input Makanan Sehat");
        setSize(500, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Label
        JLabel lblJudul = new JLabel("Nama Makanan:");
        JLabel lblDeskripsi = new JLabel("Deskripsi:");
        JLabel lblTanggal = new JLabel("Tanggal:");
        JLabel lblGambar = new JLabel("Gambar:");

        // Input fields
        JTextField txtJudul = new JTextField();
        JTextArea txtDeskripsi = new JTextArea();
        JScrollPane scrollDeskripsi = new JScrollPane(txtDeskripsi);
        JTextField txtTanggal = new JTextField("2025-07-05"); // Default hari ini
        JButton btnBrowse = new JButton("Pilih Gambar");
        JButton btnSubmit = new JButton("Simpan");

        // Set bounds
        lblJudul.setBounds(20, 20, 100, 25);
        txtJudul.setBounds(130, 20, 320, 25);

        lblDeskripsi.setBounds(20, 60, 100, 25);
        scrollDeskripsi.setBounds(130, 60, 320, 80);


        lblTanggal.setBounds(20, 190, 100, 25);
        txtTanggal.setBounds(130, 190, 320, 25);

        lblGambar.setBounds(20, 230, 100, 25);
        btnBrowse.setBounds(130, 230, 320, 25);

        btnSubmit.setBounds(180, 280, 140, 35);

        // Add components
        add(lblJudul); add(txtJudul);
        add(lblDeskripsi); add(scrollDeskripsi);
        add(lblTanggal); add(txtTanggal);
        add(lblGambar); add(btnBrowse);
        add(btnSubmit);

        // File chooser
        btnBrowse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
            }
        });

        // Submit button
        btnSubmit.addActionListener(e -> {
            if (txtJudul.getText().trim().isEmpty() || txtDeskripsi.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama makanan dan deskripsi tidak boleh kosong.");
                return;
            }

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/database", "root", "");
                 PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO makan_sehat (nama_makanan, deskripsi, tanggal, gambar) VALUES (?, ?, ?, ?)")
            ) {
                ps.setString(1, txtJudul.getText().trim());
                ps.setString(2, txtDeskripsi.getText().trim());
                ps.setDate(3, Date.valueOf(txtTanggal.getText().trim()));

                if (selectedFile != null) {
                    FileInputStream fis = new FileInputStream(selectedFile);
                    ps.setBinaryStream(4, fis, (int) selectedFile.length());
                } else {
                    ps.setNull(4, Types.BLOB);
                }

                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data makanan sehat berhasil disimpan.");
                dispose();
                new MakanSehatPage(); // Ganti sesuai nama kelas tampilan data
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data. Periksa format tanggal dan koneksi database.");
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InputMakanSehat::new);
    }
}