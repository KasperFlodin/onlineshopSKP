package com.example.onlineshop;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.onlineshop.Models.Connection;
import com.example.onlineshop.Models.Product;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class EditProductActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription, editTextPrice, editTextImageUrl;
    private ImageView imageViewProduct;
    private Button btnSave;
    private Product product;
    private static final String API_URL = "http://" + Connection.getBaseUrl() + ":8080/products";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        // Initialize UI components
        editTextName = findViewById(R.id.editText_Name);
        editTextDescription = findViewById(R.id.editText_Description);
        editTextPrice = findViewById(R.id.editText_Price);
        editTextImageUrl = findViewById(R.id.editText_ImageUrl);
        imageViewProduct = findViewById(R.id.image_ViewProduct);
        btnSave = findViewById(R.id.btn_Save);

        // Retrieve product data from the Intent
        String productJson = getIntent().getStringExtra("product");
        if (productJson != null) {
            // Deserialize the JSON string back into a Product object
            product = new Gson().fromJson(productJson, Product.class);

            // Populate the fields with product data
            if (product != null) {
                editTextName.setText(product.getName());
                editTextDescription.setText(product.getDescription());
                editTextPrice.setText(String.valueOf(product.getPrice()));
                editTextImageUrl.setText(product.getObjectImage());

                // Load product image using Glide
                Glide.with(this)
                        .load(product.getObjectImage())
                        .into(imageViewProduct);
            }
        }

        // Set an OnClickListener on the Save button
        btnSave.setOnClickListener(view -> {
            if (product != null) {
                // Update the product when the save button is clicked
                updateProduct();
            } else {
                Toast.makeText(EditProductActivity.this, "No product data available!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProduct() {
        // Gather updated data from the UI fields
        String updatedName = editTextName.getText().toString().trim();
        String updatedDescription = editTextDescription.getText().toString().trim();
        String updatedImageUrl = editTextImageUrl.getText().toString().trim();
        int updatedPrice;

        try {
            updatedPrice = Integer.parseInt(editTextPrice.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the product object with new data
        product.setName(updatedName);
        product.setDescription(updatedDescription);
        product.setPrice(updatedPrice);
        product.setObjectImage(updatedImageUrl);

        // Convert the updated product object to JSON string using Gson
        String productJson = new Gson().toJson(product);

        // URL for updating the product
        String url = API_URL + "/" + product.getId();

        // Create a StringRequest for the PUT request
        StringRequest request = new StringRequest(
                Request.Method.PUT,
                url,
                response -> {
                    // Handle success response
                    Toast.makeText(EditProductActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity
                },
                error -> {
                    // Handle error response
                    Toast.makeText(EditProductActivity.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public byte[] getBody() {
                return productJson.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        // Add the request to the Volley request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }




//    private void updateProduct() {
//        // Get the updated data from the EditText fields
//        int updatedId = product.getId();
//        String updatedName = editTextName.getText().toString();
//        String updatedDescription = editTextDescription.getText().toString();
//        String updatedPrice = editTextPrice.getText().toString();
//        String updatedImageUrl = editTextImageUrl.getText().toString();
//
//        // Create a JSONObject to hold the updated product data
//        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("id", updatedId);
//            jsonObject.put("name", updatedName);
//            jsonObject.put("description", updatedDescription);
//            jsonObject.put("price", Integer.parseInt(updatedPrice));
//            jsonObject.put("objectImage", updatedImageUrl);
//
//            String updateURL = API_URL + "/";
//
//            // Create a JsonObjectRequest for sending the JSON to the API
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                    Request.Method.PUT, // Use PUT method for updating
//                    updateURL + product.getId(),
//                    jsonObject,
//                    response -> {
//                        // Success handler - product was successfully updated
//                        Toast.makeText(EditProductActivity.this, "Product updated successfully!", Toast.LENGTH_SHORT).show();
//                        finish(); // Close activity after updating
//                    },
//                    error -> {
//                        // Error handler - log error for debugging
//                        Log.e("EditProduct", "Error updating product: " + error.toString());
//                        Toast.makeText(EditProductActivity.this, "Error updating product", Toast.LENGTH_SHORT).show();
//                    }
//            );
//
//            // Add the request to the request queue
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            requestQueue.add(jsonObjectRequest);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(EditProductActivity.this, "Error forming JSON data", Toast.LENGTH_SHORT).show();
//        }
//    }
}
