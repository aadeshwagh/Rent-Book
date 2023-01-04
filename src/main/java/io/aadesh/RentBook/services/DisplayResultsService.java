package io.aadesh.RentBook.services;


import io.aadesh.RentBook.entities.ElectricityBill;
import io.aadesh.RentBook.entities.ElectricityBillId;
import io.aadesh.RentBook.entities.Tenant;
import io.aadesh.RentBook.repos.ElectricityBillRepo;
import io.aadesh.RentBook.repos.TenantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisplayResultsService {

    @Autowired
    ElectricityBillRepo electricityBillRepo;
    @Autowired
    TenantRepo tenantRepo;

    public ElectricityBill getTenantBillByMonthYear(int floor, String month, String year){

        Optional<ElectricityBill> bill = electricityBillRepo.findById(new ElectricityBillId(floor,month+"-"+year));

        if(bill.isEmpty()){
            
        }
        return bill.get();

    }
    public List<ElectricityBill> getTenantAllBills(int floor){
        return  electricityBillRepo.findAll().stream().filter(bill->bill.getId().getTenantId()==floor).toList();

    }
    public List<ElectricityBill> getTenantLastNBills(int floor,int n){
        List<ElectricityBill> all = electricityBillRepo.findAll();
        if(n> all.size()){
            
        }
        return  all.stream().filter(bill->bill.getId().getTenantId()==floor).limit(n).toList();

    }
    public List<ElectricityBill> getLatestBillForAllTenants(){
        List<Tenant> allTenants = tenantRepo.findAll();

        return allTenants.stream().map(tenant -> getTenantsLatestBill(tenant.getFloor())).toList();
    }
    public ElectricityBill getTenantsLatestBill(int floor){
        List<ElectricityBill> electricityBills = electricityBillRepo.findAll().stream().filter(bill -> bill.getId().getTenantId() == floor).toList();
        if(electricityBills.isEmpty()){
            
            System.out.println("This one empty");
            return null;
        }else{
            return electricityBills.get(0) ;
        }

    }

}
