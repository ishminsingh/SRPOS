package dev.ishmin.srpos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Billing extends AppCompatActivity {

    static String res = null;
    static String name;
    static String mrp;
    Button read;
    List<String> productlist;
    ListView billing;
    ArrayAdapter<String> arrayAdapter;
    String sku;
    int index;
    List<String> productname = new ArrayList<String>();
    List<String> productmrp = new ArrayList<String>();
    List<String> productsku = new ArrayList<String>();
    int total=0;
    Button totalbutton;
    TextView totalview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        productlist = new ArrayList<String>();
        billing = findViewById(R.id.billinglist);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, productlist);
        billing.setAdapter(arrayAdapter);
        totalbutton=findViewById(R.id.totalbutton);
        totalview=findViewById(R.id.totaldisplay);

        totalbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productlist.isEmpty())
                {
                    Toast.makeText(Billing.this,"Please scan",Toast.LENGTH_SHORT).show();

                }
                else
                    totalview.setText(Integer.toString(total));
            }
        });


// put qr action listener here ..and return the code scanned in String sku.


        if(productsku.contains(sku))
        {
            index = productsku.indexOf(sku);

            String tempmrp=productmrp.get(index);

             int tempMRP=Integer.parseInt(tempmrp);

             String tempproduct=productlist.get(index);
             String[] split= tempproduct.split("\\s");
             tempMRP+=Integer.parseInt(split[1]);
             String update=split[0]+Integer.toString(tempMRP);

            productlist.set(index,update);
            arrayAdapter.notifyDataSetChanged();
        }

        else
        {
            try {
                String myUrl = "http://smartretailpos.pe.hu/api/products.php?sku=" + sku;
                String returned;
                Connection connection = new Connection();
                returned = connection.execute(myUrl).get();


            } catch (Exception e) {
                Log.i("Exception", e.getMessage());
            }
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
                for (int i = 0; i < array.length(); i++)
                {
                    JSONObject object = array.getJSONObject(i);
                    name = object.getString("name");
                    mrp = object.getString("mrp");

                    newitem=name+" "+mrp;
                    productlist.add(newitem);
                    arrayAdapter.notifyDataSetChanged();

                    productlist.add(name);
                    productmrp.add(mrp);
                    productsku.add(sku);
                    total+=Integer.parseInt(mrp);
                }

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
