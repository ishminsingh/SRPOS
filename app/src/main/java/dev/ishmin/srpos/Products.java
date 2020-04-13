package dev.ishmin.srpos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Products extends AppCompatActivity {


    static String res = null;
    static String name;
    static String mrp;
    Button read;
    List<String> productlist;
    ListView products;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);



        productlist = new ArrayList<String>();
        products = findViewById(R.id.productlist);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, productlist);
        products.setAdapter(arrayAdapter);



                try {
                    String myUrl = "http://smartretailpos.pe.hu/api/products.php?sku=all";
                    String returned;
                    Connection connection = new Connection();
                    returned = connection.execute(myUrl).get();


                } catch (Exception e)
                {
                    Log.i("Exception", e.getMessage());
                }


    }

    class Connection extends AsyncTask<String, Void, String> {

        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];
            String result1;
            String inputLine;
            try {
                URL myUrl = new URL(stringUrl);
                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();

                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.connect();
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());

                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while ((inputLine = reader.readLine()) != null)
                {
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();

                result1 = stringBuilder.toString();
                Log.i("JSON",result1);
                return result1;



            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Not Available", Toast.LENGTH_SHORT).show();
                String y = null;
                return (y);
            }

        }

        @Override
        protected void onPostExecute(String result1) {
            try {

                String newitem;
                JSONArray array = new JSONArray(result1);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    name = object.getString("name");
                    mrp = object.getString("mrp");

                    newitem=name+" "+mrp;
                    productlist.add(newitem);
                    arrayAdapter.notifyDataSetChanged();
                }

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
