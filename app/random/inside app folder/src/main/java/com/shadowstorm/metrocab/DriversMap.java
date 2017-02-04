package com.shadowstorm.metrocab;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

/**
 * Created by Dipesh on 31/05/16.
 */
public class DriversMap extends Activity {

    private ArrayList<String> data = new ArrayList<String>();

    DriversLocation_db userDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        openDB();
        populateListViewFromDB();

    }
    private void openDB() {
        userDb =new DriversLocation_db(this);
    }
    private void populateListViewFromDB() {
        Cursor cursor = userDb.getDriverData();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String salutes = cursor.getString(1);
                final String fnames = cursor.getString(2);
                String lnames = cursor.getString(3);

                // Allow activity to manage lifetime of the cursor.
                // DEPRECATED! Runs on the UI thread, OK for small/short queries.
                    startManagingCursor(cursor);

                    String[] fromFieldNames = new String[]
                            {"salutation","fname","lname","vehicle","dist"};
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
                    myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String db_id= String.valueOf(id);
                            //Toast.makeText(DriversMap.this, "List item was clicked at " + db_id, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DriversMap.this,callActivity.class);
                            intent.putExtra("db_id",db_id);
                            startActivity(intent);
                        }
                    });
                    myList.setAdapter(myCursorAdapter);
            }
        }

    }


}
