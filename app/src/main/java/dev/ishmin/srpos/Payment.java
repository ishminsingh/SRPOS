package dev.ishmin.srpos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Payment extends AppCompatActivity {

    Button done;
     EditText cno ;
     EditText discount;
     RadioGroup radio ;
     RadioButton radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        Bundle bundle = getIntent().getExtras();
        cno =findViewById(R.id.cno);
        discount =findViewById(R.id.discout);
        done =findViewById(R.id.Done);

        radio = (RadioGroup) findViewById(R.id.radiostaus);



        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idselected= radio.getCheckedRadioButtonId();
                radioButton= (RadioButton) findViewById(idselected);
                String status= (String) radioButton.getText();
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                if (cno.getText().toString().length() == 10 && discount.getText().toString().length() > 0)
                {
                    try {

                        MainActivity.SRPOS.execSQL("INSERT INTO Sales(customerno,date,billamount,discount,status)VALUES('" + Integer.parseInt(cno.getText().toString() )+ "','" + date + "','" + Billing.total + "'," + Float.parseFloat(discount.getText().toString()) + ",'" + status + "')");
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(Payment.this, "invalid entry", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}

