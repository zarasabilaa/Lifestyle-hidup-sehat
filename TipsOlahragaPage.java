import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class TipsOlahragaPage extends JFrame {

    public TipsOlahragaPage() {
        setTitle("Tips Olahraga - Lifestyle Sehat");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(20, 22, 43));
        sidebar.setBounds(0, 0, 200, 650);
        sidebar.setLayout(null);
        add(sidebar);

        JLabel logo = new JLabel("Lifestyle\nHidup Sehat", SwingConstants.CENTER);
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Serif", Font.BOLD, 18));
        logo.setBounds(20, 20, 160, 50);
        sidebar.add(logo);

        String[] menuItems = {"Beranda", "Berita", "Tips Olahraga", "Makan Sehat", "Logout"};
        int y = 90;
        for (String item : menuItems) {
            JButton btn = new JButton(item);
            btn.setBounds(20, y, 160, 35);
            btn.setFocusPainted(false);
            btn.setBackground(item.equals("Tips Olahraga") ? new Color(160, 120, 170) : new Color(180, 160, 200));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btn.addActionListener(_ -> {
                switch (item) {
                    case "Beranda": new MainPage(); break;
                    case "Berita": new BeritaPage(); break;
                    case "Tips Olahraga": new TipsOlahragaPage(); break;
                    case "Makan Sehat": new MakanSehatPage(); break;
                    case "Logout":
                        JOptionPane.showMessageDialog(this, "Anda telah logout.");
                        new LoginForm(); break;
                }
                dispose();
            });

            sidebar.add(btn);
            y += 45;
        }

        // Search & Tambah Tips
        JTextField searchField = new JTextField("Pencarian.....");
        searchField.setBounds(220, 20, 300, 35);
        add(searchField);

        JButton btnTambahTips = new JButton("Tambahkan Tips");
        btnTambahTips.setBounds(800, 20, 150, 35);
        btnTambahTips.setBackground(new Color(160, 120, 170));
        btnTambahTips.setForeground(Color.WHITE);
        btnTambahTips.setFocusPainted(false);
        btnTambahTips.addActionListener(_ -> {
            new InputTipsOlahragaForm(); // Form input tips
            dispose();
        });
        add(btnTambahTips);

        JLabel title = new JLabel("Tips Olahraga");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(220, 70, 300, 30);
        add(title);

        // Panel container tips
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setPreferredSize(new Dimension(740, 1000));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBounds(220, 110, 740, 500);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/database", "root", "")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM tips_olahraga ORDER BY tanggal DESC");

            int i = 0;
            int posY = 0;
            int posX = 0;

            while (rs.next()) {
                int id = rs.getInt("id");
                String judul = rs.getString("judul");
                String tanggal = rs.getString("tanggal");
                String isi = rs.getString("isi");
                byte[] imgBytes = rs.getBytes("gambar");

                JPanel card = new JPanel();
                card.setLayout(null);
                card.setBackground(new Color(230, 210, 230));
                card.setBounds(posX, posY, 360, 180);

                JLabel gambar = new JLabel();
                if (imgBytes != null && imgBytes.length > 0) {
                    ImageIcon imageIcon = new ImageIcon(imgBytes);
                    Image scaled = imageIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                    gambar.setIcon(new ImageIcon(scaled));
                } else {
                    gambar.setBackground(Color.LIGHT_GRAY);
                    gambar.setOpaque(true);
                    gambar.setText("No Image");
                    gambar.setHorizontalAlignment(SwingConstants.CENTER);
                }
                gambar.setBounds(10, 10, 80, 80);
                card.add(gambar);

                JTextArea lblJudul = new JTextArea(judul);
                lblJudul.setFont(new Font("Arial", Font.BOLD, 14));
                lblJudul.setLineWrap(true);
                lblJudul.setWrapStyleWord(true);
                lblJudul.setOpaque(false);
                lblJudul.setEditable(false);
                lblJudul.setBounds(100, 10, 240, 40);
                card.add(lblJudul);

                JLabel lblTanggal = new JLabel(tanggal);
                lblTanggal.setFont(new Font("Arial", Font.PLAIN, 10));
                lblTanggal.setBounds(100, 55, 150, 20);
                card.add(lblTanggal);

                JTextArea lblIsi = new JTextArea(isi);
                lblIsi.setWrapStyleWord(true);
                lblIsi.setLineWrap(true);
                lblIsi.setEditable(false);
                lblIsi.setOpaque(false);
                lblIsi.setBounds(10, 100, 340, 40);
                card.add(lblIsi);

                // Tombol Edit
                JButton btnEdit = new JButton("Edit");
                btnEdit.setBounds(190, 145, 70, 25);
                btnEdit.setBackground(new Color(100, 180, 100));
                btnEdit.setForeground(Color.WHITE);
                btnEdit.addActionListener(_ -> {
                    new EditTipsOlahragaForm(id, judul, isi, imgBytes); // perbaiki parameter jika perlu
                    dispose();
                });
                card.add(btnEdit);

                // Tombol Hapus - diperbaiki koneksinya
                JButton btnDelete = new JButton("Hapus");
                btnDelete.setBounds(270, 145, 70, 25);
                btnDelete.setBackground(new Color(200, 80, 80));
                btnDelete.setForeground(Color.WHITE);
                btnDelete.addActionListener(_ -> {
                    int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus tips ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try (Connection deleteConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/database", "root", "")) {
                            PreparedStatement ps = deleteConn.prepareStatement("DELETE FROM tips_olahraga WHERE id = ?");
                            ps.setInt(1, id);
                            ps.executeUpdate();
                            JOptionPane.showMessageDialog(this, "Tips berhasil dihapus!");
                            dispose();
                            new TipsOlahragaPage();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(this, "Gagal menghapus tips.");
                        }
                    }
                });
                card.add(btnDelete);

                contentPanel.add(card);

                if (i % 2 == 0) posX = 380;
                else {
                    posX = 0;
                    posY += 190;
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data tips olahraga dari database.");
        }

        setVisible(true);
    }

    public static void main(String[] args) {
        new TipsOlahragaPage();
    }
}
