package com.isro.geofauna.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Geofauna.class}, version = 8, exportSchema = false)
public abstract class GeofaunaRoomDatabase extends RoomDatabase {

    public abstract GeofaunaDao geofaunaDao();

    private static volatile GeofaunaRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static GeofaunaRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GeofaunaRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GeofaunaRoomDatabase.class, "geofauna_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

//    public static void destroyInstance() {
//        INSTANCE = null;
//    }


    /* Callback to populate database */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
//            databaseWriteExecutor.execute(() -> {
//                // Populate the database in the background.
//                // If you want to start with more words, just add them.
//                GeofaunaDao dao = INSTANCE.geofaunaDao();
//                dao.deleteAll();
//
//                Geofauna geofauna = new Geofauna();
//                geofauna.setUniqueSurveyId("GT67839");
//                geofauna.setDate("18/99/9999");
//                geofauna.setSerialNo("HJKOIDB");
//                geofauna.setLocality("Baroipur");
//                dao.insertAll(geofauna);
//
//            });
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

        }
    };

}
