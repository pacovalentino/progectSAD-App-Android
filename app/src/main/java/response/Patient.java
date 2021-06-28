package response;

import android.util.Log;

import java.io.Serializable;

public class Patient implements Serializable {
    private String email;
    private String password;
    private String last_name;
    private String first_name;
    private String date_of_birth;
    private String gender;
    private String fiscal_code;
    private String city;
    private String cap;
    private String mobile_phone;
    private String heart_disease;
    private String allergy;
    private String immunosuppression;
    private String anticoagulants;
    private String covid;
    private String id;

    public Patient(String email, String password, String first_name, String last_name,
                   String date_of_birth, String gender, String fiscal_code, String city,
                   String cap, String mobile_phone, String heart_disease, String allergy,
                   String immunosuppression,String anticoagulants, String covid, String id) {
        this.email = email;
        this.password = password;
        this.last_name = last_name;
        this.first_name = first_name;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.fiscal_code = fiscal_code;
        this.city = city;
        this.cap = cap;
        this.mobile_phone = mobile_phone;
        this.heart_disease = heart_disease;
        this.allergy = allergy;
        this.immunosuppression = immunosuppression;
        this.anticoagulants=anticoagulants;
        this.covid = covid;
        this.id=id;
    }

    public String getAnticoagulants() {
        return anticoagulants;
    }

    public void setAnticoagulants(String anticoagulants) {
        this.anticoagulants = anticoagulants;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFiscal_code() {
        return fiscal_code;
    }

    public void setFiscal_code(String fiscal_code) {
        this.fiscal_code = fiscal_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getHeart_disease() {
        return heart_disease;
    }

    public void setHeart_disease(String heart_disease) {
        this.heart_disease = heart_disease;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public String getImmunosuppression() {
        return immunosuppression;
    }

    public void setImmunosuppression(String immunosuppression) {
        this.immunosuppression = immunosuppression;
    }

    public String getCovid() {
        return covid;
    }

    public void setCovid(String covid) {
        this.covid = covid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
