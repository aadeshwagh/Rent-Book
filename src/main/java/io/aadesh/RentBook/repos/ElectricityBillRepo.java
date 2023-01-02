package io.aadesh.RentBook.repos;

import io.aadesh.RentBook.entities.ElectricityBill;
import io.aadesh.RentBook.entities.ElectricityBillId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectricityBillRepo extends JpaRepository<ElectricityBill, ElectricityBillId> {

}
