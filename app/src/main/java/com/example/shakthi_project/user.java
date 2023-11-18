package com.example.shakthi_project;

public class user {
    String Date,Destination,Fare,FinalFare,Scheme,Source;

    public user(){

    }

    public user(String date, String destination, String fare, String finalFare, String scheme, String source) {
        Date = date;
        Destination = destination;
        Fare = fare;
        FinalFare = finalFare;
        Scheme = scheme;
        Source = source;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getFare() {
        return Fare;
    }

    public void setFare(String fare) {
        Fare = fare;
    }

    public String getFinalFare() {
        return FinalFare;
    }

    public void setFinalFare(String finalFare) {
        FinalFare = finalFare;
    }

    public String getScheme() {
        return Scheme;
    }

    public void setScheme(String scheme) {
        Scheme = scheme;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }
}
