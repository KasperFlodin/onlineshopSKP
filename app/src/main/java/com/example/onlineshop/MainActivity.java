package com.example.onlineshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.onlineshop.Models.Connection;
import com.example.onlineshop.Models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private FloatingActionButton btnCart, btnManage;
    private RequestQueue requestQueue;
    private TextView tvLoggedIn;
    private static final String API_URL = "http://" + Connection.getBaseUrl() + ":8080/products";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        adapter = new ProductAdapter(productList, this);
        recyclerView.setAdapter(adapter);

        btnCart = findViewById(R.id.fab_cart);
        btnManage = findViewById(R.id.fab_admin);
        tvLoggedIn = findViewById(R.id.tvLoggedIn);

        requestQueue = Volley.newRequestQueue(this);

        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ShoppingCartActivity.class);
            startActivity(intent);
        });

        btnManage.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

            if (isLoggedIn) {
                // User is logged in, allow access to ManageProductsActivity
                Intent intent = new Intent(MainActivity.this, ManageProductsActivity.class);
                startActivity(intent);
            } else {
                // User is not logged in, redirect to LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Check login status and show/hide the "Logged in" text
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            tvLoggedIn.setVisibility(View.VISIBLE); // Show "Logged in" text
        } else {
            tvLoggedIn.setVisibility(View.GONE); // Hide "Logged in" text
        }

        getProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProducts();
    }

    // Clear login status when app goes to background
    @Override
    protected void onStop() {
        super.onStop();
        // Clear the login status when the app is no longer visible
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
    }

    private void getProducts() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                API_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Use Gson to convert the JSONArray to a list of Product objects
                            Gson gson = new Gson();
                            Type productListType = new TypeToken<List<Product>>(){}.getType();

                            // Convert JSON Array to List of Products
                            List<Product> products = gson.fromJson(response.toString(), productListType);

                            // Clear the existing productList and add new data
                            productList.clear();
                            productList.addAll(products);

                            // Notify the adapter that the data has changed
                            adapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            Log.e("MainActivity", "Gson parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MainActivity", "Volley error: " + error.getMessage());
                    }
                }
        );

        // Add the request to the request queue (assuming you have a RequestQueue instance)
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }



}