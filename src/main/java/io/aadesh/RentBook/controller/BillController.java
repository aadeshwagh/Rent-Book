package io.aadesh.RentBook.controller;

import io.aadesh.RentBook.entities.ElectricityBill;
import io.aadesh.RentBook.entities.ElectricityBillId;
import io.aadesh.RentBook.entities.Tenant;
import io.aadesh.RentBook.services.BillService;
import io.aadesh.RentBook.services.DisplayResultsService;
import io.aadesh.RentBook.services.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.YearMonth;
import java.util.*;

@Controller
public class BillController {

    private final List<String> months;

    @Autowired
    private BillService calculate;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private DisplayResultsService displayResultsService;

    public BillController(){
       this.months= List.of("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
    }

    @GetMapping("/")
    public String getRentCalculator(Model model){

        YearMonth currentMonth = YearMonth.now();
        YearMonth lastMonth = currentMonth.minusMonths(1);

        int year = lastMonth.getYear();
        String prevMonth = lastMonth.getMonth().name();
        String rentMonth = prevMonth.charAt(0)+prevMonth.substring(1).toLowerCase();

        ArrayList<Integer> years = new ArrayList<>();
        for(int i = year-5 ; i<=year+5 ;i++)
            years.add(i);

        model.addAttribute("floors",tenantService.getAllActiveTenants().stream().map(Tenant::getFloor).filter(floor -> floor >1));
        model.addAttribute("months",months);
        model.addAttribute("rentMonth",rentMonth);
        model.addAttribute("years",years);
        model.addAttribute("prevYear",year);

        return "rent-calculator";
    }

    @PostMapping("/createBill")
    public String createBill(@RequestBody MultiValueMap<String, String> formData, Model model){
        //floor, month, year, currentRoomUnits, currentBorUnits, rsPerUnit
        try {
            int floor = Integer.parseInt(Objects.requireNonNull(formData.getFirst("floor")));
            String month = formData.getFirst("month");
            String year = formData.getFirst("year");
            double currentRoomUnits = Double.parseDouble(Objects.requireNonNull(formData.getFirst("currentRoomUnits")));
            double currentBorUnits = Double.parseDouble(Objects.requireNonNull(formData.getFirst("currentBorUnits")));
            int rsPerUnit = Integer.parseInt(Objects.requireNonNull(formData.getFirst("rsPerUnit")));
            ElectricityBill bill = calculate.createElectricityBill(currentRoomUnits,currentBorUnits,floor,month,year,rsPerUnit);
            model.addAttribute("bill",bill);




        }catch (Exception e){
            model.addAttribute("exception",e.getMessage());
        }

        return getRentCalculator(model);
    }

    @GetMapping("/getBills")
    public String getBills(){
        return "bills.html";
    }

    @GetMapping("/getBillsByFloor/{floor}")
    public String getBillsByFloor(@PathVariable int floor, Model model){
        ArrayList<ElectricityBill> bills = new ArrayList<>(displayResultsService.getTenantAllBills(floor));
        Collections.reverse(bills);
        model.addAttribute("bills", bills);
        return "bills.html";
    }

    @GetMapping("/editBillPage/{billId}/{tenantId}")
    public String getEditBillPage(@PathVariable String billId, @PathVariable int tenantId,Model model){
        ElectricityBill bill  = calculate.getBillById(new ElectricityBillId(tenantId,billId));
        YearMonth currentMonth = YearMonth.now();
        YearMonth lastMonth = currentMonth.minusMonths(1);

        int year = lastMonth.getYear();
        String prevMonth = lastMonth.getMonth().name();
        String rentMonth = prevMonth.charAt(0)+prevMonth.substring(1).toLowerCase();

        ArrayList<String> years = new ArrayList<>();
        for(int i = year-5 ; i<=year+5 ;i++)
            years.add(""+i);

        model.addAttribute("floors",tenantService.getAllActiveTenants().stream().map(Tenant::getFloor).filter(floor -> floor >1));
        model.addAttribute("months",months);
        model.addAttribute("years",years);

        model.addAttribute("bill",bill);
        return "edit-bill";
    }

    @PostMapping("/editBill")
    public String editBill(@RequestBody MultiValueMap<String, String> formData, Model model){
        try {
            int floor = Integer.parseInt(Objects.requireNonNull(formData.getFirst("floor")));
            String month = formData.getFirst("month");
            String year = formData.getFirst("year");
            double currentRoomUnits = Double.parseDouble(Objects.requireNonNull(formData.getFirst("currentRoomUnits")));
            double currentBorUnits = Double.parseDouble(Objects.requireNonNull(formData.getFirst("currentBorUnits")));
            int rsPerUnit = Integer.parseInt(Objects.requireNonNull(formData.getFirst("rsPerUnit")));
            calculate.editBill(currentRoomUnits,currentBorUnits,floor,month,year,rsPerUnit);
        }catch (Exception e){
            model.addAttribute("exception",e.getMessage());
        }

        return getRentCalculator(model);
    }




}
