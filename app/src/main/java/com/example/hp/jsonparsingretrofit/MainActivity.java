package com.example.hp.jsonparsingretrofit;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.provider.ContactsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private RetroAdapter retroAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tv;
    private Button b1;
    DatabaseHelper mDatabaseHelper;
    ArrayList<String> user_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       mDatabaseHelper = new DatabaseHelper(this);

        listView = findViewById(R.id.lv);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        tv= (TextView) findViewById(R.id.tv);
        b1=(Button) findViewById(R.id.b1);
        getJSONResponse();

    }

    private void getJSONResponse()
    {
        Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(MyInterface.JSONURL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .build();

        MyInterface api =retrofit.create(MyInterface.class);
        Call<String> call = api.getString();


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Response String", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null)
                        Log.i("On Success", response.body().toString());

                     String jsonresponse = response.body().toString();
                    writeListView(jsonresponse);
                } else {
                    Log.i("onEmptyResponse", "Returned empty response");
                    Toast.makeText(MainActivity.this, "Nothing Returned", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    private void writeListView(final String response) {
        try {
            //getting the whole JSOn Array from the response
            //  JSONArray JA = new JSONArray(response);
            //  if (JA.optString("status").equals("true")) {
            //    JSONObject obj = new JSONObject(response);
            //    if (obj.optString("Status").equals("true")) {
            //       ArrayList<ModelListView> modelListViewArrayList = new ArrayList<>();


            JSONArray JA = new JSONArray(response);
            final ArrayList<ModelListView> modelListViewArrayList = new ArrayList<>();


            // looping through All Contacts
            for (int i = 0; i < JA.length(); i++) {
                ModelListView modelListView = new ModelListView();
                JSONObject c = JA.getJSONObject(i);

                modelListView.setName(c.getString("name"));
                modelListView.setEmail(c.getString("email"));


                // Phone node is JSON Object
                JSONObject address = c.getJSONObject("address");
                modelListView.setCity(address.getString("city"));
                modelListViewArrayList.add(modelListView);


                         String name1=c.getString("name");
                         String email1=c.getString("email");
                         String city1=address.getString("city");

                   Boolean insertData = mDatabaseHelper.addData(name1,email1,city1);

                    if(insertData)
                    {
                        Toast.makeText(this, "Data Inserted" +i, Toast.LENGTH_SHORT).show();
                    }

                    else
                    {
                        Toast.makeText(this, "Data not Inserted", Toast.LENGTH_SHORT).show();
                    }
            }



            final ArrayList<String> res=  mDatabaseHelper.getallData();
            tv.setText("Data is "+ res);
           // Toast.makeText(this, "Data is: "+res, Toast.LENGTH_SHORT).show();

            mDatabaseHelper.close();


            retroAdapter = new RetroAdapter(this,modelListViewArrayList);
            listView.setAdapter(retroAdapter);

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(MainActivity.this, "Long Clicked"+i, Toast.LENGTH_SHORT).show();

                      // ModelListView mv= modelListViewArrayList.get(i);
                     //  int uid=mv.getId();
                    //Toast.makeText(MainActivity.this, "Id is: "+uid, Toast.LENGTH_SHORT).show();


                  //  ModelListView mv = (ModelListView)listView.getItemAtPosition(i);
                   // ModelListView mv1 =modelListViewArrayList.get(i);
                   // Toast.makeText(MainActivity.this, "Id is"+ mv.getId(), Toast.LENGTH_SHORT).show();


                      Boolean a=mDatabaseHelper.deleteUser("Clementina DuBuque");
                       if (a)
                       {
                           Toast.makeText(MainActivity.this, "Delete Success:", Toast.LENGTH_SHORT).show();
                           final ArrayList<String> res=  mDatabaseHelper.getallData();
                           tv.setText("Data is "+ res);
                       }

                       else
                       {
                           Toast.makeText(MainActivity.this, "Delete Fail:", Toast.LENGTH_SHORT).show();
                       }
                      //Toast.makeText(MainActivity.this, "Delete" + a, Toast.LENGTH_SHORT).show();
                       modelListViewArrayList.remove(i);
                       retroAdapter.notifyDataSetChanged();
                    return true;
                }
            });

        }



        catch (JSONException e) {
            e.printStackTrace();
        }

       // Integer a=mDatabaseHelper.getUsersCount();
       // Toast.makeText(this, "Records are:"+a, Toast.LENGTH_SHORT).show();



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(MainActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
               // getJSONResponse();
                writeListView(response);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseHelper.deleteAll();
                final ArrayList<String> res=  mDatabaseHelper.getallData();
                tv.setText("Data is "+ res);
            }
        });


    }



}

