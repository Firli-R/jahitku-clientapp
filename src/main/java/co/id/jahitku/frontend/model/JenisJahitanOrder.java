/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author LENOVO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JenisJahitanOrder {
    private JenisJahitanOrderKey id;
    private JenisJahitan jenisJahitan;
    private Long kuantitas;
    private Long harga;
    private String keterangan;
    private Boolean status;
}
