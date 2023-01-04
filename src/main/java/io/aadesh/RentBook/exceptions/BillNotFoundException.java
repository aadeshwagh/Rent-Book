package io.aadesh.RentBook.exceptions;

public class BillNotFoundException extends Exception{
    public BillNotFoundException() {
        super("Bill for given month/tenant is Not Found");
    }
}
