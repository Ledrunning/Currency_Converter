package com.wordpress.ledrunning.currencyrates;

/**
 * Created by Ledrunner on 13.02.2018.
 */

public class CurrencyRateModel {

    private String id;
    private String nameCode;
    private String charCode;
    private int nominal;
    private String name;
    private double value;
    private double previous;
    private double rate;


    public String getId() {

        return this.id;
    }

    public  void setId(String id) {

        this.id = id;
    }

    public String getNameCode() {

        return this.nameCode;
    }

    public  void setNameCode(String nameCode) {

        this.nameCode = nameCode;
    }

    public String getCharCode() {

        return this.charCode;
    }

    public  void setCharCode(String charCode) {

        this.charCode = charCode;
    }

    public int getNominal() {

        return this.nominal;
    }

    public  void setNominal(int nominal) {

        this.nominal = nominal;
    }

    public String getName() {

        return this.name;
    }

    public  void setName(String name) {

        this.name = name;
    }

    public double getValue() {

        return this.value;
    }

    public  void setValue(double value) {

        this.value = value;
    }


    public double getPrevious() {

        return this.previous;
    }

    public  void setPrevious(double previous) {

        this.previous = previous;
    }

    public double getRate() {

        return this.rate;
    }

    public void setRate(double rate) {

        this.rate = rate;
    }
}
