package io.aadesh.RentBook.services;

import io.aadesh.RentBook.entities.*;
import io.aadesh.RentBook.repos.ElectricityBillRepo;
import io.aadesh.RentBook.repos.TenantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillService {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private ElectricityBillRepo electricityBillRepo;


    public ElectricityBill createElectricityBill(double currentTotalRoomUnits,double currentTotalBorUnits, int floor, String month, String year, int rsPerUnit){
      Tenant tenant = tenantService.getTenantByFloor(floor);
        if(electricityBillRepo.findById(new ElectricityBillId(floor,month+"-"+year)).isPresent()){
            //todo:: Error handling for already existed data
            throw new RuntimeException("bill for that user already exists");
        }
        //tenantRepo.delete(tenant0.get());
        List<ElectricityBill> electricityBills = electricityBillRepo.findAll().stream().filter(bill -> bill.getId().getTenantId() == floor).toList();
        if(electricityBills.isEmpty()){
            //Todo::Handle no previous data
            throw new RuntimeException();
        }
        ElectricityBill previousElectricityBill = electricityBills.get(electricityBills.size()-1);



            double previousTotalRoomUnits = previousElectricityBill.getCurrentTotalRoomUnits();
            double previousTotalBorUnits = previousElectricityBill.getCurrentTotalBorUnits();

            double totalRoomUnits = currentTotalRoomUnits - previousTotalRoomUnits;
            double totalRoomAmount = totalRoomUnits * rsPerUnit;
            double roomBorUnits = (currentTotalBorUnits-previousTotalBorUnits)/tenantService.getAllActiveTenants().size();
            double totalBorAmount = roomBorUnits * rsPerUnit;

            int totalCalculatedAmount = (int)(totalBorAmount+totalRoomAmount);
            ElectricityBill electricityBill = new ElectricityBill();

            electricityBill.setMonth(month);
            electricityBill.setYear(year);
            electricityBill.setId(new ElectricityBillId(floor,month+"-"+year));
            electricityBill.setPerRs(rsPerUnit);
            electricityBill.setRoomBorUnits(Math.round(roomBorUnits * 10.0) / 10.0);
            electricityBill.setRoomBorAmount(Math.round(totalBorAmount * 10.0) / 10.0);
            electricityBill.setRoomUnits((Math.round(totalRoomUnits * 10.0) / 10.0));

            electricityBill.setPreviousTotalBorUnits((Math.round(previousTotalBorUnits * 10.0) / 10.0));
            electricityBill.setCurrentTotalBorUnits((Math.round(currentTotalBorUnits * 10.0) / 10.0));
            electricityBill.setPreviousTotalRoomUnits((Math.round(previousTotalRoomUnits * 10.0) / 10.0));
            electricityBill.setCurrentTotalRoomUnits((Math.round(currentTotalRoomUnits * 10.0) / 10.0));


            electricityBill.setRoomBillAmount((Math.round(totalRoomAmount * 10.0) / 10.0));
            electricityBill.setTotalBill(totalCalculatedAmount);
            electricityBill.setGrandTotal(totalCalculatedAmount+tenant.getRent());


            electricityBillRepo.save(electricityBill);
            return electricityBill;

    }

    public void createElectricityBillForFirstTime(double currentTotalRoomUnits, double previousTotalRoomUnits ,double currentTotalBorUnits,double previousTotalBorUnits, int floor, String month, String year, int rsPerUnit){
       Tenant tenant = tenantService.getTenantByFloor(floor);
        if(electricityBillRepo.findById(new ElectricityBillId(floor,month+"-"+year)).isPresent()){
            //todo:: Error handling for already existed data
            throw new RuntimeException("bill for that user already exists");
        }
        //tenantRepo.delete(tenant0.get());


        double totalRoomUnits = currentTotalRoomUnits - previousTotalRoomUnits;
        double totalRoomAmount = totalRoomUnits * rsPerUnit;
        double roomBorUnits = (currentTotalBorUnits-previousTotalBorUnits)/tenantService.getAllActiveTenants().size();
        double totalBorAmount = roomBorUnits * rsPerUnit;

        int totalCalculatedAmount = (int)(totalBorAmount+totalRoomAmount);
        ElectricityBill electricityBill = new ElectricityBill();

        electricityBill.setMonth(month);
        electricityBill.setYear(year);
        electricityBill.setId(new ElectricityBillId(floor,month+"-"+year));
        electricityBill.setPerRs(rsPerUnit);
        electricityBill.setRoomBorUnits(Math.round(roomBorUnits * 10.0) / 10.0);

        electricityBill.setPreviousTotalBorUnits((Math.round(previousTotalBorUnits * 10.0) / 10.0));
        electricityBill.setCurrentTotalBorUnits((Math.round(currentTotalBorUnits * 10.0) / 10.0));
        electricityBill.setPreviousTotalRoomUnits((Math.round(previousTotalRoomUnits * 10.0) / 10.0));
        electricityBill.setCurrentTotalRoomUnits((Math.round(currentTotalRoomUnits * 10.0) / 10.0));


        electricityBill.setRoomBorAmount(Math.round(totalBorAmount * 10.0) / 10.0);

        electricityBill.setRoomUnits(Math.round(totalRoomUnits * 10.0) / 10.0);
        electricityBill.setRoomBillAmount(Math.round(totalRoomAmount * 10.0) / 10.0);
        electricityBill.setTotalBill(totalCalculatedAmount);
        electricityBill.setGrandTotal(totalCalculatedAmount+tenant.getRent());


        electricityBillRepo.save(electricityBill);


    }


    public void deleteBill(ElectricityBillId electricityBillId) {
        Optional<ElectricityBill> bill0 = electricityBillRepo.findById(electricityBillId);
        if(bill0.isEmpty()){
            throw new RuntimeException("Bill not found");
        }

        electricityBillRepo.delete(bill0.get());
    }

    public ElectricityBill getBillById(ElectricityBillId electricityBillId) {
        Optional<ElectricityBill> bill0 = electricityBillRepo.findById(electricityBillId);
        if(bill0.isEmpty()){
            throw new RuntimeException("Bill not found");
        }
        return bill0.get();
    }

    public void editBill(double currentTotalRoomUnits,double currentTotalBorUnits, int floor, String month, String year, int rsPerUnit) {
        Tenant tenant = tenantService.getTenantByFloor(floor);
        Optional<ElectricityBill> bill = electricityBillRepo.findById(new ElectricityBillId(floor,month+"-"+year));
        if(bill.isEmpty()){
            //todo:: Error handling for does not exist
            throw new RuntimeException("Bill Does not exists");
        }
        //tenantRepo.delete(tenant0.get());




        double previousTotalRoomUnits = bill.get().getPreviousTotalRoomUnits();
        double previousTotalBorUnits = bill.get().getPreviousTotalBorUnits();

        double totalRoomUnits = currentTotalRoomUnits - previousTotalRoomUnits;
        double totalRoomAmount = totalRoomUnits * rsPerUnit;
        double roomBorUnits = (currentTotalBorUnits-previousTotalBorUnits)/tenantService.getAllActiveTenants().size();
        double totalBorAmount = roomBorUnits * rsPerUnit;

        int totalCalculatedAmount = (int)(totalBorAmount+totalRoomAmount);
        ElectricityBill electricityBill = new ElectricityBill();

        electricityBill.setMonth(month);
        electricityBill.setYear(year);
        electricityBill.setId(new ElectricityBillId(floor,month+"-"+year));
        electricityBill.setPerRs(rsPerUnit);
        electricityBill.setRoomBorUnits(Math.round(roomBorUnits * 10.0) / 10.0);
        electricityBill.setRoomBorAmount(Math.round(totalBorAmount * 10.0) / 10.0);
        electricityBill.setRoomUnits((Math.round(totalRoomUnits * 10.0) / 10.0));

        electricityBill.setPreviousTotalBorUnits((Math.round(previousTotalBorUnits * 10.0) / 10.0));
        electricityBill.setCurrentTotalBorUnits((Math.round(currentTotalBorUnits * 10.0) / 10.0));
        electricityBill.setPreviousTotalRoomUnits((Math.round(previousTotalRoomUnits * 10.0) / 10.0));
        electricityBill.setCurrentTotalRoomUnits((Math.round(currentTotalRoomUnits * 10.0) / 10.0));


        electricityBill.setRoomBillAmount((Math.round(totalRoomAmount * 10.0) / 10.0));
        electricityBill.setTotalBill(totalCalculatedAmount);
        electricityBill.setGrandTotal(totalCalculatedAmount+tenant.getRent());


        electricityBillRepo.save(electricityBill);

    }
}
