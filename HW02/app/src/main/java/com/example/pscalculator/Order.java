package com.example.pscalculator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Order implements Serializable {

    Integer pPrice;
    Integer tPrice;
    Integer dCharge;
    Integer count=0;

    public List<String> getTops() {
        return tops;
    }

    public void setTops(List<String> tops) {
        this.tops = tops;
    }

    List<String> tops = new ArrayList<>();
    Order(){

    }

    public int getcount(){
        return count;
    }

    public void setcount(Integer count) {
        this.count = count;
    }

    public int getpPrice() {
        return pPrice;
    }

    public void setpPrice(int pPrice) {
        this.pPrice = pPrice;
    }

    public int gettPrice() {
        return tPrice;
    }

    public void settPrice(int tPrice) {
        this.tPrice = tPrice;
    }
    public void setdCharge(int dCharge) {
        this.dCharge = dCharge;
    }
    public int getdCharge() {
        return dCharge;
    }

    @Override
    public String toString() {
        return "Order{" +
                "tops=" + tops +
                '}';
    }
}
