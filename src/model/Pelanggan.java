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

public record Pelanggan(
        int idPelanggan,
        String nama,
        String alamat,
        String nomorTelepon,
        String email) {

    // Constructor tambahan untuk pembuatan pelanggan baru tanpa ID
    public Pelanggan(String nama, String alamat, String nomorTelepon, String email) {
        this(0, nama, alamat, nomorTelepon, email);
    }

    // Method untuk memastikan tabel 'pelanggan' ada, jika tidak ada maka membuat tabel tersebut
    public static void createTableIfNotExists() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS pelanggan ("
                + "id_pelanggan INT AUTO_INCREMENT PRIMARY KEY, "
                + "nama VARCHAR(255) NOT NULL, "
                + "alamat VARCHAR(255) NOT NULL, "
                + "nomor_telepon VARCHAR(20), "
                + "email VARCHAR(100))";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
        }
    }

    // Method untuk menyimpan pelanggan ke database
    public boolean save() throws SQLException {
        String query = "INSERT INTO pelanggan (nama, alamat, nomor_telepon, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nama);
            stmt.setString(2, alamat);
            stmt.setString(3, nomorTelepon);
            stmt.setString(4, email);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Method untuk mengambil pelanggan berdasarkan id
    public static Pelanggan getById(int id) throws SQLException {
        String query = "SELECT * FROM pelanggan WHERE id_pelanggan = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Pelanggan(
                            rs.getInt("id_pelanggan"),
                            rs.getString("nama"),
                            rs.getString("alamat"),
                            rs.getString("nomor_telepon"),
                            rs.getString("email")
                    );
                }
            }
        }
        return null; // Return null if no pelanggan is found with the given ID
    }

    // Method untuk mengambil daftar pelanggan dari database
    public static List<Pelanggan> getAll() throws SQLException {
        List<Pelanggan> pelangganList = new ArrayList<>();
        String query = "SELECT * FROM pelanggan";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                pelangganList.add(new Pelanggan(
                        rs.getInt("id_pelanggan"),
                        rs.getString("nama"),
                        rs.getString("alamat"),
                        rs.getString("nomor_telepon"),
                        rs.getString("email")
                ));
            }
        }
        return pelangganList;
    }

    // Method untuk mengambil daftar pelanggan berdasarkan keyword pencarian
    public static List<Pelanggan> getAll(String keyword) throws SQLException {
        if (keyword == null || keyword.isEmpty()) {
            // Jika keyword kosong atau null, panggil getAll tanpa parameter
            return getAll();
        }

        List<Pelanggan> pelangganList = new ArrayList<>();
        String query = "SELECT * FROM pelanggan WHERE "
                + "nama LIKE ? OR "
                + "alamat LIKE ? OR "
                + "nomor_telepon LIKE ? OR "
                + "email LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            // Menyiapkan parameter untuk mencari keyword di setiap field
            String searchKeyword = "%" + keyword + "%";
            stmt.setString(1, searchKeyword); // untuk nama
            stmt.setString(2, searchKeyword); // untuk alamat
            stmt.setString(3, searchKeyword); // untuk nomor_telepon
            stmt.setString(4, searchKeyword); // untuk email

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pelangganList.add(new Pelanggan(
                            rs.getInt("id_pelanggan"),
                            rs.getString("nama"),
                            rs.getString("alamat"),
                            rs.getString("nomor_telepon"),
                            rs.getString("email")
                    ));
                }
            }
        }
        return pelangganList;
    }

    // Method untuk mengupdate data pelanggan
    public boolean update() throws SQLException {
        String query = "UPDATE pelanggan SET nama = ?, alamat = ?, nomor_telepon = ?, email = ? WHERE id_pelanggan = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nama);
            stmt.setString(2, alamat);
            stmt.setString(3, nomorTelepon);
            stmt.setString(4, email);
            stmt.setInt(5, idPelanggan);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Method untuk menghapus pelanggan
    public boolean delete() throws SQLException {
        String query = "DELETE FROM pelanggan WHERE id_pelanggan = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idPelanggan);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    @Override
    public String toString() {
        return nama + " (" + nomorTelepon + ", " + email + ")";
    }
}
