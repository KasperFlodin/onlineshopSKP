package com.example.onlineshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.onlineshop.Models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ManageProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewManageProducts;
    private ManageProductAdapter adapter;
    private List<Product> productList;
    private RequestQueue requestQueue;
    private static final String API_URL = "http://192.168.0.155:8080/products";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_products);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerViewManageProducts = findViewById(R.id.recyclerViewManage);
        recyclerViewManageProducts.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        adapter = new ManageProductAdapter(productList, this, new ManageProductAdapter.OnProductActionListener() {
            @Override
            public void onEdit(Product product) {
                // Start EditProductActivity for editing the selected product
                Intent intent = new Intent(ManageProductsActivity.this, EditProductActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }

            @Override
            public void onDelete(Product product) {
                // Delete the product from API
                deleteProduct(product);
            }
        });

        recyclerViewManageProducts.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);

        getProducts();
    }

    private void getProducts() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                API_URL,
                null,
                response -> {
                    Gson gson = new Gson();
                    Type productListType = new TypeToken<List<Product>>() {}.getType();
                    productList.clear();
                    productList.addAll(gson.fromJson(response.toString(), productListType));
                    adapter.notifyDataSetChanged();
                },
                error -> Log.e("ManageProducts", "Error fetching products: " + error.getMessage())
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void deleteProduct(Product product) {
        String deleteUrl = API_URL + "/" + product.getId();  // Adjust this URL as per your API

        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                deleteUrl,
                response -> {
                    productList.remove(product);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(ManageProductsActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();
                },
                error -> Log.e("ManageProducts", "Error deleting product: " + error.getMessage())
        );

        requestQueue.add(stringRequest);
    }
}