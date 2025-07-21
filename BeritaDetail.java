import javax.swing.*;
import java.awt.*;

public class BeritaDetail extends JFrame {
    public BeritaDetail(String judul, String penulis, String tanggal, String isi, ImageIcon gambar) {
        setTitle(judul);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel lblJudul = new JLabel(judul);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 20));
        lblJudul.setBounds(20, 20, 450, 30);
        add(lblJudul);

        JLabel lblPenulis = new JLabel("Oleh: " + penulis);
        lblPenulis.setBounds(20, 55, 200, 20);
        add(lblPenulis);

        JLabel lblTanggal = new JLabel("Tanggal: " + tanggal);
        lblTanggal.setBounds(250, 55, 200, 20);
        add(lblTanggal);

        if (gambar != null) {
            JLabel lblGambar = new JLabel(new ImageIcon(
                    gambar.getImage().getScaledInstance(450, 120, Image.SCALE_SMOOTH)));
            lblGambar.setBounds(20, 80, 450, 120);
            add(lblGambar);
        }

        JTextArea isiArea = new JTextArea(isi);
        isiArea.setWrapStyleWord(true);
        isiArea.setLineWrap(true);
        isiArea.setEditable(false);

        // Bungkus di ScrollPane agar fleksibel
        JScrollPane scroll = new JScrollPane(isiArea);
        scroll.setBounds(20, 210, 450, 130);  // Tetap fix ukuran frame, tapi bisa discroll
        add(scroll);


        // Tombol Kembali
        JButton kembaliBtn = new JButton("← Kembali");
        kembaliBtn.setBounds(20, 360, 100, 30);
        kembaliBtn.setBackground(new Color(180, 160, 200));
        kembaliBtn.setFocusPainted(false);
        kembaliBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        kembaliBtn.addActionListener(_ -> {
            new BeritaPage(); // Kembali ke halaman berita
            dispose();        // Tutup halaman detail
        });

        add(kembaliBtn);

        setVisible(true);
    }
}
