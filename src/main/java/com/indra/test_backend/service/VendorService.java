package com.indra.test_backend.service;

import com.indra.test_backend.entity.Vendor;
import com.indra.test_backend.entity.dto.VendorCreateDto;
import com.indra.test_backend.repository.VendorRepository;
import com.indra.test_backend.respon.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VendorService {
    @Autowired
    private VendorRepository vendorRepository;

    public Vendor findById(Integer id){
        return vendorRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("category id = " + id + " tidak ditemukan"));
    }

    public List<Vendor> findAll(){
        List<Vendor> listVendor = vendorRepository.findAll();
        return listVendor;
    }

    public Vendor create(VendorCreateDto vendorDto){
        Vendor vendor = new Vendor();
        vendor.setName(vendorDto.getName());
        return vendorRepository.save(vendor);
    }

    public Vendor update(Vendor vendor){
        findById(vendor.getId());
        return vendorRepository.save(vendor);
    }

    public void delete(Integer id){
       findById(id);
        vendorRepository.deleteById(id);
    }

}
