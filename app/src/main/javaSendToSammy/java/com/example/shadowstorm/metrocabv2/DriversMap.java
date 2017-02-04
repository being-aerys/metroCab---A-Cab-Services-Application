package com.example.shadowstorm.metrocabv2; //lala pakh

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Dipesh on 31/05/16.
 */
public class DriversMap extends Activity {

    DriversLocation_db userDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        openDB();
        populateListViewFromDB();

    }

    private void openDB()
    {
        userDb = new DriversLocation_db(this);
    }
    private void populateListViewFromDB() {
        Cursor cursor = userDb.getDriverData();

        // Allow activity to manage lifetime of the cursor.
        // DEPRECATED! Runs on the UI thread, OK for small/short queries.
        startManagingCursor(cursor);

        String[] fromFieldNames = new String[]
                {"salutation","fname","lname","vehicle","lat"};
        int[] toViewIDs = new int[]
                {R.id.driverSalute,R.id.driverFname,R.id.driverLname,R.id.driverCab,R.id.driverDistance};

        // Create adapter to may columns of the DB onto elemesnt in the UI.
        SimpleCursorAdapter myCursorAdapter =
                new SimpleCursorAdapter(
                        this,        // Context
                        R.layout.driver_rows,    // Row layout template
                        cursor,                    // cursor (set of DB records to map)
                        fromFieldNames,            // DB Column names
                        toViewIDs                // View IDs to put information in
                );

        // Set the adapter for the list view
        ListView myList = (ListView) findViewById(R.id.listDriverLoc);
        myList.setAdapter(myCursorAdapter);


    }

}
