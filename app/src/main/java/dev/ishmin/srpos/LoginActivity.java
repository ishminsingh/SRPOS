package dev.ishmin.srpos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private EditText mobileNum;
    protected static String sessionCode="";
    String mobile;

    public static class session extends AsyncTask<String,Void,String>
    {
        static final String REQUEST_METHOD = "GET";
        static final int READ_TIMEOUT = 15000;
        static final int CONNECTION_TIMEOUT = 15000;
        @Override
        protected String doInBackground(String... strings)
        {
            URL url ;
            HttpURLConnection httpURLConnection=null;
            String result ="";
            String inputLine;

            try{

                url = new URL(strings[0]);
                httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod(REQUEST_METHOD);
                httpURLConnection.setReadTimeout(READ_TIMEOUT);
                httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                httpURLConnection.connect();

                InputStreamReader streamReader = new InputStreamReader(httpURLConnection.getInputStream());

                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();

                while ((inputLine = reader.readLine()) != null)
                {
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();

                result = stringBuilder.toString();
                return result;
            }

            catch (Exception e){
                e.printStackTrace();
                Log.i("async","Error in Async");
                return "";

            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("on post",s);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mobileNum = findViewById(R.id.mobileNum);
        findViewById(R.id.contBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = mobileNum.getText().toString().trim();
                if (mobile.length() != 10) {
                    mobileNum.setError("Enter a valid Phone number");
                    mobileNum.requestFocus();
                }
               else {
                    try {
                        String myurl="http://smartretailpos.pe.hu/api/auth.php?action=getdata&cno="+ mobile;
                        session newsession = new session();
                        sessionCode = newsession.execute(myurl).get();
                        Log.i("basic",sessionCode);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        Log.i("basic","failed");

                    }
                    if (sessionCode.length()>1)
                    {
                        Intent intent = new Intent(LoginActivity.this, VerifyOtpActivity.class);
                        intent.putExtra("mobile", mobile);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "USER NOT REGISTERED", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
