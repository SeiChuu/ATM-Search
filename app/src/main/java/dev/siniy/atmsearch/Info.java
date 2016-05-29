package dev.siniy.atmsearch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import java.util.HashMap;
import java.util.List;

public class Info extends AppCompatActivity {

    TextView info_userid, info_priv, info_status;
    EditText info_username, info_password, info_fullname;
    Button btnUpdate, btnSave;
    SessionManager session;

    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //Tạo action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.home);

        //Tạo biến session để lưu thông tin đăng nhập
        session = new SessionManager(this);
        //Ánh xạ các phần tử
        init();

        HashMap<String, String> s_user = session.getUserDetails();
        userid = s_user.get(SessionManager.KEY_S_USERID);
        String user = s_user.get(SessionManager.KEY_S_USER);
        String name = s_user.get(SessionManager.KEY_S_NAME);
        String stt = s_user.get(SessionManager.KEY_S_STT);
        String priv = s_user.get(SessionManager.KEY_S_PRIV);

        if (Integer.parseInt(priv) == 1) {
            priv = "Quản lý";
        } else {
            priv = "Người dùng";
        }
        if (stt.equals("true")) {
            stt = "Hoạt động";
        } else {
            stt = "Banned";
        }
        info_userid.setText("Mã tài khoản: " + userid);
        info_fullname.setText(name);
        info_username.setText(user);
        info_status.setText(stt);
        info_priv.setText(priv);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSave.setEnabled(true);
                info_fullname.setEnabled(true);
                info_username.setEnabled(true);
                info_password.setEnabled(true);
                info_fullname.setFocusableInTouchMode(true);
                info_username.setFocusableInTouchMode(true);
                info_password.setFocusableInTouchMode(true);
                info_username.requestFocus();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = info_fullname.getText().toString();
                String user = info_username.getText().toString();
                String pass = info_password.getText().toString();
                if (user.length() < 3) {
                    showAlertDialog(Info.this, "Thất bại",
                            "Tài khoản phải nhiều hơn 3 ký tự", false);
                    info_username.requestFocus();
                } else if (name.length() < 6) {
                    showAlertDialog(Info.this, "Thất bại",
                            "Tên người dùng  phải nhiều hơn 6 ký tự", false);
                    info_fullname.requestFocus();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new UpdateUser().execute("http://192.168.1.71:81/atmsearch/user/" + userid);
                        }
                    });
                }
            }
        });


    }

    class UpdateUser extends AsyncTask<String, Integer, String>{

        ProgressDialog MyDialog = new ProgressDialog(Info.this);

        @Override
        protected void onPreExecute() {
            MyDialog.setMessage("Đang cập nhật tài khoản! ");
            MyDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return POST_URL(params[0], "PUT");
        }

        @Override
        protected void onPostExecute(String s) {
            MyDialog.dismiss();
            try {
                JSONObject json = new JSONObject(s);
                if (!json.getString("fullname").equals("")) {
                    showAlertDialog(Info.this, "Thành công", "Cập nhật tài khoản thành công", true);
                    info_fullname.setEnabled(false);
                    info_username.setEnabled(false);
                    info_password.setEnabled(false);
                    info_fullname.setFocusable(false);
                    info_username.setFocusable(false);
                    info_password.setFocusable(false);
                    session.clear();
                    String sessionUserID = String.valueOf(json.getInt("id"));
                    String sessionUsername = json.getString("user");
                    String sessionFullname = json.getString("fullname");
                    String sessionPermission = String.valueOf(json.getInt("permission"));
                    String sessionStatus = json.getString("status");
                    session.createLoginSession(sessionUserID, sessionUsername, sessionFullname,
                            sessionPermission, sessionStatus);
                    String priv, stt;
                    if (Integer.parseInt(sessionPermission) == 1) {
                        priv = "Quản lý";
                    } else {
                        priv = "Người dùng";
                    }
                    if (sessionStatus.equals("true")) {
                        stt = "Hoạt động";
                    } else {
                        stt = "Banned";
                    }
                    info_userid.setText("Mã tài khoản: " + sessionUserID);
                    info_fullname.setText(sessionFullname);
                    info_username.setText(sessionUsername);
                    info_status.setText(stt);
                    info_priv.setText(priv);
                } else {
                    showAlertDialog(Info.this, "Thất bại", "Cập nhật tài khoản thành công", false);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private String POST_URL(String url, String type) {
        HttpClient httpClient = new DefaultHttpClient();

        // URL của trang web nhận request
        HttpPost httpPost = new HttpPost(url);

        // Các tham số truyền
        List nameValuePair = new ArrayList(3);
        nameValuePair.add(new BasicNameValuePair("username", info_username.getText().toString()));


        nameValuePair.add(new BasicNameValuePair("password", info_password.getText().toString()));

        nameValuePair.add(new BasicNameValuePair("fullname", info_fullname.getText().toString()));

        nameValuePair.add(new BasicNameValuePair("_method", type));

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

    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }

    private void init() {
        info_userid = (TextView) findViewById(R.id.info_userid);
        info_priv = (TextView) findViewById(R.id.info_priv);
        info_status = (TextView) findViewById(R.id.info_status);
        info_username = (EditText) findViewById(R.id.info_username);
        info_password = (EditText) findViewById(R.id.info_password);
        info_fullname = (EditText) findViewById(R.id.info_fullname);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnSave = (Button) findViewById(R.id.btnSubmit);
    }
}
