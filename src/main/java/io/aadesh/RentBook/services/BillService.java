package io.aadesh.RentBook.services;

import io.aadesh.RentBook.entities.*;
import io.aadesh.RentBook.exceptions.BillAlreadyExistsException;
import io.aadesh.RentBook.exceptions.BillNotFoundException;
import io.aadesh.RentBook.repos.ElectricityBillRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class BillService {

    private Map<String,Integer> months;
    private Map<Integer,String> monthsN;

    public BillService(){
       months = Map.ofEntries(
                Map.entry("January", 1),
                Map.entry("February", 2),
                Map.entry("March", 3),
                Map.entry("April", 4),
                Map.entry("May", 5),
                Map.entry("June", 6),
                Map.entry("July", 7),
                Map.entry("August", 8),
                Map.entry("September", 9),
                Map.entry("October", 10),
                Map.entry("November", 11),
                Map.entry("December", 12)
        );
        monthsN = Map.ofEntries(
                Map.entry(1, "January"),
                Map.entry(2, "February"),
                Map.entry(3, "March"),
                Map.entry(4, "April"),
                Map.entry(5, "May"),
                Map.entry(6, "June"),
                Map.entry(7, "July"),
                Map.entry(8, "August"),
                Map.entry(9, "September"),
                Map.entry(10, "October"),
                Map.entry(11, "November"),
                Map.entry(12, "December")
        );

    }

    @Autowired
    private TenantService tenantService;

    @Autowired
    private ElectricityBillRepo electricityBillRepo;


    public ElectricityBill createElectricityBill(double currentTotalRoomUnits,double currentTotalBorUnits, int floor, String month, String year, int rsPerUnit) throws BillAlreadyExistsException {
      Tenant tenant = tenantService.getTenantByFloor(floor);
        if(electricityBillRepo.findById(new ElectricityBillId(floor,month+"-"+year)).isPresent()){
            throw new BillAlreadyExistsException();
        }
        //List<ElectricityBill> electricityBills = electricityBillRepo.findAll().stream().filter(bill -> bill.getId().getTenantId() == floor).toList();
        ElectricityBillId previousElectricityBillId = getPreviousMonthYearId(month,year,floor);

        ElectricityBill previousElectricityBill = getBillById(previousElectricityBillId);




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
            electricityBillRepo.flush();
            return electricityBill;

    }

    private ElectricityBillId getPreviousMonthYearId(String month, String year, int floor) {

        // Convert month name to LocalDate object
        int prevYear = months.get(month) == 1 ? Integer.parseInt(year) - 1 : Integer.parseInt(year);
        int prevMonth =  months.get(month) == 1 ? 12 :  months.get(month) - 1;


        return new ElectricityBillId(floor,monthsN.get(prevMonth)+"-"+prevYear);
    }


    public void createElectricityBillForFirstTime(double currentTotalRoomUnits, double previousTotalRoomUnits ,double currentTotalBorUnits,double previousTotalBorUnits, int floor, String month, String year, int rsPerUnit)  {
       Tenant tenant = tenantService.getTenantByFloor(floor);
        if(electricityBillRepo.findById(new ElectricityBillId(floor,month+"-"+year)).isPresent()){
            
            throw new BillAlreadyExistsException();
        }
        


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


    public void deleteBill(ElectricityBillId electricityBillId){
        Optional<ElectricityBill> bill0 = electricityBillRepo.findById(electricityBillId);
        if(bill0.isEmpty()){
            throw new BillNotFoundException();
        }

        electricityBillRepo.delete(bill0.get());
    }

    public ElectricityBill getBillById(ElectricityBillId electricityBillId){
        Optional<ElectricityBill> bill0 = electricityBillRepo.findById(electricityBillId);
        if(bill0.isEmpty()){
            throw new BillNotFoundException();
        }
        return bill0.get();
    }

    public void editBill(double currentTotalRoomUnits,double currentTotalBorUnits, int floor, String month, String year, int rsPerUnit) throws BillNotFoundException {
        Tenant tenant = tenantService.getTenantByFloor(floor);
        Optional<ElectricityBill> bill = electricityBillRepo.findById(new ElectricityBillId(floor,month+"-"+year));
        if(bill.isEmpty()){
            
            throw new BillNotFoundException();
        }
        




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
        electricityBillRepo.flush();

    }
}
