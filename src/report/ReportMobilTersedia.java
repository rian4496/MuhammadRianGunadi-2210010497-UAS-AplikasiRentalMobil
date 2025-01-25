/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package report;

import model.Mobil;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;

/**
 *
 * @author HADI PC
 */
public class ReportMobilTersedia {

    // Method untuk membuat laporan PDF mobil tersedia
    public static void generate(String fileName) throws DocumentException, IOException, SQLException {
        // Ambil data mobil yang tersedia
        List<Mobil> availableCars = Mobil.getAllAvailable();

        try ( // Buat dokumen PDF baru
                Document document = new Document()) {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // Tambahkan judul
            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("Laporan Mobil Tersedia", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n")); // Spasi kosong

            // Buat tabel
            PdfPTable table = new PdfPTable(3); // 3 kolom: Gambar, Nama & Nomor Polisi, Harga Sewa
            table.setWidths(new float[]{2f, 2.5f, 2.5f}); // Lebar relatif kolom

            // Tambahkan header tabel
            Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            table.addCell(createHeaderCell("Gambar", headerFont));
            table.addCell(createHeaderCell("Nama & Nomor Polisi", headerFont));
            table.addCell(createHeaderCell("Harga Sewa/Hari", headerFont));

            // Tambahkan data ke tabel
            for (Mobil mobil : availableCars) {
                // Gambar
                Image img = Image.getInstance(mobil.gambar());
                PdfPCell imgCell = new PdfPCell(img, true);
                imgCell.setPadding(4);
                imgCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(imgCell);

                // Nama & Nomor Polisi
                String nameAndPlate = mobil.namaMobil() + "\n" + mobil.nomorPolisi();
                table.addCell(createDataCell(nameAndPlate, PdfPCell.ALIGN_RIGHT));

                // Harga sewa per hari
                table.addCell(createDataCell(formatCurrency(mobil.hargaSewaPerHari()), PdfPCell.ALIGN_RIGHT));
            }

            // Tambahkan tabel ke dokumen
            document.add(table);
        }
    }

    // Helper untuk membuat cell header
    private static PdfPCell createHeaderCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPadding(12);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        return cell;
    }

    // Helper untuk membuat cell data
    private static PdfPCell createDataCell(String content, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(content));
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPadding(8);
        return cell;
    }

    // Formatkan angka ke format mata uang
    private static String formatCurrency(double amount) {
        return NumberFormat.getCurrencyInstance().format(amount);
    }
}
