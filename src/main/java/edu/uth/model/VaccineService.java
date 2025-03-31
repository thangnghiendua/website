package edu.uth.model;

import java.util.List;

public class VaccineService {
    private Integer id;
    private String name;
    private String shortDescription;
    private String description;
    private Double price;
    private String imageUrl;
    private String type; // single, package, custom
    private List<String> items; // for packages

    public VaccineService() {
    }

    public VaccineService(Integer id, String name, String shortDescription, Double price, String type) {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.price = price;
        this.type = type;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public List<String> getItems() { return items; }
    public void setItems(List<String> items) { this.items = items; }
}
