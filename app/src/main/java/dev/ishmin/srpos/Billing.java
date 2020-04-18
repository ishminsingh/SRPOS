package dev.ishmin.srpos;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Billing extends AppCompatActivity {

    static String res = null;
    static String name;
    static String mrp;
    List<String> productlist;
    ListView billing;
    ArrayAdapter<String> arrayAdapter;
    String sku;
    int index;
    List<String> productname = new ArrayList<String>();
    List<String> productcategory = new ArrayList<String>();
    List<String> productsubcategory = new ArrayList<String>();
    List<String> productbrand = new ArrayList<String>();
    List<String> productbuyrate = new ArrayList<String>();
    List<String> productmrp = new ArrayList<String>();
    List<String> productsku = new ArrayList<String>();
    List<String> productquantity = new ArrayList<String>();
    List<String> productsupplier = new ArrayList<String>();
    List<String> productunit = new ArrayList<String>();

    float total=0;
    Button totalbutton;
    TextView totalview;
    Button scanner;



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
        scanner=findViewById(R.id.scanner);
          Button payment=findViewById(R.id.payment);

          payment.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  //payment activity.

                 MainActivity.SRPOS.execSQL("CREATE TABLE IF NOT EXISTS Sales(billid INTEGER PRIMARY KEY, customerno INT(10) ,date DATE,billamount FLOAT,discount FLOAT, status VARCHAR)");
                  MainActivity.SRPOS.execSQL("CREATE TABLE IF NOT EXISTS Solditems(id INTEGER PRIMARY KEY, name VARCHAR ,mrp FLOAT,  quantity INTEGER,unit VARCHAR,date DATE)");
                  String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                  for(int i =0; i<productlist.size();i++)
                  {
                  MainActivity.SRPOS.execSQL("INSERT INTO Solditems(name,mrp,quantity,unit,date)VALUES('"+productname.get(i)+"','"+productmrp.get(i)+"','"+productquantity.get(i)+"',"+productunit.get(i)+",'"+date+"')");


                  }
                  //intent
              }
          });


        totalbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productlist.isEmpty())
                {
                    Toast.makeText(Billing.this,"Please scan",Toast.LENGTH_SHORT).show();

                }
                else
                    totalview.setText(Float.toString(total));
            }
        });


// put qr action listener here ..and return the code scanned in String sku.



        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sku=entry.getText().toString();
                if(productsku.contains(sku))
                {
                    index = productsku.indexOf(sku);

                    String tempmrp=productmrp.get(index);
                    float tempMRP=Float.parseFloat(tempmrp.trim());
                    total+=tempMRP;
                    String tempname=productname.get(index);

                    String tempquantity=productquantity.get(index);
                    int quantity=Integer.parseInt(tempquantity.trim());
                    quantity++;
                    productquantity.set(index,Integer.toString(quantity));


                    tempMRP*=quantity;



                    String update=tempname+" "+Float.toString(tempMRP);

                   /* String tempproduct=productlist.get(index);
                    String[] split= tempproduct.split("\\s");
                    Log.i("splitting",split[1]);
                    tempMRP+=Integer.parseInt(split[1]);
                    String update=split[0]+(Float.toString(tempMRP));*/

                    productlist.set(index,update);
                    arrayAdapter.notifyDataSetChanged();
                }

                else
                {


                       /* String myUrl = "http://smartretailpos.pe.hu/api/products.php?sku=" + sku;
                        String returned;
                        Connection connection = new Connection();
                        returned = connection.execute(myUrl).get();*/
                    try {
                        Cursor c = MainActivity.SRPOS.rawQuery("SELECT * FROM Products WHERE sku="+sku, null);
                        int name = c.getColumnIndex("name");
                        int category = c.getColumnIndex("category");
                        int subcategory = c.getColumnIndex("subcategory");
                        int brand = c.getColumnIndex("brand");
                        int mrp = c.getColumnIndex("mrp");
                        int sku = c.getColumnIndex("sku");
                        int quantity = c.getColumnIndex("quantity");
                        int supplier = c.getColumnIndex("supplier");
                        int unit = c.getColumnIndex("unit");
                        int buyrate=c.getColumnIndex("buyrate");

                        c.moveToFirst();

                        while (!c.isAfterLast()) {
                            //Log.i("name", c.getString(name));
                            //Log.i("sku", c.getString(sku));
                            String newitem=c.getString(name)+" "+c.getString(sku);
                            productlist.add(newitem);
                            arrayAdapter.notifyDataSetChanged();

                            productname.add(c.getString(name));
                            productcategory.add(c.getString(category));
                            productsubcategory.add(c.getString(subcategory));
                            productbrand.add(c.getString(brand));
                            productmrp.add(c.getString(mrp).toString());
                            productsku.add(c.getString(sku).toString());
                            productsupplier.add(c.getString(supplier));
                            productunit.add(c.getString(unit).toString());
                            productbuyrate.add(c.getString(buyrate).toString());

                            productquantity.add("1");
                            total+=Float.parseFloat(c.getString(mrp).trim());
                            c.moveToNext();

                        }


                    } catch (Exception e) {
                        Toast.makeText(Billing.this,"not scanned",Toast.LENGTH_SHORT).show();
                        Log.i("Exception", e.getMessage());
                    }
                }
            }
        });

    }

   /* class Connection extends AsyncTask<String, Void, String> {

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
                    Log.i("mrp",mrp);
                    productlist.add(newitem);
                    arrayAdapter.notifyDataSetChanged();

                    productname.add(name);
                    productmrp.add(mrp);
                    productsku.add(sku);
                    productquantity.add("1");
                    total+=Float.parseFloat(mrp.trim());
                }

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }*/

}
