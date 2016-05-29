package dev.siniy.atmsearch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button btnMap, btnSearch, btnLogin, btnFavorite, btnInfo;
    SessionManager session;
    public static String userid;

    //Double back to exit
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Tạo đối tượng session
        session = new SessionManager(this);


        //Ánh xạ các phần tử
        init();

        //Xử lý trường hợp đã đăng nhập
        if(session.isLoggedIn() == true){
            TextView tvName = (TextView) findViewById(R.id.tvName);
            TextView tvAction = (TextView) findViewById(R.id.tvLogin);
            ImageView btnLogout = (ImageView) findViewById(R.id.imgLogout);

            HashMap<String, String> user = session.getUserDetails();
            userid = user.get(SessionManager.KEY_S_USERID);
            String name = user.get(SessionManager.KEY_S_USER);
            String permission = user.get(SessionManager.KEY_S_PRIV);
            if (Integer.valueOf(permission) == 1) {
                tvName.setTextColor(Color.RED);
                permission = "Quản lý";
            } else {
                tvName.setTextColor(Color.BLACK);
                permission = "Người dùng";
            }
            btnLogin.setVisibility(View.GONE);
            btnInfo.setVisibility(View.VISIBLE);
            btnFavorite.setEnabled(true);
            btnLogout.setVisibility(View.VISIBLE);
            tvAction.setText("Thông tin");
            tvName.setText(permission + ": " + name);

            //Sự kiện cho nút đăng xuất
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Đăng xuất");
                    builder.setIcon(R.drawable.logout);
                    builder.setMessage("Bạn có chắc muốn đăng xuất?");
                    builder.setPositiveButton("Có",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    session.logoutUser();
                                }
                            });
                    builder.setNegativeButton("Không",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    builder.show();
                }
            });

            btnInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, Info.class));
                }
            });

        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Search.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), "Bấm thêm một lần để thoát!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

    private void init(){
        btnMap = (Button) findViewById(R.id.btnMap);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnLogin = (Button) findViewById(R.id.btnDangNhap);
        btnInfo = (Button) findViewById(R.id.btnInfo);
        btnFavorite = (Button) findViewById(R.id.btnFavorite);
    }

    public static Intent createIntent(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }
}
