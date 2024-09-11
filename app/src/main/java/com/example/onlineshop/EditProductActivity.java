package com.example.onlineshop;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.onlineshop.Models.Product;

import java.util.HashMap;
import java.util.Map;

public class EditProductActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription, editTextPrice, editTextImageUrl;
    private ImageView imageViewProduct;
    private Button btnSave;

    private Product product;
    private static final String API_URL = "http://192.168.0.155:8080/products/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        editTextName = findViewById(R.id.editText_Name);
        editTextDescription = findViewById(R.id.editText_Description);
        editTextPrice = findViewById(R.id.editText_Price);
        editTextImageUrl = findViewById(R.id.editText_ImageUrl);
        imageViewProduct = findViewById(R.id.image_ViewProduct);
        btnSave = findViewById(R.id.btn_Save);

        // Get the product passed from ManageProductsActivity
        product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            loadProductDetails(product);
        }

        // Set save button click listener
        btnSave.setOnClickListener(v -> {
            updateProduct();
        });
    }

    private void loadProductDetails(Product product) {
        // Load product data into the fields
        editTextName.setText(product.getName());
        editTextDescription.setText(product.getDescription());
        editTextPrice.setText(String.valueOf(product.getPrice()));
        editTextImageUrl.setText(product.getObjectImage());

        // Load product image
        Glide.with(this)
                .load(product.getObjectImage())
                .into(imageViewProduct);
    }

    private void updateProduct() {
        String updatedName = editTextName.getText().toString();
        String updatedDescription = editTextDescription.getText().toString();
        String updatedPrice = editTextPrice.getText().toString();
        String updatedImageUrl = editTextImageUrl.getText().toString();

        // Build the URL for the update request
        String updateUrl = API_URL + product.getId();

        // Create a StringRequest for updating the product
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT,
                updateUrl,
                response -> {
                    // Success - Handle successful response
                    Toast.makeText(EditProductActivity.this, "Product updated successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after updating
                },
                error -> {
                    // Error - Handle the error
                    Log.e("EditProduct", "Error updating product: " + error.getMessage());
                    Toast.makeText(EditProductActivity.this, "Error updating product", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                // Set the updated parameters to be sent in the request
                Map<String, String> params = new HashMap<>();
                params.put("name", updatedName);
                params.put("description", updatedDescription);
                params.put("price", updatedPrice);
                params.put("objectImage", updatedImageUrl);
                return params;
            }
        };

        // Add the request to the Volley request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
