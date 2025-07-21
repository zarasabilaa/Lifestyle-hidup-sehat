import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.sql.Connection;

public class BeritaPage extends JFrame {

    public BeritaPage() {
        setTitle("Lifestyle Sehat - Berita");
        setSize(1000, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(20, 22, 43));
        sidebar.setBounds(0, 0, 200, 650);
        sidebar.setLayout(null);
        add(sidebar);

        JLabel logo = new JLabel("<html><center>Lifestyle<br>Hidup Sehat</center></html>", SwingConstants.CENTER);
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
            btn.setBackground(item.equals("Berita") ? new Color(160, 120, 170) : new Color(180, 160, 200));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(_ -> {
                switch (item) {
                    case "Beranda": new MainPage(); break;
                    case "Berita": new BeritaPage(); break;
                    case "Tips Olahraga": new TipsOlahragaPage(); break;
                    case "Makan Sehat": new MakanSehatPage(); break;
                    case "Logout":
                        JOptionPane.showMessageDialog(this, "Anda telah logout.");
                        new LoginForm();
                        break;
                }
                dispose();
            });
            sidebar.add(btn);
            y += 45;
        }

        // Header
        JTextField searchField = new JTextField("Pencarian.....");
        searchField.setBounds(220, 20, 300, 35);
        searchField.setBackground(new Color(230, 210, 230));
        searchField.setForeground(Color.GRAY);
        searchField.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (searchField.getText().equals("Pencarian.....")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
        });
        add(searchField);

        JLabel userLabel = new JLabel("admin", SwingConstants.RIGHT);
        userLabel.setBounds(780, 20, 180, 20);
        add(userLabel);

        JLabel avatar = new JLabel();
        avatar.setBounds(740, 20, 30, 30);
        try {
            ImageIcon icon = new ImageIcon(new ImageIcon("profil.jpg").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
            avatar.setIcon(icon);
        } catch (Exception e) {
            avatar.setText("\uD83D\uDC64");
        }
        add(avatar);

        JLabel beritaLabel = new JLabel("Berita");
        beritaLabel.setFont(new Font("Arial", Font.BOLD, 22));
        beritaLabel.setBounds(220, 70, 200, 30);
        add(beritaLabel);

        JButton btnTambahBerita = new JButton("Tambah Berita");
        btnTambahBerita.setBounds(750, 70, 150, 30);
        btnTambahBerita.setBackground(new Color(160, 120, 170));
        btnTambahBerita.setForeground(Color.WHITE);
        btnTambahBerita.setFocusPainted(false);
        btnTambahBerita.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTambahBerita.addActionListener(_ -> {
            new InputBeritaForm();
            dispose();
        });
        add(btnTambahBerita);

        // Container Berita
        JPanel container = new JPanel(null);
        container.setPreferredSize(new Dimension(740, 1000));
        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBounds(220, 110, 760, 330);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/database", "root", "");
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM berita ORDER BY tanggal DESC");

            int index = 0;
            while (rs.next()) {
                String judul = rs.getString("judul");
                String penulis = rs.getString("penulis");
                String tanggal = rs.getString("tanggal");
                String deskripsi = rs.getString("deskripsi");
                String link = rs.getString("link");
                String kategori = rs.getString("kategori");

                byte[] imgBytes = rs.getBytes("picture");
                ImageIcon imgIcon = null;
                if (imgBytes != null) {
                    BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imgBytes));
                    Image img = bufferedImage.getScaledInstance(210, 80, Image.SCALE_SMOOTH);
                    imgIcon = new ImageIcon(img);
                }

                int col = index % 3;
                int row = index / 3;
                int x = col * 250;
                int yNews = row * 220;

                JPanel newsPanel = new JPanel(null);
                newsPanel.setBackground(new Color(230, 210, 230));
                newsPanel.setBounds(x, yNews, 230, 200);
                newsPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

                JLabel imgLabel = new JLabel();
                imgLabel.setBounds(10, 10, 210, 80);
                if (imgIcon != null) imgLabel.setIcon(imgIcon);
                else imgLabel.setText("\uD83D\uDDBC");
                newsPanel.add(imgLabel);

                JLabel title = new JLabel("<html><center>" + judul + "</center></html>", SwingConstants.CENTER);
                title.setFont(new Font("Arial", Font.BOLD, 12));
                title.setBounds(10, 95, 210, 30);
                title.setHorizontalAlignment(SwingConstants.CENTER);
                title.setCursor(new Cursor(Cursor.HAND_CURSOR));
                title.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        JOptionPane.showMessageDialog(null,
                            "\uD83D\uDCCC " + judul +
                            "\n\uD83D\uDD8B Penulis: " + penulis +
                            "\n\uD83D\uDCC5 Tanggal: " + tanggal +
                            "\n\uD83D\uDCC2 Kategori: " + kategori +
                            "\n\n\uD83D\uDCDD " + (deskripsi.length() > 100 ? deskripsi.substring(0, 100) + "..." : deskripsi),
                            "Detail Berita",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                });
                newsPanel.add(title);

                JLabel author = new JLabel(penulis + " | " + tanggal);
                author.setFont(new Font("Arial", Font.PLAIN, 10));
                author.setBounds(10, 125, 200, 15);
                newsPanel.add(author);

                JLabel cat = new JLabel("Kategori: " + kategori);
                cat.setFont(new Font("Arial", Font.PLAIN, 10));
                cat.setBounds(10, 140, 200, 15);
                newsPanel.add(cat);

                JButton linkBtn = new JButton("Baca Selengkapnya");
                linkBtn.setBounds(10, 160, 180, 25);
                linkBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                linkBtn.setBackground(new Color(160, 120, 170));
                linkBtn.setForeground(Color.WHITE);
                linkBtn.setFont(new Font("Arial", Font.PLAIN, 11));
                linkBtn.setFocusPainted(false);
                linkBtn.addActionListener(_ -> {
                    try {
                        Desktop.getDesktop().browse(new java.net.URI(link));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Gagal membuka link.");
                    }
                });
                newsPanel.add(linkBtn);

                container.add(newsPanel);
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat berita dari database.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        // Motivasi
        JPanel motivasiPanel = new JPanel();
        motivasiPanel.setLayout(new BorderLayout());
        motivasiPanel.setBackground(new Color(200, 180, 230));
        motivasiPanel.setBounds(220, 470, 760, 80);
        JLabel motivasiLabel = new JLabel("<html><div style='text-align:center;'><b>\u2728 Tetap semangat jaga pola hidup sehat setiap hari! \u2728</b></div></html>", SwingConstants.CENTER);
        motivasiPanel.add(motivasiLabel, BorderLayout.CENTER);
        add(motivasiPanel);

    

        setVisible(true);
    }

    public static void main(String[] args) {
        new BeritaPage();
    }
}