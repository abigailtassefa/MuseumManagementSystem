package com.museum.model;

import java.math.BigDecimal;
import java.sql.Time;

public class Museum {
    private int museumId;
    private String name;
    private String description;
    private City city;
    private Time openingTime;
    private Time closingTime;
    private BigDecimal localPrice;
    private BigDecimal foreignPrice;
    private String culturalInfo;
    private String imagePath;

    public Museum() {}

    public Museum(int museumId, String name, String description, City city,
                  Time openingTime, Time closingTime, BigDecimal localPrice,
                  BigDecimal foreignPrice, String culturalInfo, String imagePath) {
        this.museumId = museumId;
        this.name = name;
        this.description = description;
        this.city = city;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.localPrice = localPrice;
        this.foreignPrice = foreignPrice;
        this.culturalInfo = culturalInfo;
        this.imagePath = imagePath;
    }

    // Getters and Setters
    public int getMuseumId() { return museumId; }
    public void setMuseumId(int museumId) { this.museumId = museumId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public City getCity() { return city; }
    public void setCity(City city) { this.city = city; }
    public Time getOpeningTime() { return openingTime; }
    public void setOpeningTime(Time openingTime) { this.openingTime = openingTime; }
    public Time getClosingTime() { return closingTime; }
    public void setClosingTime(Time closingTime) { this.closingTime = closingTime; }
    public BigDecimal getLocalPrice() { return localPrice; }
    public void setLocalPrice(BigDecimal localPrice) { this.localPrice = localPrice; }
    public BigDecimal getForeignPrice() { return foreignPrice; }
    public void setForeignPrice(BigDecimal foreignPrice) { this.foreignPrice = foreignPrice; }
    public String getCulturalInfo() { return culturalInfo; }
    public void setCulturalInfo(String culturalInfo) { this.culturalInfo = culturalInfo; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    @Override
    public String toString() {
        return name;  // for JList display
    }
}