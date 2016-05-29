package dev.siniy.atmsearch;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    Button btnLogin;
    TextView tvRegister, tvMsg;
    EditText edtUsername, edtPassword;
    SessionManager session;
    String username, password;
    private static String KEY_SUCCESS = "success";
    private static String KEY_MSG = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Tạo action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.home);
        //Tạo biến session để lưu thông tin đăng nhập
        session = new SessionManager(this);
        //Ánh xạ các phần tử
        init();

        //Sự kiện đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtUsername.getText().toString();
                password = edtPassword.getText().toString();
                if(username.equals("")){
                    tvMsg.setText("Tài khoản không được phép rỗng");
                }else if(password.equals("")){
                    tvMsg.setText("Mật khẩu không được phép rỗng");
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new LoginTask().execute();
                        }
                    });
                }
            }
        });

    }

    private class LoginTask extends AsyncTask<String, Integer, String>{

        ProgressDialog myDialog = new ProgressDialog(Login.this);

        @Override
        protected void onPreExecute() {
            myDialog.setMessage("Đang đăng nhập! ");
            myDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return POST_URL("http://192.168.1.71:81/atmsearch/appLogin", null);
        }

        @Override
        protected void onPostExecute(String s) {
            myDialog.dismiss();

            try {
                JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.getInt(KEY_SUCCESS) == 1){
                    String sessionUserID = String.valueOf(jsonObject.getInt("id"));
                    String sessionUsername = jsonObject.getString("user");
                    String sessionFullname = jsonObject.getString("fullname");
                    String sessionPermission = String.valueOf(jsonObject.getInt("permission"));
                    String sessionStatus = jsonObject.getString("status");
                    session.createLoginSession(sessionUserID, sessionUsername, sessionFullname,
                            sessionPermission, sessionStatus);
                    startActivity(MainActivity
                            .createIntent(getApplicationContext()));
                    finish();
                }else{
                    tvMsg.setText(jsonObject.getString(KEY_MSG).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            tvMsg.setText(s);


//            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    }


    private String POST_URL(String url, String type) {

        HttpClient httpClient = new DefaultHttpClient();

        // URL của trang web nhận request
        HttpPost httpPost = new HttpPost(url);

        // Các tham số truyền
        List nameValuePair = new ArrayList(2);
        nameValuePair.add(new BasicNameValuePair("username", edtUsername.getText().toString()));

        nameValuePair.add(new BasicNameValuePair("password", edtPassword.getText().toString()));


        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String kq = "";
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            kq = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return kq;
    }


    private void init(){
        btnLogin = (Button) findViewById(R.id.btnDangNhap);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvMsg = (TextView) findViewById(R.id.msg);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
    }
}
