package io.aadesh.RentBook.entities;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ElectricityBillId implements Serializable {

    public ElectricityBillId(){}

    private int tenantId;
    private String billId;
    public ElectricityBillId(int tenantId, String billId){
        this.tenantId = tenantId;
        this.billId = billId;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    @Override
    public String toString() {
        return billId;
    }

    // equals, hashCode, and getters/setters
}