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
public class DisplayResults {

    @Autowired
    ElectricityBillRepo electricityBillRepo;
    @Autowired
    TenantRepo tenantRepo;

    public ElectricityBill getTenantBillByMonthYear(int floor, String month, String year){

        Optional<ElectricityBill> bill = electricityBillRepo.findById(new ElectricityBillId(floor,month+"-"+year));

        if(bill.isEmpty()){
            //Todo: do error handling
        }
        return bill.get();

    }
    public List<ElectricityBill> getTenantAllBills(int floor){
        return  electricityBillRepo.findAll().stream().filter(bill->bill.getTenant().getFloor()==floor).toList();

    }
    public List<ElectricityBill> getTenantLastNBills(int floor,int n){
        List<ElectricityBill> all = electricityBillRepo.findAll();
        if(n> all.size()){
            //todo error handling
        }
        return  all.stream().filter(bill->bill.getTenant().getFloor()==floor).limit(n).toList();

    }
    public List<ElectricityBill> getLatestBillForAllTenants(){
        List<Tenant> allTenants = tenantRepo.findAll();

        return allTenants.stream().map(tenant -> getTenantsLatestBill(tenant.getFloor())).toList();
    }
    public ElectricityBill getTenantsLatestBill(int floor){
        List<ElectricityBill> electricityBills = electricityBillRepo.findAll().stream().filter(bill -> bill.getTenant().getFloor() == floor).toList();
        if(electricityBills.isEmpty()){
            //Todo::Handle
            System.out.println("This one empty");
            return null;
        }else{
            return electricityBills.get(electricityBills.size()-1) ;
        }

    }

}
