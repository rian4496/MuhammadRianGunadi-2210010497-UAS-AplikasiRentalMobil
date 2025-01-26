# PBO2-UAS-AplikasiRentalMobil
 ![gambar](https://github.com/user-attachments/assets/f3eea630-1d31-4e8e-9854-59b0ffb25e71)
![gambar](https://github.com/user-attachments/assets/68911714-6e76-4775-940e-df80d4004a48)
![gambar](https://github.com/user-attachments/assets/3f100f8e-47de-414d-af0a-9d695ed61d37)
![gambar](https://github.com/user-attachments/assets/1a41175e-2a31-43b0-bf09-6560ca1a2170)

# Aplikasi Rental Mobil

Aplikasi ini merupakan sebuah sistem informasi sederhana untuk mengelola rental mobil yang dikembangkan menggunakan **NetBeans IDE** dan bahasa pemrograman **Java**. Aplikasi ini dirancang untuk memudahkan pengelolaan data penyewaan mobil, data pelanggan, dan laporan transaksi.

## Fitur Utama

1. **Pengelolaan Data Mobil**
   - Tambah, edit, hapus, dan lihat daftar mobil yang tersedia.
   - Informasi meliputi: plat nomor, merek, jenis, harga sewa, dan status ketersediaan.

2. **Pengelolaan Data Pelanggan**
   - Tambah, edit, hapus, dan lihat data pelanggan.
   - Informasi meliputi: nama pelanggan, kontak, dan identitas.

3. **Transaksi Penyewaan**
   - Proses penyewaan mobil dengan input data pelanggan, mobil yang disewa, tanggal sewa, dan tanggal kembali.
   - Perhitungan total biaya sewa otomatis berdasarkan lama penyewaan.

4. **Laporan**
   - Menampilkan riwayat transaksi penyewaan.
   - Pencarian laporan berdasarkan periode waktu tertentu.

## Teknologi yang Digunakan

- **Java**: Bahasa pemrograman utama untuk logika aplikasi.
- **NetBeans IDE**: Alat pengembangan untuk membangun aplikasi ini.
- **MySQL**: Basis data untuk menyimpan informasi mobil, pelanggan, dan transaksi.
- **JDBC**: Untuk menghubungkan aplikasi dengan database MySQL.

## Cara Menjalankan Aplikasi

1. **Persiapan Lingkungan**:
   - Pastikan **Java Development Kit (JDK)** telah terinstal di komputer.
   - Instal **NetBeans IDE** jika belum tersedia.
   - Instal dan konfigurasi **MySQL** sebagai database.

2. **Clone Repository**:
   ```bash
   git clone https://github.com/rian4496/MuhammadRianGunadi-2210010497-UAS-AplikasiRentalMobil.git
   ```
3. **Konfigurasi Database**:
   - Impor file SQL yang terdapat di folder `database` ke MySQL.
   - Perbarui konfigurasi koneksi database di file `DatabaseConnection.java` sesuai dengan kredensial lokal Anda.

4. **Jalankan Aplikasi**:
   - Buka proyek di NetBeans.
   - Klik tombol `Run` atau tekan **F6** untuk menjalankan aplikasi.

## Struktur Proyek

```
├── src
│   ├── model
│   ├── view
│   ├── controller
├── database
│   └── rental_mobil.sql
└── README.md
```

- **src**: Berisi kode sumber aplikasi.
  - `model`: Kelas untuk representasi data (Model).
  - `view`: Kelas untuk antarmuka pengguna (GUI).
  - `controller`: Kelas untuk logika dan alur aplikasi.
- **database**: Berisi file SQL untuk membuat tabel yang dibutuhkan aplikasi.
- **README.md**: Dokumentasi proyek.


