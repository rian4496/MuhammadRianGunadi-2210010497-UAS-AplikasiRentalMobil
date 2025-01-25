/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Pongo
 */
import config.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public record Mobil(
        int idMobil,
        String namaMobil,
        String nomorPolisi,
        String tipe,
        double hargaSewaPerHari,
        boolean status,
        byte[] gambar) {

    // Constructor tambahan untuk pembuatan mobil baru tanpa ID
    public Mobil(String namaMobil, String nomorPolisi, String tipe, double hargaSewaPerHari, boolean status, byte[] gambar) {
        this(0, namaMobil, nomorPolisi, tipe, hargaSewaPerHari, status, gambar);
    }

    // Method untuk memastikan tabel 'mobil' ada, jika tidak ada maka membuat tabel tersebut
    public static void createTableIfNotExists() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS mobil ("
                + "id_mobil INT AUTO_INCREMENT PRIMARY KEY, "
                + "nama_mobil VARCHAR(255) NOT NULL, "
                + "nomor_polisi VARCHAR(20) NOT NULL, "
                + "tipe VARCHAR(50) NOT NULL, "
                + "harga_sewa_per_hari DOUBLE NOT NULL, "
                + "status BOOLEAN NOT NULL, "
                + "gambar LONGBLOB)";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
        }
    }

    // Method untuk menyimpan mobil ke database
    public boolean save() throws SQLException {
        String query = "INSERT INTO mobil (nama_mobil, nomor_polisi, tipe, harga_sewa_per_hari, status, gambar) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, namaMobil);
            stmt.setString(2, nomorPolisi);
            stmt.setString(3, tipe);
            stmt.setDouble(4, hargaSewaPerHari);
            stmt.setBoolean(5, status);
            stmt.setBytes(6, gambar);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Method untuk mengambil mobil berdasarkan id
    public static Mobil getById(int id) throws SQLException {
        String query = "SELECT * FROM mobil WHERE id_mobil = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Mobil(
                            rs.getInt("id_mobil"),
                            rs.getString("nama_mobil"),
                            rs.getString("nomor_polisi"),
                            rs.getString("tipe"),
                            rs.getDouble("harga_sewa_per_hari"),
                            rs.getBoolean("status"),
                            rs.getBytes("gambar")
                    );
                }
            }
        }
        return null; // Return null if no mobil is found with the given ID
    }

    // Method untuk mengambil daftar mobil dari database
    public static List<Mobil> getAll() throws SQLException {
        List<Mobil> mobilList = new ArrayList<>();
        String query = "SELECT * FROM mobil";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                mobilList.add(new Mobil(
                        rs.getInt("id_mobil"),
                        rs.getString("nama_mobil"),
                        rs.getString("nomor_polisi"),
                        rs.getString("tipe"),
                        rs.getDouble("harga_sewa_per_hari"),
                        rs.getBoolean("status"),
                        rs.getBytes("gambar")
                ));
            }
        }
        return mobilList;
    }

    public static List<Mobil> getAll(String keyword) throws SQLException {
        if (keyword == null || keyword.isEmpty()) {
            // Jika keyword kosong atau null, panggil getAll tanpa parameter
            return getAll();
        }

        List<Mobil> mobilList = new ArrayList<>();
        String query = "SELECT * FROM mobil WHERE "
                + "nama_mobil LIKE ? OR "
                + "nomor_polisi LIKE ? OR "
                + "tipe LIKE ? OR "
                + "harga_sewa_per_hari LIKE ? OR "
                + "status LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            // Menyiapkan parameter untuk mencari keyword di setiap field
            String searchKeyword = "%" + keyword + "%";
            stmt.setString(1, searchKeyword); // untuk nama_mobil
            stmt.setString(2, searchKeyword); // untuk nomor_polisi
            stmt.setString(3, searchKeyword); // untuk tipe
            stmt.setString(4, searchKeyword); // untuk harga_sewa_per_hari
            stmt.setString(5, searchKeyword); // untuk status

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    mobilList.add(new Mobil(
                            rs.getInt("id_mobil"),
                            rs.getString("nama_mobil"),
                            rs.getString("nomor_polisi"),
                            rs.getString("tipe"),
                            rs.getDouble("harga_sewa_per_hari"),
                            rs.getBoolean("status"),
                            rs.getBytes("gambar")
                    ));
                }
            }
        }
        return mobilList;
    }

    // Method untuk mengambil daftar mobil yang tersedia (status = true)
    public static List<Mobil> getAllAvailable() throws SQLException {
        List<Mobil> availableMobilList = new ArrayList<>();
        String query = "SELECT * FROM mobil WHERE status = true"; // Hanya ambil mobil dengan status true

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                availableMobilList.add(new Mobil(
                        rs.getInt("id_mobil"),
                        rs.getString("nama_mobil"),
                        rs.getString("nomor_polisi"),
                        rs.getString("tipe"),
                        rs.getDouble("harga_sewa_per_hari"),
                        rs.getBoolean("status"),
                        rs.getBytes("gambar")
                ));
            }
        }
        return availableMobilList;
    }

    // Method untuk mengupdate data mobil di database
    public boolean update() throws SQLException {
        String query = "UPDATE mobil SET nama_mobil = ?, nomor_polisi = ?, tipe = ?, harga_sewa_per_hari = ?, status = ?, gambar = ? WHERE id_mobil = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, namaMobil);
            stmt.setString(2, nomorPolisi);
            stmt.setString(3, tipe);
            stmt.setDouble(4, hargaSewaPerHari);
            stmt.setBoolean(5, status);
            stmt.setBytes(6, gambar);
            stmt.setInt(7, idMobil);  // ID mobil yang akan diupdate
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Method untuk menghapus mobil dari database
    public boolean delete() throws SQLException {
        String query = "DELETE FROM mobil WHERE id_mobil = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idMobil);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    @Override
    public String toString() {
        return (status ? "✅" : "❌") + " " + namaMobil + " - " + nomorPolisi + " - " + tipe + " - Rp. " + hargaSewaPerHari + " per Hari";
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Mobil && ((Mobil) other).idMobil == idMobil;
    }
}
