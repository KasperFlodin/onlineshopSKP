package com.example.onlineshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineshop.Models.Product;

import java.util.List;

public class ManageProductAdapter extends RecyclerView.Adapter<ManageProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;
    private OnProductActionListener onProductActionListener;

    // Interface for handling edit and delete actions
    public interface OnProductActionListener {
        void onEdit(Product product);
        void onDelete(Product product);
    }

    public ManageProductAdapter(List<Product> productList, Context context, OnProductActionListener onProductActionListener) {
        this.productList = productList;
        this.context = context;
        this.onProductActionListener = onProductActionListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, descriptionTextView, priceTextView;
        ImageView productImageView;
        Button btnEdit, btnDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            descriptionTextView = itemView.findViewById(R.id.textViewDescription);
            priceTextView = itemView.findViewById(R.id.textViewPrice);
            productImageView = itemView.findViewById(R.id.image_ViewProduct);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(final Product product) {
            nameTextView.setText(product.getName());
            descriptionTextView.setText(product.getDescription());
            priceTextView.setText(product.getPrice() + " kr");

            // Load product image using Glide
            Glide.with(productImageView.getContext())
                    .load(product.getObjectImage())
                    .override(100, 100)
                    .fitCenter()
                    .into(productImageView);

            // Edit button click listener
            btnEdit.setOnClickListener(v -> {
                if (onProductActionListener != null) {
                    onProductActionListener.onEdit(product);
                }
            });

            // Delete button click listener
            btnDelete.setOnClickListener(v -> {
                if (onProductActionListener != null) {
                    onProductActionListener.onDelete(product);
                }
            });
        }
    }
}
