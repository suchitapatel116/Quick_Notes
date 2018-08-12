package com.example.harshit.sms1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import Utility.HttpManager;
import Utility.RequestPackage;

/**
 *
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button login;
    String un, pw;
    EditText username, password;
    TextView btnstaff, btnfpss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        login = (Button) findViewById(R.id.btnlogin) ;

        username =(EditText) findViewById(R.id.ed1);
        password=(EditText) findViewById(R.id.ed2);
        btnstaff = findViewById(R.id.ays);
        btnfpss = findViewById(R.id.fp);

        login.setOnClickListener(this);
        btnstaff.setOnClickListener(this);
        btnfpss.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        un = username.getText().toString();
        pw = password.getText().toString();

        WebTask wt = new WebTask();
        wt.execute("http://172.20.10.3:12037/StudentService/Login");

        if (v.getId() == R.id.ays) {
            startActivity(new Intent(HomeActivity.this, StaffLoginActivity.class));
        } else if (v.getId() == R.id.fp) {
            startActivity(new Intent(HomeActivity.this, ForgotPasswordActivity.class));
            finish();
        }
    }

    private class WebTask extends AsyncTask<String,String,String> {
        ProgressDialog pd = null;
        String un, pw;

        @Override
        protected String doInBackground(String... strings) {
            String uri = strings[0];
            RequestPackage rp = new RequestPackage();
            rp.setMethod("GET");

            rp.setParam("un", un);
            rp.setParam("pw", pw);

            rp.setUri(uri);

            String ans = HttpManager.getData(rp);

            return ans;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            un = username.getText().toString();
            pw = password.getText().toString();

            pd = new ProgressDialog(HomeActivity.this);
            pd.setTitle("Verifying...");
            pd.setMessage("Please wait");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected void onPostExecute(String s) {

            String jmp = null;

            try {
                JSONObject reader = new JSONObject(jmp);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if(pd!=null){
                pd.dismiss();
            }


            if(s.toUpperCase().startsWith("SUCCESS")){


                Intent in = new Intent(HomeActivity.this, StudentHomeActivity.class);
                startActivity(in);
                finish();
            }else{
                Toast.makeText(HomeActivity.this, "Invalid username or password !!", Toast.LENGTH_LONG).show();
            }
        }
    }
}