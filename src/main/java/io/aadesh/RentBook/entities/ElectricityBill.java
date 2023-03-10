package io.aadesh.RentBook.entities;
import jakarta.persistence.*;

@Entity
public class ElectricityBill {

    @EmbeddedId
    private ElectricityBillId id;

    private String month;
    private String year;

    private double currentTotalRoomUnits;

    private double previousTotalRoomUnits;

    private double roomUnits;

    private double roomBillAmount;

    private double currentTotalBorUnits;

    private double previousTotalBorUnits;

    private double roomBorUnits;

    private double roomBorAmount;
    private int perRs;

    private int totalBill;

    private int grandTotal;

    public int getPerRs() {
        return perRs;
    }

    public double getRoomBorAmount() {
        return roomBorAmount;
    }

    public void setRoomBorAmount(double roomBorAmount) {
        this.roomBorAmount = roomBorAmount;
    }

    public void setPerRs(int perRs) {
        this.perRs = perRs;
    }

    public ElectricityBillId getId() {
        return id;
    }

    public void setId(ElectricityBillId id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getCurrentTotalRoomUnits() {
        return currentTotalRoomUnits;
    }

    public void setCurrentTotalRoomUnits(double currentTotalRoomUnits) {
        this.currentTotalRoomUnits = currentTotalRoomUnits;
    }

    public double getPreviousTotalRoomUnits() {
        return previousTotalRoomUnits;
    }

    public void setPreviousTotalRoomUnits(double previousTotalRoomUnits) {
        this.previousTotalRoomUnits = previousTotalRoomUnits;
    }

    public double getRoomUnits() {
        return roomUnits;
    }

    public void setRoomUnits(double roomUnits) {
        this.roomUnits = roomUnits;
    }

    public double getRoomBillAmount() {
        return roomBillAmount;
    }

    public void setRoomBillAmount(double roomBillAmount) {
        this.roomBillAmount = roomBillAmount;
    }

    public double getCurrentTotalBorUnits() {
        return currentTotalBorUnits;
    }

    public void setCurrentTotalBorUnits(double currentTotalBorUnits) {
        this.currentTotalBorUnits = currentTotalBorUnits;
    }

    public double getPreviousTotalBorUnits() {
        return previousTotalBorUnits;
    }

    public void setPreviousTotalBorUnits(double previousTotalBorUnits) {
        this.previousTotalBorUnits = previousTotalBorUnits;
    }

    public double getRoomBorUnits() {
        return roomBorUnits;
    }

    public void setRoomBorUnits(double roomBorUnits) {
        this.roomBorUnits = roomBorUnits;
    }

    public int getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(int totalBill) {
        this.totalBill = totalBill;
    }

    public int getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(int grandTotal) {
        this.grandTotal = grandTotal;
    }


    @Override
    public String toString() {
        return  id.toString() +
                ", Floor = " + id.getTenantId() +
                "\n???????????? Room Reading = " + currentTotalRoomUnits +
                ", ??????????????? Room Reading = " + previousTotalRoomUnits +
                ", Total Units = " + roomUnits +
                ", Unit Per Rs = " + perRs +
                "\nTotal Room Bill = " + Math.round(roomBillAmount * 10.0) / 10.0 +
                "\n???????????? Bor Reading = " + currentTotalBorUnits +
                ", ??????????????? Bor Reading = " + previousTotalBorUnits +
                ", Total units = " + Math.round(roomBorUnits * 10.0) / 10.0 +
                "\nTotal Bor Bill = " + (int)roomBorAmount +

                "\nTotal Bill = " + totalBill;

    }
}
