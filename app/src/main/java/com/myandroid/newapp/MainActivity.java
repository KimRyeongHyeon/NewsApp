package com.myandroid.newapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] myDataset;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        queue = Volley.newRequestQueue(this);
        getNews();
    }

    public void getNews() {
            final String url ="NEWS API KEY";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                final JSONObject jsonObject = new JSONObject(response);

                                final JSONArray arrayArticles = jsonObject.getJSONArray("articles");

                                List<NewsData> news = new ArrayList<>();

                                for(int i = 0, j = arrayArticles.length(); i < j; i++ ) {
                                    final JSONObject obj = arrayArticles.getJSONObject(i);

                                    Log.d("NEWS", obj.toString());

                                    final NewsData newsData = new NewsData();
                                    newsData.setTitle(obj.getString("title"));
                                    newsData.setUrlToImage(obj.getString("urlToImage"));
                                    newsData.setNews_link(obj.getString("url"));
                                    news.add(newsData);

                                    mAdapter = new MyAdapter(news, MainActivity.this, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Object obj = v.getTag();
                                            if(obj != null) {
                                                int position = (int)obj;
                                                String link = ((MyAdapter)mAdapter).getNews(position).getNews_link();
                                                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                                                intent.putExtra("link", link);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                }

                                recyclerView.setAdapter(mAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

        queue.add(stringRequest);
    }
}
