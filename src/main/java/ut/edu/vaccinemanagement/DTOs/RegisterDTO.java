package ut.edu.vaccinemanagement.DTOs;

import ut.edu.vaccinemanagement.models.Gender;

import java.util.Date;

public class RegisterDTO {
    private String userName;
    private Gender gender;
    private Date birthDate;
    private String email;
    private String userPassword;

    public String getUserName() {
        return userName;
    }

    public Gender getGender() {
        return gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
