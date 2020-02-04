package com.isro.geofauna.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "geofauna")
public class Geofauna {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "UniqueSurveyID")
    private String uniqueSurveyId;

    @ColumnInfo(name = "Serial_no")
    private String serialNo;

    @ColumnInfo(name = "Locality")
    private String locality;

    @ColumnInfo(name = "State")
    private String state;

    @ColumnInfo(name = "Collector")
    private String collector;

    @ColumnInfo(name = "Habitat")
    private String habitat;

    @ColumnInfo(name = "Entomofauna")
    private String entomofauna;

    @ColumnInfo(name = "OtherInvertebrate")
    private String otherInvertebrate;

    @ColumnInfo(name = "Vertebrate")
    private String vertebrate;

    @ColumnInfo(name = "NoOfExamples")
    private String noOfExamples;

    @ColumnInfo(name = "Temperature")
    private String temperature;

    @ColumnInfo(name = "Humidity")
    private String humidity;

    @ColumnInfo(name = "ImageAnimal")
    private String imageAnimal;

    @ColumnInfo(name = "ImageHabitat")
    private String imageHabitat;

    @ColumnInfo(name = "ImageHost")
    private String imageHost;

    // Geo-Tag
    @ColumnInfo(name = "Date")
    private String date;

    @ColumnInfo(name = "Time")
    private String time;

    @ColumnInfo(name = "Latitude")
    private String latitude;

    @ColumnInfo(name = "Longitude")
    private String longitude;


    @NonNull
    public String getUniqueSurveyId() {
        return uniqueSurveyId;
    }

    public void setUniqueSurveyId(@NonNull String uniqueSurveyId) {
        this.uniqueSurveyId = uniqueSurveyId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getEntomofauna() {
        return entomofauna;
    }

    public void setEntomofauna(String entomofauna) {
        this.entomofauna = entomofauna;
    }

    public String getOtherInvertebrate() {
        return otherInvertebrate;
    }

    public void setOtherInvertebrate(String otherInvertebrate) {
        this.otherInvertebrate = otherInvertebrate;
    }

    public String getVertebrate() {
        return vertebrate;
    }

    public void setVertebrate(String vertebrate) {
        this.vertebrate = vertebrate;
    }

    public String getNoOfExamples() {
        return noOfExamples;
    }

    public void setNoOfExamples(String noOfExamples) {
        this.noOfExamples = noOfExamples;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getImageAnimal() {
        return imageAnimal;
    }

    public void setImageAnimal(String imageAnimal) {
        this.imageAnimal = imageAnimal;
    }

    public String getImageHabitat() {
        return imageHabitat;
    }

    public void setImageHabitat(String imageHabitat) {
        this.imageHabitat = imageHabitat;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
