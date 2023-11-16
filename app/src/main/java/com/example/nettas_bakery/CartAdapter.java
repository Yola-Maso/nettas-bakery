package com.example.nettas_bakery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private TextView totalPriceTextView;  // Reference to the total price TextView

    //private Button btnRemove;

    private TextView totalTextView;

    private DatabaseReference userCartRef;

    private CartAdapterCallback callback;
    // Constructor with a reference to the total price TextView
    public CartAdapter(List<CartItem> cartItems, TextView totalTextView, DatabaseReference userCartRef, CartAdapterCallback callback) {
        this.cartItems = cartItems;
        this.totalTextView = totalTextView;
        this.userCartRef = userCartRef;
        this.callback = callback;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view, userCartRef); // Pass userCartRef to the constructor
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.bind(cartItem);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView textItemName;
        private TextView textItemPrice;
        private TextView textQuantity;
        private Button btnDecrease;
        private Button btnIncrease;

        private Button btnRemove;
        private DatabaseReference userCartRef;

        public CartViewHolder(@NonNull View itemView, DatabaseReference userCartRef) {
            super(itemView);
            textItemName = itemView.findViewById(R.id.textItemName);
            textItemPrice = itemView.findViewById(R.id.textItemPrice);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);

            this.userCartRef = userCartRef;

            btnRemove = itemView.findViewById(R.id.btnRemove);

            // Set click listener for the remove button
            btnRemove.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    CartItem removedItem = cartItems.get(position);

                    // Remove the item from the adapter
                    cartItems.remove(position);
                    notifyItemRemoved(position);

                    // Remove the item from the Firebase Realtime Database
                    userCartRef.child(removedItem.getCkey()).removeValue();

                    // TODO: Update the total and other calculations if needed
                }
            });
        }



        public void bind(CartItem cartItem) {
            textItemName.setText(cartItem.getcName());
            textItemPrice.setText(String.format("R%.2f", Float.parseFloat(cartItem.getcPrice())));

            // Set the initial quantity
            textQuantity.setText(String.valueOf(cartItem.getcQuantity()));

            // Decrease quantity
            btnDecrease.setOnClickListener(v -> {
                int quantity = Integer.parseInt(textQuantity.getText().toString());
                if (quantity > 1) {
                    quantity--;
                    textQuantity.setText(String.valueOf(quantity));
                    cartItem.setcQuantity(quantity);

                    // Update the quantity in Firebase Realtime Database
                    userCartRef.child(cartItem.getCkey()).child("cQuantity").setValue(quantity);

                    updateTotalPrice();  // Update the total price
                }
            });

            // Increase quantity
            btnIncrease.setOnClickListener(v -> {
                int quantity = Integer.parseInt(textQuantity.getText().toString());
                quantity++;
                textQuantity.setText(String.valueOf(quantity));
                cartItem.setcQuantity(quantity);

                // Update the quantity in Firebase Realtime Database
                userCartRef.child(cartItem.getCkey()).child("cQuantity").setValue(quantity);

                updateTotalPrice();  // Update the total price
            });
        }

        // Method to update the total price
        private void updateTotalPrice() {
            // Iterate through cartItems and calculate the total
            float total = 0;
            for (CartItem item : cartItems) {
                total += Float.parseFloat(item.getcPrice()) * item.getcQuantity();
            }

            // Notify the callback with the updated total
            if (callback != null) {
                callback.onTotalUpdated(total);
            }
        }

        // Interface for the callback

    }
    public interface CartAdapterCallback {
        void onTotalUpdated(float total);
    }
    }
