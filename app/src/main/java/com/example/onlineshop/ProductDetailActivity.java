package com.example.onlineshop;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.onlineshop.Models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    // Static list to act as a shopping cart
    public static List<Product> cart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ImageView imageView = findViewById(R.id.imageViewProductDetail);
        TextView nameTextView = findViewById(R.id.textViewNameDetail);
        TextView descriptionTextView = findViewById(R.id.textViewDescriptionDetail);
        TextView priceTextView = findViewById(R.id.textViewPriceDetail);
        FloatingActionButton buttonAddToCart = findViewById(R.id.buttonAddToCart);

        Product product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            // Indstil værdier for views
            nameTextView.setText(product.getName());
            descriptionTextView.setText(product.getDescription());
            priceTextView.setText(String.valueOf(product.getPrice()) + " kr");

            // Brug Glide til at indlæse billedet
            Glide.with(this)
                    .load(product.getObjectImage())
                    .into(imageView);
        }

        // Set click listener for Add to Cart button
        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product != null) {
                    // Add the product to the cart
                    cart.add(product);
                    Toast.makeText(ProductDetailActivity.this, product.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
