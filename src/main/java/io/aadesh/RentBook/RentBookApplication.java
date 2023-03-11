package io.aadesh.RentBook;

import io.aadesh.RentBook.entities.ElectricityBillId;
import io.aadesh.RentBook.services.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RentBookApplication implements CommandLineRunner {
	@Autowired
	BillService billService;

	public static void main(String[] args) {
		SpringApplication.run(RentBookApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		billService.deleteBill(new ElectricityBillId(2,"February-2023"));
//		billService.deleteBill(new ElectricityBillId(3,"February-2023"));
	}
}
