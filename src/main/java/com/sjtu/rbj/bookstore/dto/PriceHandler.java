package com.sjtu.rbj.bookstore.dto;

/**
 * An util class to handle the price.
 *
 * @author Bojun Ren
 * @data 2023/05/07
 */
public class PriceHandler {

    private Integer price;
    private Integer decimalPlace = 2;

    public static PriceHandler from(Integer price) {
        return new PriceHandler(price);
    }

    public static PriceHandler of(Integer price, Integer decimalPlace) {
        return new PriceHandler(price, decimalPlace);
    }

    /**
     * Create from the price only. The default decimal place is 2.
     * @param price
     */
    public PriceHandler(Integer price) {
        this.price = price;
    }

    public PriceHandler(Integer price, Integer decimalPlace) {
        this.price = price;
        this.decimalPlace = decimalPlace;
    }

    @Override
    public String toString() {
        String priceStr = price.toString();
        int integerPlace = priceStr.length() - decimalPlace;
        if (integerPlace <= 0) {
            String prefix = "0.";
            while (integerPlace < 0) {
                integerPlace++;
                prefix += "0";
            }
            return prefix + priceStr.toString();
        }
        return priceStr.substring(0, integerPlace) + "." + priceStr.substring(integerPlace);
    }
}
