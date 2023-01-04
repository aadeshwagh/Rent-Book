package io.aadesh.RentBook.exceptions;

public class TenantNotFindException extends Exception{
    public TenantNotFindException() {
        super("Tenant with given floor is not found");
    }
}
