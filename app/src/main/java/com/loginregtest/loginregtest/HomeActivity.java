package com.loginregtest.loginregtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private ListView lv_Countries;
    private Toolbar toolbar;
    private ArrayList <String> arrayListCountris;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        lv_Countries = (ListView) findViewById(R.id.lv_Countries);
        arrayListCountris = new ArrayList<>();
        final ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayListCountris);
        lv_Countries.setAdapter(itemsAdapter);
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.get("http://services.groupkt.com/country/get/all")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray result = response.getJSONObject("RestResponse").getJSONArray("result");
                            for (int i=0;i<result.length();i++)
                            {
                                arrayListCountris.add(result.getJSONObject(i).getString("name"));
                                itemsAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_logout:
                LogOut();
        }
        return super.onOptionsItemSelected(item);
    }
    public  void LogOut()
    {
        FirebaseAuth.getInstance().signOut();

        startActivity(new Intent(HomeActivity.this,LogInActivity.class));
        finish();

    }
}
