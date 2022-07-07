/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Firli
 */
@Data
public class JenisOrderData {
    private LocalDateTime tanggalMasuk;
    private LocalDateTime tanggalSelesai;
    private String statusPesanan;
    private String nama;
    private String username;
    private String lokasiBarang;
    private String statusPembayaran;
    private Long totalBiaya;
    private List<Jjod> jenisJahitanOrder;
}
