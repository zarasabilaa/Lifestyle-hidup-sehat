import javax.swing.*;
import java.io.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class InputTipsOlahragaForm extends JFrame {
    private File selectedFile;

    public InputTipsOlahragaForm() {
        setTitle("Input Tips Olahraga");
        setSize(500, 500);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel lblJudul = new JLabel("Judul:");
        JLabel lblIsi = new JLabel("Isi Tips:");
        JLabel lblKategori = new JLabel("Kategori:");
        JLabel lblTanggal = new JLabel("Tanggal:");
        JLabel lblGambar = new JLabel("Gambar:");

        JTextField txtJudul = new JTextField();
        JTextArea txtIsi = new JTextArea();
        JComboBox<String> txtKategori = new JComboBox<>(new String[]{"Umum", "Pemula", "Lanjutan"});
        JTextField txtTanggal = new JTextField("2025-07-05"); // default hari ini
        JButton btnBrowse = new JButton("Pilih Gambar");

        JButton btnSubmit = new JButton("Simpan");

        lblJudul.setBounds(20, 20, 100, 25);
        txtJudul.setBounds(130, 20, 300, 25);
        lblIsi.setBounds(20, 60, 100, 25);
        txtIsi.setBounds(130, 60, 300, 100);
        lblKategori.setBounds(20, 170, 100, 25);
        txtKategori.setBounds(130, 170, 300, 25);
        lblTanggal.setBounds(20, 210, 100, 25);
        txtTanggal.setBounds(130, 210, 300, 25);
        lblGambar.setBounds(20, 250, 100, 25);
        btnBrowse.setBounds(130, 250, 300, 25);
        btnSubmit.setBounds(180, 300, 120, 30);

        add(lblJudul); add(txtJudul);
        add(lblIsi); add(txtIsi);
        add(lblKategori); add(txtKategori);
        add(lblTanggal); add(txtTanggal);
        add(lblGambar); add(btnBrowse);
        add(btnSubmit);

        btnBrowse.addActionListener(_ -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
            }
        });

        btnSubmit.addActionListener(_ -> {
            if (txtJudul.getText().isEmpty() || txtIsi.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Judul dan isi tidak boleh kosong.");
                return;
            }

            Connection conn = null;
            PreparedStatement ps = null;
            try {
                conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/database", "root", "");
                ps = conn.prepareStatement("INSERT INTO tips_olahraga (judul, isi, kategori, tanggal, gambar) VALUES (?, ?, ?, ?, ?)");

                ps.setString(1, txtJudul.getText());
                ps.setString(2, txtIsi.getText());
                ps.setString(3, txtKategori.getSelectedItem().toString());
                ps.setDate(4, Date.valueOf(txtTanggal.getText()));

                if (selectedFile != null) {
                    FileInputStream fis = new FileInputStream(selectedFile);
                    ps.setBinaryStream(5, fis, (int) selectedFile.length());
                } else {
                    ps.setNull(5, Types.BLOB);
                }

                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Tips olahraga berhasil disimpan.");
                dispose(); // tutup form
                new TipsOlahragaPage(); // buka halaman daftar tips
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal menyimpan tips.");
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
        new InputTipsOlahragaForm();
    }
}
