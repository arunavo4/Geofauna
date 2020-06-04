package com.isro.geofauna.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "geofauna")
public class Geofauna implements Parcelable {

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

    @ColumnInfo(name = DatabaseColumns.fieldNotes)
    private String fieldNotes;

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
    private Double latitude;

    @ColumnInfo(name = DatabaseColumns.longitude)
    private Double longitude;

    @ColumnInfo(name = DatabaseColumns.accuracy)
    private String accuracy;

    @ColumnInfo(name = DatabaseColumns.timestamp)
    private Long timestamp;

    public Geofauna(){}

    protected Geofauna(Parcel in) {
        uniqueSurveyId = Objects.requireNonNull(in.readString());
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
        fieldNotes = in.readString();
        imageAnimal = in.readString();
        imageAnimalPath = in.readString();
        imageHabitat = in.readString();
        imageHabitatPath = in.readString();
        imageHost = in.readString();
        imageHostPath = in.readString();
        date = in.readString();
        time = in.readString();
        accuracy = in.readString();
        if (in.readByte() == 0) {
            timestamp = null;
            longitude = null;
            latitude = null;
        } else {
            timestamp = in.readLong();
            longitude = in.readDouble();
            latitude = in.readDouble();
        }
    }

    public static final Creator<Geofauna> CREATOR = new Creator<Geofauna>() {
        @Override
        public Geofauna createFromParcel(Parcel in) {
            return new Geofauna(in);
        }

        @Override
        public Geofauna[] newArray(int size) {
            return new Geofauna[size];
        }
    };

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

    public String getFieldNotes() {
        return fieldNotes;
    }

    public void setFieldNotes(String fieldNotes) {
        this.fieldNotes = fieldNotes;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
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

    // Parcelable Stuff

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
        dest.writeString(fieldNotes);
        dest.writeString(imageAnimal);
        dest.writeString(imageAnimalPath);
        dest.writeString(imageHabitat);
        dest.writeString(imageHabitatPath);
        dest.writeString(imageHost);
        dest.writeString(imageHostPath);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(accuracy);
        if (timestamp == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(timestamp);
        }
    }

}
