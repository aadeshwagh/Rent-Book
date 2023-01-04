package io.aadesh.RentBook.services;

import io.aadesh.RentBook.entities.Tenant;
import io.aadesh.RentBook.exceptions.TenantNotFindException;
import io.aadesh.RentBook.repos.TenantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TenantService {

    @Autowired
    private TenantRepo tenantRepo;

    public void addTenant(int rent,String name, int floor){
        Optional<Tenant> tenant0 = tenantRepo.findById(floor);

        if(tenant0.isPresent()){
            if(tenant0.get().isActive()){
                System.out.println("Tenant already exists");
                throw new RuntimeException();
            }else {
                tenantRepo.delete(tenant0.get());
                Tenant tenant = tenant0.get();
                tenant.setActive(true);
                tenantRepo.save(tenant);
            }

        }
        Tenant tenant = new Tenant();

        tenant.setRent(rent);
        tenant.setName(name);
        tenant.setFloor(floor);
        tenant.setActive(true);
        tenantRepo.save(tenant);

    }

    public void deActivateTenant(int floor){
        Optional<Tenant> tenant0 = tenantRepo.findById(floor);

        if(tenant0.isEmpty() || !tenant0.get().isActive()){
            
            System.out.println("Not exist/ Deactivated");

            throw new RuntimeException();

        }
        tenantRepo.delete(tenant0.get());
        Tenant tenant = tenant0.get();
        tenant.setActive(false);
        tenantRepo.save(tenant);
    }

    public List<Tenant> getAllActiveTenants(){
        return tenantRepo.findAll().stream().filter(Tenant::isActive).toList();
    }
    public List<Tenant> getAllTenants(){
        return tenantRepo.findAll().stream().toList();
    }

    public Tenant getTenantByFloor(int floor){
        Optional<Tenant> tenant0 = tenantRepo.findById(floor);

        if(tenant0.isEmpty() || !tenant0.get().isActive()){
            
            System.out.println("Tenant Does Not Exists");

            throw new RuntimeException();

        }
        return tenant0.get();
    }

    public void activateTenant(int floor) throws TenantNotFindException {
        Optional<Tenant> tenant0 = tenantRepo.findById(floor);

        if(tenant0.isPresent()){
            if(tenant0.get().isActive()){
                System.out.println("Tenant already Activated");
                throw new RuntimeException();
            }else {
                tenantRepo.delete(tenant0.get());
                Tenant tenant = tenant0.get();
                tenant.setActive(true);
                tenantRepo.save(tenant);
            }

        }else {
            throw new TenantNotFindException();
        }

    }
}
