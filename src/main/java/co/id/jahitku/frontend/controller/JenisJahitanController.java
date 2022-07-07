/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.jahitku.frontend.controller;

import co.id.jahitku.frontend.model.JenisJahitan;
import co.id.jahitku.frontend.model.dto.ResponseData;
import co.id.jahitku.frontend.service.JenisJahitanService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author LENOVO
 */
@Controller
@RequestMapping("/jenisjahitan")
public class JenisJahitanController {

    private JenisJahitanService jenisJahitanService;

    @Autowired
    public JenisJahitanController(JenisJahitanService jenisJahitanService) {
        this.jenisJahitanService = jenisJahitanService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String getJenisJahitan() {
        return "jenisjahitan/index";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/get-all")
    public @ResponseBody
    ResponseEntity<List<JenisJahitan>> getAllJenisJahitan() {
        return ResponseEntity.ok(jenisJahitanService.getAll());
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<JenisJahitan> getById(@PathVariable Long id) {
        return ResponseEntity.ok(jenisJahitanService.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public @ResponseBody
    ResponseEntity create(@Valid @RequestBody JenisJahitan jenisJahitan,
            BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        jenisJahitanService.create(jenisJahitan);
        return ResponseEntity.ok(new ResponseData("success", "Jenis Jahitan Created"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public @ResponseBody
    ResponseEntity update(@Valid @RequestBody JenisJahitan jenisJahitan, @PathVariable("id") Long id,
            BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        jenisJahitanService.update(id, jenisJahitan);
        return ResponseEntity.ok(new ResponseData("success", "update success"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public @ResponseBody
    ResponseEntity deleteJenisJahitan(@PathVariable("id") Long id) {

        jenisJahitanService.delete(id);
        return ResponseEntity.ok(new ResponseData("success", "JenisJahitan deleted"));
    }
}
