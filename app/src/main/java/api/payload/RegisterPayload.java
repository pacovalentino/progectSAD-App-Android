package api.payload;

import java.util.HashMap;

public class RegisterPayload {
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final String dateOfBirth;
    private final String fiscalCode;
    private final String city;
    private final String cap;
    private final String mobilePhone;
    private final String gender;
    private final String heartDisease;
    private final String allergy;
    private final String immunosuppression;
    private final String anticoagulants;
    private final String covid;

    public RegisterPayload(
        String email,
        String password,
        String firstName,
        String lastName,
        String dateOfBirth,
        String fiscalCode,
        String city,
        String cap,
        String mobilePhone,
        boolean gender,
        boolean heartDisease,
        boolean allergy,
        boolean immunosuppression,
        boolean anticoagulants,
        boolean covid
    ) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.fiscalCode = fiscalCode;
        this.city = city;
        this.cap = cap;
        this.mobilePhone = mobilePhone;
        this.gender = gender ? "0" : "1";
        this.heartDisease = heartDisease ? "0" : "1";
        this.allergy = allergy ? "0" : "1";
        this.immunosuppression = immunosuppression ? "0" : "1";
        this.anticoagulants = anticoagulants ? "0" : "1";
        this.covid = covid ? "0" : "1";
    }

    //TODO: in una futura iterazione del progetto andrebbe aggiunta più logica
    //TODO: di validazione
    public boolean validate() {
        if (this.email.equals("")) {
            return false;
        }

        if (this.password.equals("")) {
            return false;
        }

        if (this.firstName.equals("")) {
            return false;
        }

        if (this.lastName.equals("")) {
            return false;
        }

        if (this.dateOfBirth.equals("")) {
            return false;
        }

        if (this.fiscalCode.equals("")) {
            return false;
        }

        if (this.city.equals("")) {
            return false;
        }

        if (this.cap.equals("")) {
            return false;
        }

        if (this.mobilePhone.equals("")) {
            return false;
        }

        if (this.gender.equals("")) {
            return false;
        }

        if (this.heartDisease.equals("")) {
            return false;
        }

        if (this.allergy.equals("")) {
            return false;
        }

        if (this.immunosuppression.equals("")) {
            return false;
        }

        if (this.anticoagulants.equals("")) {
            return false;
        }

        if (this.covid.equals("")) {
            return false;
        }

        return true;
    }

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> hashedPayload = new HashMap<>();

        hashedPayload.put("email", this.email);
        hashedPayload.put("password", this.password);
        hashedPayload.put("first_name", this.firstName);
        hashedPayload.put("last_name", this.lastName);
        hashedPayload.put("date_of_birth", this.dateOfBirth);
        hashedPayload.put("fiscal_code", this.fiscalCode);
        hashedPayload.put("city", this.city);
        hashedPayload.put("cap", this.cap);
        hashedPayload.put("mobile_phone", this.mobilePhone);
        hashedPayload.put("gender", this.gender);
        hashedPayload.put("heart_disease", this.heartDisease);
        hashedPayload.put("allergy", this.allergy);
        hashedPayload.put("immunosuppression", this.immunosuppression);
        hashedPayload.put("anticoagulants", this.anticoagulants);
        hashedPayload.put("covid", this.covid);

        return hashedPayload;
    }
}
