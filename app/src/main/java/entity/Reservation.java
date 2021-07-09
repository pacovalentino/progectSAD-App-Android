package entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Reservation  implements Serializable {

    private String struttura;
    private String data;
    private String time;
    private String stock_vaccino;
    private String vaccino;
    private String stato;
    private String tel;

    public Reservation(JSONObject reservationObject) throws JSONException {
        struttura = reservationObject.getString("structure_name");
        data = reservationObject.getString("date");
        time = reservationObject.getString("time");
        stock_vaccino = reservationObject.getString("stock_id");
        vaccino = reservationObject.getString("name");
        stato = reservationObject.getString("state");
        tel = reservationObject.getString("phone_number");
    }

    public Reservation(String struttura, String data, String time, String stock_vaccino, String vaccino, String stato, String tel) {
        this.struttura = struttura;
        this.data = data;
        this.time = time;
        this.stock_vaccino = stock_vaccino;
        this.vaccino=vaccino;
        this.stato = stato;
        this.tel=tel;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getVaccino() {
        return vaccino;
    }

    public void setVaccino(String vaccino) {
        this.vaccino = vaccino;
    }

    public String getStruttura() {
        return struttura;
    }

    public void setStruttura(String struttura) {
        this.struttura = struttura;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStock_vaccino() {
        return stock_vaccino;
    }

    public void setStock_vaccino(String stock_vaccino) {
        this.stock_vaccino = stock_vaccino;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
}
