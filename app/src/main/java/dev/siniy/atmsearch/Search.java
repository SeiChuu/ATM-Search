package dev.siniy.atmsearch;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Search extends AppCompatActivity {

    Spinner spnBank, spnProvince, spnDistrict;
    Button btnSearch;
    ListView lvPlace;


    ArrayList<Bank> listBank = new ArrayList<Bank>();
    ArrayList<District> listDistrict = new ArrayList<District>();
    ArrayList<Province> listProvince = new ArrayList<Province>();
    ArrayList<Place> listPlace = new ArrayList<Place>();

    String idProvince;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Tạo action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.home);

        //Ánh xạ các phần tử layout
        init();

        //Load danh sách ngân hàng
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GetBank().execute("http://192.168.1.71:81/atmsearch/search/listBank");
            }
        });

        //Bắt sự kiện khi chọn 1 tỉnh thành nào đó
        spnProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                idProvince = listProvince.get(i).getIdProvince();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Hiển thị danh sách dựa theo tỉnh thành được chọn
                        new GetDistrict().execute("http://192.168.1.71:81/atmsearch/search/listDistrict/" + idProvince);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        District indexDistrict = listDistrict.get(spnDistrict
                                .getSelectedItemPosition());
                        Bank indexBank = listBank.get(spnBank
                                .getSelectedItemPosition());
                        String url = "http://192.168.1.71:81/atmsearch/search/place/" + indexDistrict.getIdDistrict() + "/" + indexBank.getIdBank();
                        new GetPlace().execute(url);
                    }
                });
            }
        });
    }

    private class GetBank extends AsyncTask<String, Integer, String>{

        ProgressDialog MyDialog = new ProgressDialog(Search.this);

        @Override
        protected void onPreExecute() {
            MyDialog.setMessage("Đang load dữ liệu ngân hàng!");
            MyDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return GET_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            MyDialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(s);
                listBank.clear();
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String idBank = jsonObject.getString("bankCode");
                    String nameBank = jsonObject.getString("name");
                    String logoBank = jsonObject.getString("logo");
                    listBank.add(new Bank(idBank, nameBank, logoBank));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayAdapter<Bank> adapter = new ArrayAdapter<Bank>(Search.this, android.R.layout.simple_spinner_item, listBank);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnBank.setAdapter(adapter);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new GetProvince().execute("http://192.168.1.71:81/atmsearch/search/listProvince");
                }
            });

        }
    }

    private class GetProvince extends AsyncTask<String, Integer, String>{
        ProgressDialog MyDialog = new ProgressDialog(Search.this);

        @Override
        protected void onPreExecute() {
            MyDialog.setMessage("Đang load dữ liệu tỉnh thành!");
            MyDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return GET_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            MyDialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(s);
                listProvince.clear();
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String idProvince = String.valueOf(jsonObject.getInt("id"));
                    String nameProvince = jsonObject.getString("name");
                    listProvince.add(new Province(idProvince, nameProvince));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayAdapter<Province> adapter = new ArrayAdapter<Province>(Search.this, android.R.layout.simple_spinner_item, listProvince);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnProvince.setAdapter(adapter);
        }
    }

    private class GetDistrict extends AsyncTask<String, Integer, String>{

        ProgressDialog MyDialog = new ProgressDialog(Search.this);

        @Override
        protected void onPreExecute() {
            MyDialog.setMessage("Đang load dữ liệu quận huyện!");
            MyDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return GET_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            MyDialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(s);
                listDistrict.clear();
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String idDistrict = String.valueOf(jsonObject.getInt("id"));
                    String nameDistrict = jsonObject.getString("name");
                    listDistrict.add(new District(idDistrict, nameDistrict));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayAdapter<District> adapter = new ArrayAdapter<District>(Search.this, android.R.layout.simple_spinner_item, listDistrict);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnDistrict.setAdapter(adapter);
        }
    }

    private class GetPlace extends AsyncTask<String, Integer, String>{

        ProgressDialog MyDialog = new ProgressDialog(Search.this);

        @Override
        protected void onPreExecute() {
            MyDialog.setMessage("Đang tìm kiếm!");
            MyDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return GET_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            MyDialog.dismiss();
//            Toast.makeText(Search.this, s, Toast.LENGTH_LONG).show();
            if(s.length() > 3){
                listPlace.clear();
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String name = jsonObject.getString("name");
                        String address = jsonObject.getString("address");
                        String logo = jsonObject.getString("logo");
                        Double lat = jsonObject.getDouble("lat");
                        Double lng = jsonObject.getDouble("lng");
                        listPlace.add(new Place(id, name, address, lat, lng, logo));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(Search.this, "Không có dữ liệu", Toast.LENGTH_LONG).show();
            }
            PlaceAdapter adapter = new PlaceAdapter(Search.this, R.layout.activity_place, listPlace);
            lvPlace.setAdapter(adapter);
        }
    }

    private static String GET_URL(String theUrl)
    {
        StringBuilder content = new StringBuilder();
        try
        {
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return content.toString();
    }

    private void init() {
        btnSearch = (Button) findViewById(R.id.btnSearch);
        spnBank = (Spinner) findViewById(R.id.spnBank);
        spnProvince = (Spinner) findViewById(R.id.spnProvince);
        spnDistrict = (Spinner) findViewById(R.id.spnDistrict);
        lvPlace = (ListView) findViewById(R.id.listView);
    }
}
