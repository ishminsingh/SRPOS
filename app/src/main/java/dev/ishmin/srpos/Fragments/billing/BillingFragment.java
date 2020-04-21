package dev.ishmin.srpos.Fragments.billing;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dev.ishmin.srpos.MainActivity;
import dev.ishmin.srpos.Payment;
import dev.ishmin.srpos.R;
import dev.ishmin.srpos.ScannerActivity;

public class BillingFragment extends Fragment {
    public static TextView textView;
    static String res = null;
    static String name;
    static String mrp;
    static List<String> productlist;
    public static ListView billing;
    static ArrayAdapter<String> arrayAdapter;
     public static String sku;
     public static int flag1;

    static  int index;
   static List<String> productname = new ArrayList<String>();
    static List<String> productcategory = new ArrayList<String>();
   static List<String> productsubcategory = new ArrayList<String>();
    static List<String> productbrand = new ArrayList<String>();
   static List<String> productbuyrate = new ArrayList<String>();
   static List<String> productmrp = new ArrayList<String>();
   static List<String> productsku = new ArrayList<String>();
   static List<String> productquantity = new ArrayList<String>();
   static List<String> productsupplier = new ArrayList<String>();
    static List<String> productunit = new ArrayList<String>();

    static float total=0;
    Button totalbutton;
    TextView totalview;
    Button qscanner;

    public static void entry()
    {

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

            String update = tempname + "  "+quantity+"  " + Float.toString(tempMRP);

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
                Cursor c = MainActivity.SRPOS.rawQuery("SELECT * FROM Products1 WHERE sku="+Long.parseLong(sku), null);
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
                    productname.add(c.getString(name));
                    productcategory.add(c.getString(category));
                    productsubcategory.add(c.getString(subcategory));
                    productbrand.add(c.getString(brand));
                    productmrp.add(Float.toString(c.getFloat(mrp)));
                    productsku.add(Long.toString(c.getLong(sku)));
                    productsupplier.add(c.getString(supplier));
                    productunit.add(Integer.toString(c.getInt(unit)));
                    productbuyrate.add(Float.toString(c.getFloat(buyrate)));

                    productquantity.add("1");
                    total += c.getFloat(mrp);
                    String newitem = c.getString(name) +"  1   "+  Float.toString(c.getFloat(mrp));
                    productlist.add(newitem);
                    arrayAdapter.notifyDataSetChanged();
                    c.moveToNext();
                }

            } catch (Exception e) {
                //Toast.makeText(,"not scanned",Toast.LENGTH_SHORT).show();
                Log.i("Exception", e.getMessage());
            }
        }

    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_billing, container, false);

        String sku="";
        flag1=1;
        //textView = v.findViewById(R.id.txtView);

        productlist = new ArrayList<String>();
        billing = v.findViewById(R.id.billinglist);
        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, productlist);
        billing.setAdapter(arrayAdapter);
        totalbutton = v.findViewById(R.id.totalbutton);
        totalview = v.findViewById(R.id.totaldisplay);
        qscanner = v.findViewById(R.id.scanner);

        final Button payment = v.findViewById(R.id.payment);

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //payment activity.

                try
                {
                MainActivity.SRPOS.execSQL("CREATE TABLE IF NOT EXISTS Sales(billid INTEGER PRIMARY KEY, customerno INT(10) ,date DATE,billamount FLOAT,discount FLOAT, status VARCHAR)");
                MainActivity.SRPOS.execSQL("CREATE TABLE IF NOT EXISTS Solditems(id INTEGER PRIMARY KEY, name VARCHAR ,mrp FLOAT,  quantity INTEGER,unit VARCHAR,date DATE)");
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                for(int i =0; i<productlist.size();i++)
                {
                    MainActivity.SRPOS.execSQL("INSERT INTO Solditems(name,mrp,quantity,unit,date)VALUES('"+productname.get(i)+"',"+Float.parseFloat(productmrp.get(i))+","+Integer.parseInt(productquantity.get(i))+","+productunit.get(i)+",'"+date+"')");

                }
                Intent i = new Intent(getActivity(), Payment.class); //open scanner
                startActivity(i);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        totalbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productlist.isEmpty())
                {
                    Toast.makeText(getActivity(),"Please scan",Toast.LENGTH_SHORT).show();
                }
                else
                    totalview.setText(Float.toString(total));
            }
        });

// put qr action listener here ..and return the code scanned in String sku.

        qscanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), ScannerActivity.class); //open scanner
                startActivity(i);

                //
            }
        });

        return v;
    }


}
