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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        Bundle bundle = getIntent().getExtras();

        final EditText cno =findViewById(R.id.cno);
        final EditText discount =findViewById(R.id.discout);
        RadioGroup radio = (RadioGroup) findViewById(R.id.radiostaus);
        int idselected= radio.getCheckedRadioButtonId();

        RadioButton radioButton= (RadioButton) findViewById(idselected);
        final String status= radioButton.getText().toString();
        Button done =findViewById(R.id.Done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

