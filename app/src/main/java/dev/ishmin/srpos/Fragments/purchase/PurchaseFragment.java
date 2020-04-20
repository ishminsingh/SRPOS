package dev.ishmin.srpos.Fragments.purchase;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dev.ishmin.srpos.MainActivity;
import dev.ishmin.srpos.PurchaseNewItem;
import dev.ishmin.srpos.R;

public class PurchaseFragment extends Fragment {
    EditText name;
    EditText brand;
    EditText category;
    EditText subcategory;
    EditText sku;
    EditText buyrate;
    EditText mrp;
    EditText units;
    EditText quantity;
    EditText supplier;
    Button purchase;
    String scannerresult;
    int flag;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_purchase, container, false);


        name = v.findViewById(R.id.name);
        brand = v.findViewById(R.id.brand);
        category = v.findViewById(R.id.category);
        subcategory = v.findViewById(R.id.Subcategory);
        sku = v.findViewById(R.id.sku);
        buyrate = v.findViewById(R.id.buyrate);
        mrp = v.findViewById(R.id.mrp);
        units = v.findViewById(R.id.units);
        quantity = v.findViewById(R.id.quantity);
        supplier = v.findViewById(R.id.supplier);
        purchase = v.findViewById(R.id.purchase);
        Button scanner = v.findViewById(R.id.scanner);
        flag = 0;


        try {


            MainActivity.SRPOS.execSQL("CREATE TABLE IF NOT EXISTS PurchasedItems(id INTEGER PRIMARY KEY, name VARCHAR ,category VARCHAR, subcategory VARCHAR, brand VARCHAR ,sku INTEGER,buyrate FLOAT,mrp FLOAT,supplier VARCHAR,unit VARCHAR,quantity INTEGER,date DATE )");
            MainActivity.SRPOS.execSQL("CREATE TABLE IF NOT EXISTS Products(id INTEGER PRIMARY KEY, name VARCHAR ,category VARCHAR, subcategory VARCHAR, brand VARCHAR ,sku INTEGER,buyrate FLOAT,mrp FLOAT,supplier VARCHAR,unit VARCHAR,stock INTEGER)");

            // MainActivity.SRPOS.execSQL("CREATE TABLE IF NOT EXISTS Purchases(purchaseid INTEGER PRIMARY KEY, supplier VARCHAR ,date DATE,purchaseamount FLOAT)");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1=name.getText().toString();
                String brand1=brand.getText().toString();
                String category1=category.getText().toString();
                String subcategory1=subcategory.getText().toString();
                String sku1=sku.getText().toString();
                String buyrate1=buyrate.getText().toString();
                String mrp1=mrp.getText().toString();
                String units1=units.getText().toString();
                String quantity1=quantity.getText().toString();
                String supplier1=supplier.getText().toString();

                if(name1.length()>0&&brand1.length()>0&&category1.length()>0&&subcategory1.length()>0&&sku1.length()>0&&buyrate1.length()>0&&mrp1.length()>0&&units1.length()>0&&quantity1.length()>0&&supplier1.length()>0)
                {
                    try {

                        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                        MainActivity.SRPOS.execSQL("INSERT INTO PurchasedItems(name,category,subcategory,brand,sku,buyrate, mrp,supplier,unit,quantiy,date)VALUES('" + name1 + "','" + category1 + "','" + subcategory1 + "'," + Integer.parseInt(sku1) + "," + Float.parseFloat(buyrate1) + "," +Float.parseFloat(mrp1)+ ",'" + supplier1 + "','" + units1 + "'," + Integer.parseInt(quantity1) + ",'"+date+"')");
                        if (flag==1)
                            MainActivity.SRPOS.execSQL("INSERT INTO Products(name,category,subcategory,brand,sku,buyrate, mrp,supplier,unit,stock)VALUES('" + name1 + "','" + category1 + "','" + subcategory1 + "'," +Integer.parseInt(sku1) + "," + Float.parseFloat(buyrate1) + "," + Float.parseFloat(mrp1) + ",'" + supplier1 + "','" + units1 + "'," + Integer.parseInt(quantity1)+ ")");
                        else
                        {
                            Cursor c = MainActivity.SRPOS.rawQuery("SELECT stock FROM Products WHERE sku="+scannerresult, null);
                            int quantity2 = c.getColumnIndex("stock");
                            int stock=c.getInt(quantity2);
                            int newstock =stock + Integer.parseInt(quantity1);
                            MainActivity.SRPOS.execSQL("UPADTE Products SET stock= "+newstock+" WHERE sku="+sku );
                        }


                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    flag=0;
                }

            }
        });
        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    //scannerresult=
                    Cursor c = MainActivity.SRPOS.rawQuery("SELECT * FROM Products WHERE sku="+sku, null);
                    int name2 = c.getColumnIndex("name");
                    int category2 = c.getColumnIndex("category");
                    int subcategory2 = c.getColumnIndex("subcategory");
                    int brand2 = c.getColumnIndex("brand");
                    int mrp2 = c.getColumnIndex("mrp");
                    int sku2 = c.getColumnIndex("sku");

                    int supplier2 = c.getColumnIndex("supplier");
                    int unit2 = c.getColumnIndex("unit");
                    int buyrate2=c.getColumnIndex("buyrate");

                    c.moveToFirst();

                    while (!c.isAfterLast()) {

                        name.setText(c.getString(name2));
                        category.setText(c.getString(category2));
                        subcategory.setText(c.getString(subcategory2));
                        brand.setText(c.getString(brand2));
                        mrp.setText(Float.toString(c.getFloat(mrp2)));
                        sku.setText(Integer.toString(c.getInt(sku2)));
                        supplier.setText(c.getString(supplier2));
                        units.setText(Integer.toString(c.getInt(unit2)));
                        buyrate.setText(Float.toString(c.getFloat(buyrate2)));

                        c.moveToNext();


                    }

                    flag=1;

                } catch (Exception e) {
                    Toast.makeText(getActivity(),"not scanned",Toast.LENGTH_SHORT).show();
                    Log.i("Exception", e.getMessage());
                }

            }
        });



        return v;
    }

}
