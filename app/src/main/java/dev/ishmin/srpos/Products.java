package dev.ishmin.srpos;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


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
        final EditText search = findViewById(R.id.search);

        try


            {
                Cursor c = MainActivity.SRPOS.rawQuery("SELECT * FROM Products ", null);
                int name = c.getColumnIndex("name");

                int brand = c.getColumnIndex("brand");
                int stock = c.getColumnIndex("stock");
                c.moveToFirst();

                while (!c.isAfterLast()) {
                    //Log.i("name", c.getString(name));
                    //Log.i("sku", c.getString(sku));
                    String newitem=c.getString(name)+"     "+c.getString(brand)+"     "+c.getInt(stock);
                    productlist.add(newitem);
                    arrayAdapter.notifyDataSetChanged();

                }
            }
        catch (Exception e)
        {
            e.printStackTrace();

        }


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {

                    if(!search.getText().equals(""))
                    {
                    Cursor c = MainActivity.SRPOS.rawQuery("SELECT * FROM Products WHERE name LIKE'"+search.getText().toString()+"%'", null);
                        int name = c.getColumnIndex("name");

                        int brand = c.getColumnIndex("brand");
                        int stock = c.getColumnIndex("stock");
                        c.moveToFirst();

                        while (!c.isAfterLast()) {

                            String newitem=c.getString(name)+"     "+c.getString(brand)+"     "+c.getInt(stock);
                            productlist.add(newitem);
                            arrayAdapter.notifyDataSetChanged();

                      }
                    }
                    else
                        {
                            Cursor c = MainActivity.SRPOS.rawQuery("SELECT * FROM Products ", null);
                            int name = c.getColumnIndex("name");

                            int brand = c.getColumnIndex("brand");
                            int stock = c.getColumnIndex("stock");
                            c.moveToFirst();

                            while (!c.isAfterLast()) {

                                String newitem=c.getString(name)+"     "+c.getString(brand)+"     "+c.getInt(stock);
                                productlist.add(newitem);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



                }


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
    }*/

