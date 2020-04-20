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

public class payment extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        final EditText cno =findViewById(R.id.cno);
        final EditText discount =findViewById(R.id.discout);
        RadioGroup radio = (RadioGroup) findViewById(R.id.radiostaus);
        int idselected= radio.getCheckedRadioButtonId();

         final RadioButton radioButton= (RadioButton) findViewById(idselected);

        Button done =findViewById(R.id.Done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Toast.makeText(payment.this, "invalid entry", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}

