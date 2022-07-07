/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author LENOVO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    private Long id;
    private String noOrder;
    private Double progress = 0.0;
    private LocalDateTime tanggalMasuk;
    private LocalDateTime tanggalSelesai;
    private String lokasiBarang;
    private String statusPesanan;
    private User user;
    private Pembayaran pembayaran;
    private List<JenisJahitanOrder> jenisJahitanOrder;
}
