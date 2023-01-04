package io.aadesh.RentBook.exceptions;

public class BillAlreadyExistsException extends Exception{
    public BillAlreadyExistsException() {
        super("Bill for given month/tenant is Already Exists");
    }
}
