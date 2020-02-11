package com.isro.geofauna.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Record implements Parcelable {

    public String getUniqueSurveyId() {
        return uniqueSurveyId;
    }

    public void setUniqueSurveyId(String uniqueSurveyId) {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getImageAnimalPath() {
        return imageAnimalPath;
    }

    public void setImageAnimalPath(String imageAnimalPath) {
        this.imageAnimalPath = imageAnimalPath;
    }

    public String getImageHabitat() {
        return imageHabitat;
    }

    public void setImageHabitat(String imageHabitat) {
        this.imageHabitat = imageHabitat;
    }

    public String getImageHabitatPath() {
        return imageHabitatPath;
    }

    public void setImageHabitatPath(String imageHabitatPath) {
        this.imageHabitatPath = imageHabitatPath;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public String getImageHostPath() {
        return imageHostPath;
    }

    public void setImageHostPath(String imageHostPath) {
        this.imageHostPath = imageHostPath;
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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public static Creator<Record> getCREATOR() {
        return CREATOR;
    }

    private String uniqueSurveyId;
    private String serialNo;
    private String locality;
    private String state;
    private String collector;
    private String phone;
    private String habitat;
    private String entomofauna;
    private String otherInvertebrate;
    private String vertebrate;
    private String noOfExamples;
    private String temperature;
    private String humidity;
    private String imageAnimal;
    private String imageAnimalPath;
    private String imageHabitat;
    private String imageHabitatPath;
    private String imageHost;
    private String imageHostPath;

    // Geo-Tag
    private String date;
    private String time;
    private String latitude;
    private String longitude;
    private String accuracy;
    private Long timestamp;

    public Record(Geofauna geofauna){
        uniqueSurveyId = geofauna.getUniqueSurveyId();
        serialNo = geofauna.getSerialNo();
        locality = geofauna.getLocality();
        state = geofauna.getState();
        collector = geofauna.getCollector();
        phone = geofauna.getPhone();
        habitat = geofauna.getHabitat();
        entomofauna = geofauna.getEntomofauna();
        otherInvertebrate = geofauna.getOtherInvertebrate();
        vertebrate = geofauna.getVertebrate();
        noOfExamples = geofauna.getNoOfExamples();
        temperature = geofauna.getTemperature();
        humidity = geofauna.getHumidity();
        imageAnimal = geofauna.getImageAnimal();
        imageAnimalPath = geofauna.getImageAnimalPath();
        imageHabitat = geofauna.getImageHabitat();
        imageHabitatPath = geofauna.getImageHabitatPath();
        imageHost = geofauna.getImageHost();
        imageHostPath = geofauna.getImageHostPath();

        // Geo-Tag
        date = geofauna.getDate();
        time = geofauna.getTime();
        latitude = geofauna.getLatitude();
        longitude = geofauna.getLongitude();
        accuracy = geofauna.getAccuracy();
        timestamp = geofauna.getTimestamp();
    }

    protected Record(Parcel in) {
        uniqueSurveyId = in.readString();
        serialNo = in.readString();
        locality = in.readString();
        state = in.readString();
        collector = in.readString();
        phone = in.readString();
        habitat = in.readString();
        entomofauna = in.readString();
        otherInvertebrate = in.readString();
        vertebrate = in.readString();
        noOfExamples = in.readString();
        temperature = in.readString();
        humidity = in.readString();
        imageAnimal = in.readString();
        imageAnimalPath = in.readString();
        imageHabitat = in.readString();
        imageHabitatPath = in.readString();
        imageHost = in.readString();
        imageHostPath = in.readString();

        // Geo-Tag
        date = in.readString();
        time = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        accuracy = in.readString();
        timestamp = in.readLong();
    }

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uniqueSurveyId);
        dest.writeString(serialNo);
        dest.writeString(locality);
        dest.writeString(state);
        dest.writeString(collector);
        dest.writeString(phone);
        dest.writeString(habitat);
        dest.writeString(entomofauna);
        dest.writeString(otherInvertebrate);
        dest.writeString(vertebrate);
        dest.writeString(noOfExamples);
        dest.writeString(temperature);
        dest.writeString(humidity);
        dest.writeString(imageAnimal);
        dest.writeString(imageAnimalPath);
        dest.writeString(imageHabitat);
        dest.writeString(imageHabitatPath);
        dest.writeString(imageHost);
        dest.writeString(imageHostPath);

        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(accuracy);
        dest.writeLong(timestamp);

    }
}
