/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author Hadi PC
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Koneksi database statis
    private static final String URL = "jdbc:mysql://localhost:3306/rental_mobil";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Method untuk mendapatkan koneksi ke database
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Gagal menghubungkan ke database: " + e.getMessage());
            throw e;
        }
    }
}
