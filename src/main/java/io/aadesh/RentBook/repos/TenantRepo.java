package io.aadesh.RentBook.repos;

import io.aadesh.RentBook.entities.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepo extends JpaRepository<Tenant,Integer> {
}
