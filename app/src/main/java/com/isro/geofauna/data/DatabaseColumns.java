package com.isro.geofauna.data;

public class DatabaseColumns {

    public static final String parcelGeofauna = "PARCEL_GEOFAUNA";

    public static final String timestamp = "Timestamp";

    public static final String uniqueSurveyId = "UniqueSurveyID";

    public static final String serialNo = "Serial_no";

    public static final String locality = "Locality";

    public static final String state = "State";

    public static final String collector = "Collector";

    public static final String phone = "Phone";

    public static final String habitat = "Habitat";

    public static final String entomofauna = "Entomofauna";

    public static final String otherInvertebrate = "OtherInvertebrate";

    public static final String vertebrate = "Vertebrate";

    public static final String noOfExamples = "NoOfExamples";

    public static final String temperature = "Temperature";

    public static final String humidity = "Humidity";

    public static final String fieldNotes = "Field_Notes";

    public static final String imageAnimal = "ImageAnimal";

    public static final String imageAnimalPath = "ImageAnimalPath";

    public static final String imageHabitat = "ImageHabitat";

    public static final String imageHabitatPath = "ImageHabitatPath";

    public static final String imageHost = "ImageHost";

    public static final String imageHostPath = "ImageHostPath";

    // Geo-Tag
    public static final String date = "Date";

    public static final String time = "Time";

    public static final String latitude = "Latitude";

    public static final String longitude = "Longitude";

    public static final String accuracy = "Accuracy";

    public static String[] getDatabaseColumns(){

        return new String[]{
                uniqueSurveyId,
                serialNo,
                locality,
                state,
                collector,
                phone,
                habitat,
                entomofauna,
                otherInvertebrate,
                vertebrate,
                noOfExamples,
                temperature,
                humidity,
                fieldNotes,
                imageAnimal,
                imageHabitat,
                imageHost,
                date,
                time,
                latitude,
                longitude,
                accuracy
        };
    }
}
