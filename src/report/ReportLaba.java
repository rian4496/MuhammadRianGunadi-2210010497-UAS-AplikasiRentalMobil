/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package report;

import model.Transaksi;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;

/**
 *
 * @author HADI PC
 */
public class ReportLaba {

    // Method untuk membuat PDF laporan penghasilan dengan data dari database
    public static void generate(String fileName) throws DocumentException, IOException, SQLException {
        // Ambil data transaksi tahun ini
        List<Transaksi> transaksiList = Transaksi.getAllThisYear();

        try ( // Buat dokumen PDF baru
                Document document = new Document()) {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            // Judul laporan
            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("Laporan Laba Tahun Ini", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));  // Memberikan spasi
            // Buat tabel
            PdfPTable table = new PdfPTable(4);  // 4 kolom
            table.setWidths(new float[]{0.75f, 1f, 1.5f, 2f});  // Menentukan lebar kolom
            // Set header tabel
            Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            addHeaderCell(table, "No", headerFont);
            addHeaderCell(table, "Bulan", headerFont);
            addHeaderCell(table, "Tanggal", headerFont);
            addHeaderCell(table, "Penghasilan", headerFont);
            // Variabel untuk total penghasilan tahun ini
            double totalPenghasilan = 0;
            // Menyimpan data transaksi bulan demi bulan
            int nomor = 1;
            String bulan = "";
            double totalBulan = 0;
            for (Transaksi transaksi : transaksiList) {
                // Ambil bulan dan total biaya dari transaksi
                String transaksiBulan = transaksi.tanggalSewa().getMonth().toString();  // Bulan dari transaksi
                double totalBiaya = transaksi.totalBiaya();
                
                // Jika bulan berubah, tampilkan total untuk bulan sebelumnya
                if (!bulan.equals(transaksiBulan) && !bulan.isEmpty()) {
                    // Gabungkan dua kolom untuk total bulan
                    addTotalRow(table, "Total " + bulan, totalBulan, headerFont);
                    
                    totalPenghasilan += totalBulan;
                    totalBulan = 0;  // Reset total bulan
                }
                
                // Tambahkan baris untuk transaksi ini
                table.addCell(createCell(String.format("%02d", nomor++), PdfPCell.ALIGN_CENTER, 4)); // Nomor
                table.addCell(createCell(transaksiBulan, PdfPCell.ALIGN_CENTER, 4)); // Bulan
                table.addCell(createCell(transaksi.tanggalSewa().toString(), PdfPCell.ALIGN_CENTER, 4)); // Tanggal
                table.addCell(createCell(formatCurrency(totalBiaya), PdfPCell.ALIGN_CENTER, 4)); // Penghasilan
                
                totalBulan += totalBiaya;  // Tambah total bulan
                bulan = transaksiBulan;  // Set bulan saat ini
            }   // Tampilkan total untuk bulan terakhir
            if (totalBulan > 0) {
                addTotalRow(table, "Total " + bulan, totalBulan, headerFont);
                totalPenghasilan += totalBulan;
            }   // Baris kosong
            PdfPCell blankCell = new PdfPCell();
            blankCell.setColspan(4);
            table.addCell(blankCell);
            // Total keseluruhan
            addTotalRow(table, "Total Laba", totalPenghasilan, headerFont);
            // Tambahkan tabel ke dalam dokumen
            document.add(table);
            // Tutup dokumen
        }
    }

    // Method helper untuk menambahkan cell dengan padding dan alignment
    private static PdfPCell createCell(String content, int alignment, int padding) {
        PdfPCell cell = new PdfPCell(new Phrase(content));
        cell.setPadding(padding);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    // Menambahkan header ke dalam tabel
    private static void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(12);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // Rata tengah
        table.addCell(cell);
    }

    // Menambahkan baris total untuk bulan atau keseluruhan
    private static void addTotalRow(PdfPTable table, String label, double total, Font font) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
        labelCell.setColspan(2); // Gabungkan 2 kolom untuk total
        labelCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        labelCell.setPadding(8);

        PdfPCell totalCell = new PdfPCell(new Phrase(formatCurrency(total)));
        totalCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        totalCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

        table.addCell("");  // Kolom nomor kosong
        table.addCell(labelCell);  // Label total
        table.addCell(totalCell);  // Penghasilan
    }

    // Formatkan angka ke format mata uang
    private static String formatCurrency(double amount) {
        return NumberFormat.getCurrencyInstance().format(amount);
    }
}
