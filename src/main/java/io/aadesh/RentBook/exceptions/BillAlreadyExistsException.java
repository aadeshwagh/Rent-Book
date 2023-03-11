package io.aadesh.RentBook.exceptions;

public class BillAlreadyExistsException extends RuntimeException{
    public BillAlreadyExistsException() {
        super("Bill for given month/tenant is Already Exists");
    }
}
