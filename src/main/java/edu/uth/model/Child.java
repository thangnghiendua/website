package edu.uth.model;

import java.util.Date;
import java.util.Calendar;

public class Child {
    private Integer id;
    private String name;
    private Date birthDate;
    private String gender;
    private String relationship;
    private Double weight;
    private Double height;
    private String allergies;
    private String medicalHistory;
    private String note;
    private String avatarUrl;
    private Integer completedVaccinations;
    private Integer userId;

    public Child() {
        this.completedVaccinations = 0;
    }

    public Child(Integer id, String name, Date birthDate, String gender) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.completedVaccinations = 0;
    }

    // Helper method for template display
    public String getAgeDescription() {
        if (birthDate == null) return "N/A";
        
        Calendar dob = Calendar.getInstance();
        dob.setTime(birthDate);
        Calendar today = Calendar.getInstance();
        
        int years = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        int months = today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
        
        if (months < 0) {
            years--;
            months += 12;
        }
        
        if (years > 0) {
            return years + " tuổi " + (months > 0 ? months + " tháng" : "");
        } else {
            return months + " tháng tuổi";
        }
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }

    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public Integer getCompletedVaccinations() { return completedVaccinations; }
    public void setCompletedVaccinations(Integer completedVaccinations) { this.completedVaccinations = completedVaccinations; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
}
