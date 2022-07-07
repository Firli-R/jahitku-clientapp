/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.model.dto;

import co.id.jahitku.frontend.model.JenisJahitanOrder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Firli
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderData {
//    private Long id;

    private LocalDateTime tanggalMasuk;
    private LocalDateTime tanggalSelesai;
    private String statusPesanan;
    private String nama;
    private String username;
    private String lokasiBarang;
    private String statusPembayaran;
    private Long totalBiaya;
    private List<JenisJahitanOrderData> jenisJahitanOrder;
}
