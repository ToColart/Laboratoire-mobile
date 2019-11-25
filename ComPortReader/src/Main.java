import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.*;

public class Main {
    public static void main(String[] args) throws Exception {

        SerialReader main = new SerialReader();
        main.initialize();
        Thread t=new Thread() {
            public void run() {
                //the following line will keep this app alive for 1000 seconds,
                //waiting for events to occur and responding to them (printing incoming messages to console).
                try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
            }
        };
        t.start();
        System.out.println("Started");

        /*String jsonTest = "{\"id_destination\": 1,\"luminosity\": 0,\"temperature\":25.0,\"humidity\":80.0 }";
        JSONObject weather = new JSONObject(jsonTest);
        System.out.println(weather.getInt("id_destination"));
        System.out.println(weather.getDouble("temperature"));
        System.out.println(weather.getDouble("humidity"));
        System.out.println(weather.toString());*/
    }

}
