package com.example.parthivnaresh.myfirstapp;

import java.util.Comparator;

public class StockObject {

    private String price_change;
    private String percent_change;
    private String ticker;
    private String company_name;
    private String price;

    public StockObject(String ticker, String company_name, String price, String price_change, String percent_change) {
        this.ticker = ticker;
        this.company_name = company_name;
        this.price = price;
        this.price_change = price_change;
        this.percent_change = percent_change;
    }

    public String getTicker() {
        return this.ticker;
    }

    public String getCompanyName() {
        return this.company_name;
    }

    public String setCompanyName(String company_name) {
        return this.company_name = company_name;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPercentChange() {
        return this.percent_change;
    }

    public void setPercentChange(String percent_change) { this.percent_change = percent_change; }

    public String getPriceChange() {
        return this.price_change;
    }

    public void setPriceChange(String price_changed) {
        this.price_change = price_changed;
    }
/*
    public int compareTo(StockObject stock) {

        Double compareQuantity = Double.valueOf((stock).getPercentChange());

        //ascending order
        return (int) (Double.valueOf(this.percent_change) - compareQuantity);
    }

    public static Comparator<StockObject> StockPercentComparator
            = (o1, o2) -> {
                Double percentchange1 = Double.valueOf(o1.getPercentChange());
                Double percentchange2 = Double.valueOf(o2.getPercentChange());
                return percentchange2.compareTo(percentchange1);
            };
*/
}
