package io.aadesh.RentBook.controller;

import io.aadesh.RentBook.services.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @GetMapping("/getTenants")
    public String getTenants(Model model){
        model.addAttribute("tenants",tenantService.getAllTenants());
        return "tenants";
    }

    @GetMapping("/deleteTenant/{floor}")
    public String deleteTenant(@PathVariable int floor, Model model){
        tenantService.deActivateTenant(floor);
        return getTenants(model);
    }

    @GetMapping("/activateTenant/{floor}")
    public String activateTenant(@PathVariable int floor, Model model){
        tenantService.activateTenant(floor);
        return getTenants(model);
    }

}
