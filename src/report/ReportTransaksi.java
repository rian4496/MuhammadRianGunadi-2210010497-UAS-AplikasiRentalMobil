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
 * @author Muhammad Syaiful
 */
public class ReportTransaksi {

     public static void main(String[] args) {
        try {
            ReportTransaksi.generate("LaporanTransaksi.pdf");
            System.out.println("Laporan berhasil dibuat.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Method untuk membuat PDF laporan transaksi dengan data dari database
    public static void generate(String fileName) throws DocumentException, IOException, SQLException {
        // Ambil data transaksi dari database
        List<Transaksi> transaksiList = Transaksi.getAll();

        try (Document document = new Document()) {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // Judul laporan
            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("Laporan Transaksi", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n")); // Spasi setelah judul

            // Buat tabel
            PdfPTable table = new PdfPTable(5); // 5 kolom
            table.setWidths(new float[]{1f, 2f, 2f, 2f, 2f}); // Set lebar kolom

            // Header tabel
            Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            addHeaderCell(table, "No", headerFont);
            addHeaderCell(table, "ID Transaksi", headerFont);
            addHeaderCell(table, "Tanggal Sewa", headerFont);
            addHeaderCell(table, "Tanggal Kembali", headerFont);
            addHeaderCell(table, "Total Biaya", headerFont);

            // Data transaksi
            int nomor = 1;
            double totalKeseluruhan = 0;

            for (Transaksi transaksi : transaksiList) {
                table.addCell(createCell(String.valueOf(nomor++), PdfPCell.ALIGN_CENTER, 8));
                table.addCell(createCell(String.valueOf(transaksi.idTransaksi()), PdfPCell.ALIGN_CENTER, 8));
                table.addCell(createCell(transaksi.tanggalSewa().toString(), PdfPCell.ALIGN_CENTER, 8));
                table.addCell(createCell(transaksi.tanggalKembali().toString(), PdfPCell.ALIGN_CENTER, 8));
                table.addCell(createCell(formatCurrency(transaksi.totalBiaya()), PdfPCell.ALIGN_CENTER, 8));

                totalKeseluruhan += transaksi.totalBiaya();
            }

            // Tambahkan baris total keseluruhan
            PdfPCell totalLabelCell = new PdfPCell(new Phrase("Total Keseluruhan", headerFont));
            totalLabelCell.setColspan(4);
            totalLabelCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            totalLabelCell.setPadding(8);

            PdfPCell totalValueCell = new PdfPCell(new Phrase(formatCurrency(totalKeseluruhan), headerFont));
            totalValueCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            table.addCell(totalLabelCell);
            table.addCell(totalValueCell);

            // Tambahkan tabel ke dokumen
            document.add(table);
        }
    }

    // Method helper untuk membuat cell
    private static PdfPCell createCell(String content, int alignment, int padding) {
        PdfPCell cell = new PdfPCell(new Phrase(content));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(padding);
        return cell;
    }

    // Method helper untuk menambahkan header ke tabel
    private static void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setPadding(12);
        table.addCell(cell);
    }

    // Format angka menjadi mata uang
    private static String formatCurrency(double amount) {
        return NumberFormat.getCurrencyInstance().format(amount);
    }
}
