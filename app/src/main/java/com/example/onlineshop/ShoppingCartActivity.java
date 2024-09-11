package com.example.onlineshop;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.Models.Product;

import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private ProductAdapter adapter;
    private List<Product> cart;
    private TextView tvTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_cart);


        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        tvTotalPrice = findViewById(R.id.tv_totalprice);

        // Get cart data from the intent
        cart = ProductDetailActivity.cart;

        if (cart != null && !cart.isEmpty()) {
            adapter = new ProductAdapter(cart, this);
            recyclerViewCart.setAdapter(adapter);

            double totalPrice = calculateTotalPrice(cart);
            tvTotalPrice.setText("Total: " + String.format("%.2f", totalPrice) + " Kr.");
        } else {
            Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            tvTotalPrice.setText("Total: 0.00 Kr.");
        }
    }

    private double calculateTotalPrice(List<Product> cart) {
        double total = 0;
        for (Product product : cart) {
            total += product.getPrice(); // Assuming each Product has a getPrice() method
        }
        return total;
    }
}