package com.mycompany.mavenproject3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Mavenproject3 extends JFrame implements Runnable {
    private String text;
    private int x;
    private int width;

    private BannerPanel bannerPanel;
    private JButton addProductButton;
    private JButton addSellButton;
    private JButton addCustomerButton;
    private JButton kelolaKategoriButton;
    private JButton laporanPenjualanButton;
    private JButton logoutButton; // Tambahan tombol logout

    private List<Product> productList = new ArrayList<>();
    private List<String> categoryList = new ArrayList<>(List.of("Coffee", "Dairy", "Juice", "Soda", "Tea"));
    private List<ProductForm> productForms = new ArrayList<>();
    private List<Sale> sales = new ArrayList<>();

    public Mavenproject3() {
        setTitle("WK. STI Chill");
        setSize(800, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Data awal produk
        productList.add(new Product(1, "P001", "Americano", "Coffee", 10000, 10));
        productList.add(new Product(2, "P002", "Pandan Latte", "Coffee", 20000, 10));
        productList.add(new Product(3, "P003", "Aren Latte", "Coffee", 15000, 10));
        productList.add(new Product(4, "P004", "Matcha Frappucino", "Tea", 28000, 10));
        productList.add(new Product(5, "P005", "Jus Apel", "Juice", 17000, 10));

        this.text = getBannerTextFromProducts();
        this.x = -getFontMetrics(new Font("Arial", Font.BOLD, 18)).stringWidth(text);

        // Banner
        bannerPanel = new BannerPanel();
        add(bannerPanel, BorderLayout.CENTER);

        // Panel tombol
        JPanel bottomPanel = new JPanel();
        addProductButton = new JButton("Kelola Produk");
        addSellButton = new JButton("Penjualan");
        addCustomerButton = new JButton("Kelola Customer");
        kelolaKategoriButton = new JButton("Kategori Produk");
        laporanPenjualanButton = new JButton("Laporan Penjualan");
        logoutButton = new JButton("Logout"); // Tombol Logout

        bottomPanel.add(addProductButton);
        bottomPanel.add(addSellButton);
        bottomPanel.add(addCustomerButton);
        bottomPanel.add(kelolaKategoriButton);
        bottomPanel.add(laporanPenjualanButton);
        bottomPanel.add(logoutButton); // Ditambahkan ke panel

        add(bottomPanel, BorderLayout.SOUTH);

        // Aksi tombol
        addProductButton.addActionListener(e -> {
            ProductForm pf = new ProductForm(this);
            productForms.add(pf);
            pf.setVisible(true);
        });

        addSellButton.addActionListener(e -> new SellingForm(this).setVisible(true));
        addCustomerButton.addActionListener(e -> new CustomerForm().setVisible(true));
        kelolaKategoriButton.addActionListener(e -> new CategoryForm(this).setVisible(true));
        laporanPenjualanButton.addActionListener(e -> new SalesReport(this).setVisible(true));

        // Aksi tombol logout
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginForm(); // Kembali ke form login
            }
        });

        setVisible(true);
        new Thread(this).start();
    }

    // Banner panel
    private class BannerPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLUE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString(text, x, getHeight() / 2 + 7);
        }
    }

    // Animasi banner
    @Override
    public void run() {
        width = getWidth();
        while (true) {
            x += 2;
            if (x > width) {
                x = -getFontMetrics(new Font("Arial", Font.BOLD, 18)).stringWidth(text);
            }
            bannerPanel.repaint();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Membuat teks banner dari daftar produk
    public String getBannerTextFromProducts() {
        StringBuilder sb = new StringBuilder("Menu yang tersedia: ");
        for (int i = 0; i < productList.size(); i++) {
            sb.append(productList.get(i).getName());
            if (i < productList.size() - 1) sb.append(" | ");
        }
        return sb.toString();
    }

    public void setBannerText(String newText) {
        this.text = newText;
        this.x = -getFontMetrics(new Font("Arial", Font.BOLD, 18)).stringWidth(text);
    }

    public void refreshBanner() {
        setBannerText(getBannerTextFromProducts());
    }

    public List<Product> getProductList() {
        return productList;
    }

    public List<String> getCategoryList() {
        return categoryList;
    }

    public void addCategory(String category) {
        if (!categoryList.contains(category)) {
            categoryList.add(category);
            updateAllProductFormCategoryCombo();
        }
    }

    public void removeCategory(String category) {
        categoryList.remove(category);
        updateAllProductFormCategoryCombo();
    }

    public void updateAllProductFormCategoryCombo() {
        for (ProductForm pf : productForms) {
            pf.updateCategoryCombo();
        }
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void addSale(Sale sale) {
        sales.add(sale);
    }

    // Diblokir akses langsung tanpa login
    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Akses ditolak! Silakan login terlebih dahulu.", "Akses Dibatasi", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }
}
