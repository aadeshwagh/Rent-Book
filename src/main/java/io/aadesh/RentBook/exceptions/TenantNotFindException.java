package io.aadesh.RentBook.exceptions;

public class TenantNotFindException extends RuntimeException{
    public TenantNotFindException() {
        super("Tenant with given floor is not found");
    }
}
