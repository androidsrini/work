package com.codesense.driverapp.localstoreage;

import androidx.room.Room;
import android.content.Context;

import com.codesense.driverapp.net.Constant;

/**
 * This client class will
 */
public class DatabaseClient {

    private Context mCtx;
    private static DatabaseClient mInstance;

    //our app database object
    private AppDatabase appDatabase;

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, Constant.APP_DB_NAME).build();
    }

    /**
     * This method will return DatabaseClient(current) object.
     * This method to create object only one time.
     * @param mCtx param
     * @return DatabaseClient
     */
    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (null == mInstance) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    /**
     * This method to return AppDatabase object.
     * @return AppDatabase
     */
    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
