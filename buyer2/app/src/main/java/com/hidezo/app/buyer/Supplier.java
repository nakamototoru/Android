package com.hidezo.app.buyer;

import java.util.ArrayList;

/**
 *
 */
public class Supplier {

    public String name;
    public String hometown;

    public Supplier(String name, String hometown) {
        this.name = name;
        this.hometown = hometown;
    }

    public static ArrayList<Supplier> getSuppliers() {
        ArrayList<Supplier> users = new ArrayList<Supplier>();
        users.add(new Supplier("Harry", "San Diego"));
        users.add(new Supplier("Marla", "San Francisco"));
        users.add(new Supplier("Sarah", "San Marco"));
        return users;
    }

}
