/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author HADI PC
 */
import config.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record Transaksi(
        int idTransaksi,
        int idMobil,
        int idPelanggan,
        LocalDate tanggalSewa,
        LocalDate tanggalKembali,
        double totalBiaya) {

    // Constructor tambahan untuk pembuatan transaksi baru tanpa ID
    public Transaksi(int idMobil, int idPelanggan, LocalDate tanggalSewa, LocalDate tanggalKembali, double totalBiaya) {
        this(0, idMobil, idPelanggan, tanggalSewa, tanggalKembali, totalBiaya);
    }

    // Method untuk memastikan tabel 'transaksi' ada, jika tidak ada maka membuat tabel tersebut
    public static void createTableIfNotExists() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS transaksi ("
                + "id_transaksi INT AUTO_INCREMENT PRIMARY KEY, "
                + "id_mobil INT NOT NULL, "
                + "id_pelanggan INT NOT NULL, "
                + "tanggal_sewa DATE NOT NULL, "
                + "tanggal_kembali DATE NOT NULL, "
                + "total_biaya DOUBLE NOT NULL)";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
        }
    }

    // Method untuk menyimpan transaksi ke database
    public boolean save() throws SQLException {
        String query = "INSERT INTO transaksi (id_mobil, id_pelanggan, tanggal_sewa, tanggal_kembali, total_biaya) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idMobil);
            stmt.setInt(2, idPelanggan);
            stmt.setDate(3, Date.valueOf(tanggalSewa));
            stmt.setDate(4, Date.valueOf(tanggalKembali));
            stmt.setDouble(5, totalBiaya);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Method untuk mengambil daftar transaksi berdasarkan keyword pencarian
    public static List<Transaksi> getAll(String keyword) throws SQLException {
        if (keyword == null || keyword.isEmpty()) {
            return getAll();
        }

        List<Transaksi> transaksiList = new ArrayList<>();
        String query = "SELECT * FROM transaksi WHERE "
                + "id_transaksi LIKE ? OR "
                + "id_mobil LIKE ? OR "
                + "id_pelanggan LIKE ? OR "
                + "tanggal_sewa LIKE ? OR "
                + "tanggal_kembali LIKE ? "
                + "ORDER BY tanggal_sewa DESC";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            String searchKeyword = "%" + keyword + "%";  // Search term with wildcards for LIKE

            // Set the same parameter for all fields
            stmt.setString(1, searchKeyword);  // id_transaksi
            stmt.setString(2, searchKeyword);  // id_mobil
            stmt.setString(3, searchKeyword);  // id_pelanggan
            stmt.setString(4, searchKeyword);  // tanggal_sewa
            stmt.setString(5, searchKeyword);  // tanggal_kembali

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transaksiList.add(new Transaksi(
                            rs.getInt("id_transaksi"),
                            rs.getInt("id_mobil"),
                            rs.getInt("id_pelanggan"),
                            rs.getDate("tanggal_sewa").toLocalDate(),
                            rs.getDate("tanggal_kembali").toLocalDate(),
                            rs.getDouble("total_biaya")
                    ));
                }
            }
        }
        return transaksiList;
    }

    // Method untuk mengambil semua transaksi
    public static List<Transaksi> getAll() throws SQLException {
        List<Transaksi> transaksiList = new ArrayList<>();
        String query = "SELECT * FROM transaksi ORDER BY tanggal_sewa DESC";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                transaksiList.add(new Transaksi(
                        rs.getInt("id_transaksi"),
                        rs.getInt("id_mobil"),
                        rs.getInt("id_pelanggan"),
                        rs.getDate("tanggal_sewa").toLocalDate(),
                        rs.getDate("tanggal_kembali").toLocalDate(),
                        rs.getDouble("total_biaya")
                ));
            }
        }
        return transaksiList;
    }

    // Method untuk mengambil semua transaksi tahun ini
    public static List<Transaksi> getAllThisYear() throws SQLException {
        List<Transaksi> transaksiList = new ArrayList<>();
        int currentYear = LocalDate.now().getYear(); // Ambil tahun saat ini
        String query = "SELECT * FROM transaksi WHERE YEAR(tanggal_sewa) = ? ORDER BY tanggal_sewa DESC"; // Filter berdasarkan tahun

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, currentYear); // Set tahun saat ini

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transaksiList.add(new Transaksi(
                            rs.getInt("id_transaksi"),
                            rs.getInt("id_mobil"),
                            rs.getInt("id_pelanggan"),
                            rs.getDate("tanggal_sewa").toLocalDate(),
                            rs.getDate("tanggal_kembali").toLocalDate(),
                            rs.getDouble("total_biaya")
                    ));
                }
            }
        }
        return transaksiList;
    }

    // Method untuk mengambil semua transaksi bulan ini
    public static List<Transaksi> getAllThisMonth() throws SQLException {
        List<Transaksi> transaksiList = new ArrayList<>();
        int currentYear = LocalDate.now().getYear(); // Ambil tahun saat ini
        int currentMonth = LocalDate.now().getMonthValue(); // Ambil bulan saat ini
        String query = "SELECT * FROM transaksi WHERE YEAR(tanggal_sewa) = ? AND MONTH(tanggal_sewa) = ? ORDER BY tanggal_sewa DESC"; // Filter berdasarkan bulan dan tahun

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, currentYear); // Set tahun saat ini
            stmt.setInt(2, currentMonth); // Set bulan saat ini

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transaksiList.add(new Transaksi(
                            rs.getInt("id_transaksi"),
                            rs.getInt("id_mobil"),
                            rs.getInt("id_pelanggan"),
                            rs.getDate("tanggal_sewa").toLocalDate(),
                            rs.getDate("tanggal_kembali").toLocalDate(),
                            rs.getDouble("total_biaya")
                    ));
                }
            }
        }
        return transaksiList;
    }

    // Method untuk mendapatkan mobil terkait dengan transaksi ini
    public Mobil mobil() throws SQLException {
        if (idMobil == 0) {
            return null;
        }

        // Using Mobil.getById method to fetch the Mobil object
        return Mobil.getById(idMobil);
    }

    // Method untuk mendapatkan pelanggan terkait dengan transaksi ini
    public Pelanggan pelanggan() throws SQLException {
        if (idPelanggan == 0) {
            return null;
        }

        // Using Pelanggan.getById method to fetch the Pelanggan object
        return Pelanggan.getById(idPelanggan);
    }

    // Method untuk mengupdate transaksi
    public boolean update() throws SQLException {
        String query = "UPDATE transaksi SET id_mobil = ?, id_pelanggan = ?, tanggal_sewa = ?, tanggal_kembali = ?, total_biaya = ? WHERE id_transaksi = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idMobil);
            stmt.setInt(2, idPelanggan);
            stmt.setDate(3, Date.valueOf(tanggalSewa));
            stmt.setDate(4, Date.valueOf(tanggalKembali));
            stmt.setDouble(5, totalBiaya);
            stmt.setInt(6, idTransaksi);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Method untuk menghapus transaksi
    public boolean delete() throws SQLException {
        String query = "DELETE FROM transaksi WHERE id_transaksi = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idTransaksi);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Method untuk menampilkan transaksi dalam format string
    @Override
    public String toString() {
        try {
            Mobil mobil = this.mobil();
            Pelanggan pelanggan = this.pelanggan();
            return tanggalSewa.toString()
                    + " -> " + tanggalKembali.toString()
                    + " | " + mobil.namaMobil()
                    + " - " + mobil.nomorPolisi()
                    + " | " + pelanggan.nama()
                    + " - " + pelanggan.nomorTelepon()
                    + " - " + pelanggan.email();
        } catch (Exception e) {
            return tanggalSewa.toString()
                    + " -> " + tanggalKembali.toString()
                    + " | Error: " + e.toString();
        }
    }
}
