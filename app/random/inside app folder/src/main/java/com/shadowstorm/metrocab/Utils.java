package com.shadowstorm.metrocab;

        import android.content.Context;
        import android.os.Looper;
        import android.util.Log;
        import android.widget.Toast;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.net.URLConnection;

/**
 * Created by Shadow Storm on 5/24/2016.
 */
public class Utils {
    public static final String serverIP = "http://omkishan.com.np/metrocab";
    public static void updateLocationInDatabase(final Context con, final String user, final String lat, final String lon){
        Log.e("code", "Trying to update database");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    URL url = new URL(serverIP+"/update.php?username="+user+"&latitude="+lat+"&longitude="+lon);

                    URLConnection urlConnection = url.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    InputStream in = urlConnection.getInputStream();

                    Log.e("code", "waiting");

                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String code = br.readLine();
                    //Toast.makeText(con, "Database updated", Toast.LENGTH_SHORT).show();
                    Log.e("code", code);
                } catch (IOException e) {
                    e.printStackTrace();
                    //Toast.makeText(con,"Database Update failed!\n"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("code", "Database Update failed");
                }
                Looper.loop();
            }
        }).start();
    }
}
