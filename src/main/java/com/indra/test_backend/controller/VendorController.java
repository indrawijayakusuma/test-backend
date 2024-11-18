package com.indra.test_backend.controller;

import com.indra.test_backend.entity.Vendor;
import com.indra.test_backend.entity.dto.VendorCreateDto;
import com.indra.test_backend.service.VendorService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    private final Bucket bucket;

    public VendorController() {
        Bandwidth limit = Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @GetMapping("/vendors")
    public ResponseEntity<List<Vendor>> getAll(){
        return ResponseEntity.ok().body(vendorService.findAll());
    }

    @GetMapping("/vendors/{id}")
    public ResponseEntity<Vendor> getVendorById(@PathVariable Integer id){
        return ResponseEntity.ok().body(vendorService.findById(id));
    }
    @PostMapping("/vendors")
    public ResponseEntity<Vendor> create(@Valid @RequestBody VendorCreateDto vendorDto){
        if (bucket.tryConsume(1)){
            return ResponseEntity.ok().body(vendorService.create(vendorDto));
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @PutMapping("/vendors")
    public ResponseEntity<Vendor> update(@RequestBody Vendor vendor){
        if (bucket.tryConsume(1)){
            return ResponseEntity.ok().body(vendorService.update(vendor));
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @DeleteMapping("/vendors/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        vendorService.delete(id);
        return ResponseEntity.ok("vendor telah dihapus");
    }
}
