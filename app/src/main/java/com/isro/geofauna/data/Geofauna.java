package com.isro.geofauna.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "geofauna")
public class Geofauna {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DatabaseColumns.uniqueSurveyId)
    private String uniqueSurveyId;

    @ColumnInfo(name = DatabaseColumns.serialNo)
    private String serialNo;

    @ColumnInfo(name = DatabaseColumns.locality)
    private String locality;

    @ColumnInfo(name = DatabaseColumns.state)
    private String state;

    @ColumnInfo(name = DatabaseColumns.collector)
    private String collector;

    @ColumnInfo(name = DatabaseColumns.phone)
    private String phone;

    @ColumnInfo(name = DatabaseColumns.habitat)
    private String habitat;

    @ColumnInfo(name = DatabaseColumns.entomofauna)
    private String entomofauna;

    @ColumnInfo(name = DatabaseColumns.otherInvertebrate)
    private String otherInvertebrate;

    @ColumnInfo(name = DatabaseColumns.vertebrate)
    private String vertebrate;

    @ColumnInfo(name = DatabaseColumns.noOfExamples)
    private String noOfExamples;

    @ColumnInfo(name = DatabaseColumns.temperature)
    private String temperature;

    @ColumnInfo(name = DatabaseColumns.humidity)
    private String humidity;

    @ColumnInfo(name = DatabaseColumns.imageAnimal)
    private String imageAnimal;

    @ColumnInfo(name = DatabaseColumns.imageAnimalPath)
    private String imageAnimalPath;

    @ColumnInfo(name = DatabaseColumns.imageHabitat)
    private String imageHabitat;

    @ColumnInfo(name = DatabaseColumns.imageHabitatPath)
    private String imageHabitatPath;

    @ColumnInfo(name = DatabaseColumns.imageHost)
    private String imageHost;

    @ColumnInfo(name = DatabaseColumns.imageHostPath)
    private String imageHostPath;

    // Geo-Tag
    @ColumnInfo(name = DatabaseColumns.date)
    private String date;

    @ColumnInfo(name = DatabaseColumns.time)
    private String time;

    @ColumnInfo(name = DatabaseColumns.latitude)
    private String latitude;

    @ColumnInfo(name = DatabaseColumns.longitude)
    private String longitude;

    @ColumnInfo(name = DatabaseColumns.accuracy)
    private String accuracy;

    @ColumnInfo(name = DatabaseColumns.timestamp)
    private Long timestamp;

    /* Getters & Setters */
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

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

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

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getImageAnimalPath() {
        return imageAnimalPath;
    }

    public void setImageAnimalPath(String imageAnimalPath) {
        this.imageAnimalPath = imageAnimalPath;
    }

    public String getImageHabitatPath() {
        return imageHabitatPath;
    }

    public void setImageHabitatPath(String imageHabitatPath) {
        this.imageHabitatPath = imageHabitatPath;
    }

    public String getImageHostPath() {
        return imageHostPath;
    }

    public void setImageHostPath(String imageHostPath) {
        this.imageHostPath = imageHostPath;
    }


    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
