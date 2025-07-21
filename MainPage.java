import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainPage extends JFrame {

    public MainPage() {
        setTitle("Lifestyle Sehat - Beranda");
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

        JLabel logo = new JLabel("<html>Lifestyle<br>Hidup Sehat</html>", SwingConstants.CENTER);
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
            btn.setBackground(item.equals("Beranda") ? new Color(160, 120, 170) : new Color(180, 160, 200));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btn.addActionListener(_ -> {
                switch (item) {
                    case "Logout":
                        JOptionPane.showMessageDialog(this, "Anda telah logout.");
                        new LoginForm();
                        dispose();
                        break;
                    case "Beranda":
                        new MainPage();
                        dispose();
                        break;
                    case "Berita":
                        new BeritaPage();
                        dispose();
                        break;
                    case "Tips Olahraga":
                        new TipsOlahragaPage();
                        dispose();
                        break;
                    case "Makan Sehat":
                        new MakanSehatPage();
                        dispose();
                        break;
                }
            });

            sidebar.add(btn);
            y += 45;
        }

        // Pencarian
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

        // Nama pengguna + avatar
        JLabel userLabel = new JLabel("Admin", SwingConstants.RIGHT);
        userLabel.setBounds(780, 20, 180, 20);
        add(userLabel);


        // Judul Halaman
        JLabel berandaLabel = new JLabel("Beranda");
        berandaLabel.setFont(new Font("Arial", Font.BOLD, 22));
        berandaLabel.setBounds(220, 70, 200, 30);
        add(berandaLabel);

        // Data dummy berita
        String[] judulArr = {
            "Manfaat Pola Hidup Sehat",
            "Cegah Depresi dengan Pola Makan Ini",
            "Matcha Kini Jadi Primadona"
        };
        String[] penulisArr = {"Prudial Syariah", "Hestianingsih", "CNN Indonesia"};
        String[] tanggalArr = {"01 Juni 2025", "01 Juli 2025", "26 Mei 2025"};
        String[] isiArr = {
            "Pola hidup sehat adalah cara hidup yang bertujuan untuk menjaga kesehatan tubuh dan mental.",
            "Diet ini memanfaatkan ikan-ikanan sebagai lauk utama.",
            "Tak hanya rasanya yang unik, matcha kini menjadi favorit masyarakat."
        };

        int startY = 110;

        for (int i = 0; i < judulArr.length; i++) {
            JPanel newsPanel = new JPanel();
            newsPanel.setLayout(null);
            newsPanel.setBackground(new Color(230, 210, 230));
            newsPanel.setBounds(220, startY, 750, 80);
            newsPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JLabel title = new JLabel(judulArr[i]);
            title.setFont(new Font("Arial", Font.BOLD, 14));
            title.setBounds(10, 5, 400, 20);
            newsPanel.add(title);

            JLabel author = new JLabel("Oleh: " + penulisArr[i]);
            author.setBounds(10, 25, 200, 20);
            newsPanel.add(author);

            JLabel date = new JLabel(tanggalArr[i]);
            date.setBounds(630, 5, 100, 20);
            newsPanel.add(date);

            JTextArea content = new JTextArea("Klik untuk melihat selengkapnya...");
            content.setWrapStyleWord(true);
            content.setLineWrap(true);
            content.setEditable(false);
            content.setOpaque(false);
            content.setBounds(10, 45, 700, 30);
            newsPanel.add(content);

            final int index = i;
            newsPanel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    new BeritaDetail(
                        judulArr[index],
                        penulisArr[index],
                        tanggalArr[index],
                        isiArr[index],
                        null
                    );
                }
            });

            add(newsPanel);
            startY += 90;
        }

        // Tips olahraga
        JPanel tipsPanel = new JPanel();
        tipsPanel.setLayout(new BorderLayout());
        tipsPanel.setBackground(new Color(230, 210, 230));
        tipsPanel.setBounds(220, startY, 370, 100);

        JLabel tipsLabel = new JLabel("TIPS OLAHRAGA AGAR HIDUP LEBIH SEHAT", SwingConstants.CENTER);
        JTextArea tipsText = new JTextArea("Lakukan olahraga teratur minimal 3x seminggu untuk menjaga kebugaran tubuh dan pikiran.");
        tipsText.setWrapStyleWord(true);
        tipsText.setLineWrap(true);
        tipsText.setEditable(false);
        tipsText.setOpaque(false);

        tipsPanel.add(tipsLabel, BorderLayout.NORTH);
        tipsPanel.add(tipsText, BorderLayout.CENTER);
        add(tipsPanel);

        // Makanan sehat
        JPanel makananPanel = new JPanel();
        makananPanel.setLayout(new BorderLayout());
        makananPanel.setBackground(new Color(230, 210, 230));
        makananPanel.setBounds(610, startY, 370, 100);

        JLabel makanLabel = new JLabel("MAKANAN SEHAT", SwingConstants.CENTER);
        JTextArea makanText = new JTextArea("Perbanyak konsumsi sayur, buah, dan air putih untuk mendukung metabolisme tubuh.");
        makanText.setWrapStyleWord(true);
        makanText.setLineWrap(true);
        makanText.setEditable(false);
        makanText.setOpaque(false);

        makananPanel.add(makanLabel, BorderLayout.NORTH);
        makananPanel.add(makanText, BorderLayout.CENTER);
        add(makananPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new MainPage();
    }
}
