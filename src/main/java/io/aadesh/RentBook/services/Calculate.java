package io.aadesh.RentBook.services;

import io.aadesh.RentBook.entities.*;
import io.aadesh.RentBook.repos.ElectricityBillRepo;
import io.aadesh.RentBook.repos.TenantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class Calculate {

    private List<String> months;

    Calculate(){
        months =  List.of("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
    }
    @Autowired
    private TenantRepo tenantRepo;
    @Autowired
    private ElectricityBillRepo electricityBillRepo;


    public void createElectricityBill(double currentTotalRoomUnits,double currentTotalBorUnits, int floor, String month, String year, int rsPerUnit){
        Optional<Tenant> tenant0 = tenantRepo.findById(floor);

        if(tenant0.isEmpty()){
            //Todo::send to add new tenants
            System.out.println("Error");

            throw new RuntimeException();

        }
        //tenantRepo.delete(tenant0.get());
        Tenant tenant = tenant0.get();
            String previousMonthYear = getPreviousMonthYear(month,year);
            Optional<ElectricityBill> previousElectricityBill =  electricityBillRepo.findById(new ElectricityBillId(floor,previousMonthYear));
            if(previousElectricityBill.isEmpty()){
                //Todo:: do error handling
                throw new RuntimeException();
            }
//            Optional<Tenant> tenant = previousElectricityBill.get().getTenants().stream().filter(tenant1 -> tenant1.getFloor() == floor )
//                .findFirst();


//            if(tenant.isEmpty()){
//                //Todo:: do error handling
//                throw new RuntimeException();
//            }

            double previousTotalRoomUnits = previousElectricityBill.get().getCurrentTotalRoomUnits();
            double previousTotalBorUnits = previousElectricityBill.get().getCurrentTotalBorUnits();

            double totalRoomUnits = currentTotalRoomUnits - previousTotalRoomUnits;
            double totalRoomAmount = totalRoomUnits * rsPerUnit;
            double roomBorUnits = (currentTotalBorUnits-previousTotalBorUnits)/tenantRepo.findAll().size();
            double totalBorAmount = roomBorUnits * rsPerUnit;

            int totalCalculatedAmount = (int)(totalBorAmount+totalRoomAmount);
            ElectricityBill electricityBill = new ElectricityBill();

            electricityBill.setMonth(month);
            electricityBill.setYear(year);
            electricityBill.setId(new ElectricityBillId(floor,month+"-"+year));
            electricityBill.setPerRs(rsPerUnit);
            electricityBill.setRoomBorUnits(roomBorUnits);
            electricityBill.setRoomBorAmount(totalBorAmount);
            electricityBill.setRoomUnits(totalRoomUnits);

            electricityBill.setPreviousTotalBorUnits(previousTotalBorUnits);
            electricityBill.setCurrentTotalBorUnits(currentTotalBorUnits);
            electricityBill.setPreviousTotalRoomUnits(previousTotalRoomUnits);
            electricityBill.setCurrentTotalRoomUnits(currentTotalRoomUnits);


            electricityBill.setRoomBillAmount(totalRoomAmount);
            electricityBill.setTotalBill(totalCalculatedAmount);
            electricityBill.setGrandTotal(totalCalculatedAmount+tenant.getRent());

            electricityBill.setTenant(tenant);

            electricityBillRepo.save(electricityBill);

    }

    public void createElectricityBillForFirstTime(double currentTotalRoomUnits, double previousTotalRoomUnits ,double currentTotalBorUnits,double previousTotalBorUnits, int floor, String month, String year, int rsPerUnit){
        Optional<Tenant> tenant0 = tenantRepo.findById(floor);

        if(tenant0.isEmpty()){
            //Todo::send to add new tenants
            System.out.println("Error");

            throw new RuntimeException();

        }
        if(electricityBillRepo.findById(new ElectricityBillId(floor,month+"-"+year)).isPresent()){
            //todo:: Error handling for already existed data
        }
        //tenantRepo.delete(tenant0.get());
        Tenant tenant = tenant0.get();


        double totalRoomUnits = currentTotalRoomUnits - previousTotalRoomUnits;
        double totalRoomAmount = totalRoomUnits * rsPerUnit;
        double roomBorUnits = (currentTotalBorUnits-previousTotalBorUnits)/tenantRepo.findAll().size();
        double totalBorAmount = roomBorUnits * rsPerUnit;

        int totalCalculatedAmount = (int)(totalBorAmount+totalRoomAmount);
        ElectricityBill electricityBill = new ElectricityBill();

        electricityBill.setMonth(month);
        electricityBill.setYear(year);
        electricityBill.setId(new ElectricityBillId(floor,month+"-"+year));
        electricityBill.setPerRs(rsPerUnit);
        electricityBill.setRoomBorUnits(roomBorUnits);

        electricityBill.setPreviousTotalBorUnits(previousTotalBorUnits);
        electricityBill.setCurrentTotalBorUnits(currentTotalBorUnits);
        electricityBill.setPreviousTotalRoomUnits(previousTotalRoomUnits);
        electricityBill.setCurrentTotalRoomUnits(currentTotalRoomUnits);
        electricityBill.setRoomBorAmount(totalBorAmount);

        electricityBill.setRoomUnits(totalRoomUnits);
        electricityBill.setRoomBillAmount(totalRoomAmount);
        electricityBill.setTotalBill(totalCalculatedAmount);
        electricityBill.setGrandTotal(totalCalculatedAmount+tenant.getRent());

        electricityBill.setTenant(tenant);

        electricityBillRepo.save(electricityBill);


    }

    public void addTenant(int rent,String name, int floor){
        Optional<Tenant> tenant0 = tenantRepo.findById(floor);

        if(tenant0.isEmpty()){
            //Todo::send to add new tenants
            System.out.println("Error");

            throw new RuntimeException();

        }
        Tenant tenant = new Tenant();

        tenant.setRent(rent);
        tenant.setName(name);
        tenant.setFloor(floor);
        tenantRepo.save(tenant);

    }



    private String getPreviousMonthYear(String month,String year){
       int prevIndex =  (months.indexOf(month) - 1) % months.size() ;
       String prevMonth = months.get(prevIndex);

       return  prevMonth+"-"+year;
    }
}
