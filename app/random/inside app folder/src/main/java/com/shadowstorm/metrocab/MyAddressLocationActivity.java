package com.shadowstorm.metrocab;

        import android.app.ActionBar;
        import android.location.Address;
        import android.location.Geocoder;
        import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.Button;
        import android.widget.Toast;

        import java.io.IOException;
        import java.util.List;
        import java.util.Locale;


public class MyAddressLocationActivity extends ActionBarActivity {
    Button Button;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address_location);
        Button = (Button)findViewById(R.id.revGeocodeButton);
        GPSTracker gps = new GPSTracker(this);



        Geocoder geocoder;
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            // String country = addresses.get(0).getCountryName();
            // String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            //String city = addresses.get(0).getLocality();

            //Toast.makeText(this,"Lat:  "+latitude+ "Lon: "+longitude,Toast.LENGTH_LONG).show();


        } catch (IOException e) {
            e.printStackTrace();
        }

        //String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        //String city = addresses.get(0).getLocality();
        // String state = addresses.get(0).getAdminArea();
        // String country = addresses.get(0).getCountryName();
        // String postalCode = addresses.get(0).getPostalCode();
        // String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL


        // Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());

        //double latitude = gps.getLatitude();
        //double longitude = gps.getLongitude();
        // Toast.makeText(this,"Lat:  "+country+latitude + "Lon: "+longitude,Toast.LENGTH_LONG).show();



       // Toast.makeText(this,"Lat: "+ latitude + "Lon: "+longitude,Toast.LENGTH_LONG).show();
    }





    public void onGeoButton(){
        //Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());

        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

       // Toast.makeText(this,"Lat: "+ latitude + "Lon: "+longitude,Toast.LENGTH_LONG).show();








    }











    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_address_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
