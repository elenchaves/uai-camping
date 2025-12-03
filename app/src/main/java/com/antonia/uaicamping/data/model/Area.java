package com.antonia.uaicamping.data.model;


public class Area {
    private int id;
    private int userId;
    private String title;
    private String description;
    private String address;
    private double pricePerNight;
    private int maxGuests;
    private boolean hasWater;
    private boolean hasElectricity;

    public Area() {
    }

    public Area(int userId, String title, String description, String address, double pricePerNight, int maxGuests, boolean hasWater, boolean hasElectricity) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.address = address;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
        this.hasWater = hasWater;
        this.hasElectricity = hasElectricity;
    }

    public Area(int id, int userId, String title, String description, String address, double pricePerNight, int maxGuests, boolean hasWater, boolean hasElectricity) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.address = address;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
        this.hasWater = hasWater;
        this.hasElectricity = hasElectricity;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }
    public int getMaxGuests() { return maxGuests; }
    public void setMaxGuests(int maxGuests) { this.maxGuests = maxGuests; }
    public boolean isHasWater() { return hasWater; }
    public void setHasWater(boolean hasWater) { this.hasWater = hasWater; }
    public boolean isHasElectricity() { return hasElectricity; }
    public void setHasElectricity(boolean hasElectricity) { this.hasElectricity = hasElectricity; }
}
