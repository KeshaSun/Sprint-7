package edu.praktikum.sprint7.models;

public class CourierCred {
    private String login;
    private String password;

    public CourierCred(String login,String password){

        this.login = login;
        this.password = password;
    }
    public static CourierCred fromCourier(Courier courier){
        return new CourierCred(courier.getLogin(),courier.getPassword());
    }

    @Override
    public String toString() {
        return "CourierCred{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
