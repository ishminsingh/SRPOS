package dev.ishmin.srpos;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PurchaseNewItem extends AppCompatActivity {
EditText name=findViewById(R.id.name);
    EditText brand=findViewById(R.id.brand);
    EditText category=findViewById(R.id.category);
    EditText subcategory=findViewById(R.id.Subcategory);
    EditText sku=findViewById(R.id.sku);
    EditText buyrate=findViewById(R.id.buyrate);
    EditText mrp=findViewById(R.id.mrp);
    EditText units=findViewById(R.id.units);
    EditText quantity=findViewById(R.id.quantity);
    EditText supplier=findViewById(R.id.supplier);
    Button purchase=findViewById(R.id.purchase);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_new_item);
        //populate textviews..auto


        try {


            MainActivity.SRPOS.execSQL("CREATE TABLE IF NOT EXISTS PurchasedItems(id INTEGER PRIMARY KEY, name VARCHAR ,category VARCHAR, subcategory VARCHAR, brand VARCHAR ,sku INTEGER,buyrate FLOAT,mrp FLOAT,supplier VARCHAR,unit VARCHAR,quantity INTEGER,date DATE )");
            MainActivity.SRPOS.execSQL("CREATE TABLE IF NOT EXISTS Products(id INTEGER PRIMARY KEY, name VARCHAR ,category VARCHAR, subcategory VARCHAR, brand VARCHAR ,sku INTEGER,buyrate FLOAT,mrp FLOAT,supplier VARCHAR,unit VARCHAR,quantity INTEGER)");
            MainActivity.SRPOS.execSQL("CREATE TABLE IF NOT EXISTS Purchases(purchaseid INTEGER PRIMARY KEY, supplier VARCHAR ,date DATE,purchaseamount FLOAT)");
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
                           MainActivity.SRPOS.execSQL("INSERT INTO PurchasedItems(name,category,subcategory,brand,sku,buyrate, mrp,supplier,unit,quantiy,date)VALUES('" + name1 + "','" + category1 + "','" + subcategory1 + "'," + sku1 + "," + buyrate1 + "," + mrp1 + ",'" + units1 + "," + quantity1 + "'" + supplier1 + "')");
                           MainActivity.SRPOS.execSQL("INSERT INTO Products(name,category,subcategory,brand,sku,buyrate, mrp,supplier,unit,quantiy)VALUES('" + name1 + "','" + category1 + "','" + subcategory1 + "'," + sku1 + "," + buyrate1 + "," + mrp1 + ",'" + units1 + "," + quantity1 + "'" + supplier1 + "')");
                       }
                       catch (Exception e)
                       {
                           e.printStackTrace();
                       }
                }

            }
        });




        /*
        try {

            MainActivity.SRPOS.execSQL("CREATE TABLE IF NOT EXISTS PurchasedItems(id INTEGER PRIMARY KEY, name VARCHAR ,category VARCHAR, subcategory VARCHAR, brand VARCHAR ,sku INTEGER,buyrate FLOAT,mrp FLOAT,supplier VARCHAR,unit VARCHAR,quantity INTEGER,date DATE )");
            MainActivity.SRPOS.execSQL("CREATE TABLE IF NOT EXISTS Products(id INTEGER PRIMARY KEY, name VARCHAR ,category VARCHAR, subcategory VARCHAR, brand VARCHAR ,sku INTEGER,buyrate FLOAT,mrp FLOAT,supplier VARCHAR,unit VARCHAR,quantity INTEGER)");

            MainActivity.SRPOS.execSQL("INSERT INTO Products(name,category,subcategory,brand,sku,buyrate, mrp,supplier,unit)VALUES()");




            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/

    }
}
