package io.aadesh.RentBook;

import io.aadesh.RentBook.services.BillService;
import io.aadesh.RentBook.services.DisplayResultsService;
import io.aadesh.RentBook.services.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RentBookApplication implements CommandLineRunner {

	@Autowired
	private BillService calculate;

	@Autowired
	private TenantService tenantService;

	@Autowired
	DisplayResultsService displayResults;

	public static void main(String[] args) {
		SpringApplication.run(RentBookApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		tenantService.addTenant(4500,"2 nd floor",2);
//		tenantService.addTenant(5200,"3rd floor",3);
//
//		tenantService.addTenant(0,"Owner",1);

//		calculate.createElectricityBillForFirstTime(6332.8,6234,2100.4,2073,2,"December","2022",8);
//		calculate.createElectricityBillForFirstTime(3473,3436,2100.4,2073,3,"December","2022",8);


		//displayResults.getTenantAllBills(2).stream().forEach(System.out::println);
		//displayResults.getLatestBillForAllTenants().forEach(bill->System.out.println(bill+"\n"));


	}
}
