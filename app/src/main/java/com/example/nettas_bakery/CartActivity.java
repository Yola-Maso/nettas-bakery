package com.example.nettas_bakery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.paypal.android.sdk.payments.PayPalConfiguration;
//import com.paypal.android.sdk.payments.PayPalPayment;
//import com.paypal.android.sdk.payments.PayPalService;
//import com.paypal.android.sdk.payments.PaymentActivity;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.core.exception.APIConnectionException;
import com.stripe.android.core.exception.APIException;
import com.stripe.android.core.exception.AuthenticationException;
import com.stripe.android.core.exception.InvalidRequestException;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;
    private TextView totalTextView;
    private DatabaseReference userCartRef;
    private DatabaseReference requestsRef;

    private CartAdapter.CartAdapterCallback callback;

    //private static final int STRIPE_PAYMENT_REQUEST_CODE = 123;


    // Global variables for PayPal Payment Gateway Integration
    //private static final int PAYPAL_REQUEST_CODE = 123; // You can use any code here
    //private static PayPalConfiguration config = new PayPalConfiguration()
     //       .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // Change to ENVIRONMENT_PRODUCTION for production
    //        .clientId("AZeAjINdDIHD9gVjjVLOZ1W3po67CM56ihPvdl8jIWouyW-1S9ReWJB0Vjbs5WicHHUGu4oN4aMeydax");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Set your publishable key: You should use your real publishable key here.
        //PaymentConfiguration.init(getApplicationContext(), "pk_test_51NyHSyF6BmGMvpvNEU60EfcVKQTVwDGcuISkfzrjlvwM73z8dmaA7u3n6bQGW9RHqvSCHeYMypAgxaJFkG6MCEqp00bzgaTpYV");

        //Stripe stripe = new Stripe(getApplicationContext(), "pk_test_51NyHSyF6BmGMvpvNEU60EfcVKQTVwDGcuISkfzrjlvwM73z8dmaA7u3n6bQGW9RHqvSCHeYMypAgxaJFkG6MCEqp00bzgaTpYVpk_test_51NyHSyF6BmGMvpvNEU60EfcVKQTVwDGcuISkfzrjlvwM73z8dmaA7u3n6bQGW9RHqvSCHeYMypAgxaJFkG6MCEqp00bzgaTpYV");


        recyclerView = findViewById(R.id.recyclerViewCart);
        totalTextView = findViewById(R.id.textTotal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItems = new ArrayList<>();
        //TextView totalPriceTextView = findViewById(R.id.textTotal);

        UserData userData = UserData.getInstance();
        String userIdentifier = userData.getPhoneNumber();

        userCartRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(userIdentifier)
                .child("cart");

        requestsRef = FirebaseDatabase.getInstance().getReference().child("Requests");

        callback = new CartAdapter.CartAdapterCallback() {
            @Override
            public void onTotalUpdated(float total) {
                totalTextView.setText("Total: R" + String.format("%.2f", total));
            }
        };

        cartAdapter = new CartAdapter(cartItems, totalTextView, userCartRef, callback);
        recyclerView.setAdapter(cartAdapter);

        // TODO: Fetch cart items from Firebase
        fetchCartItems();

        // TODO: Implement total calculation logic
        calculateTotal();

        // Example to remove an item
        // removeItem("01");

        // Set onClickListener for the Checkout button
        Button checkoutButton = findViewById(R.id.btnCheckout);
        checkoutButton.setOnClickListener(view -> {
            try {
                handleCheckout();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Fetch cart items from Firebase
    private void fetchCartItems() {
        UserData userData = UserData.getInstance();
        String userIdentifier = userData.getPhoneNumber();

         userCartRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(userIdentifier)
                .child("cart");

        userCartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    cartItems.clear();
                    for (DataSnapshot cartItemSnapshot : dataSnapshot.getChildren()) {
                        CartItem cartItem = cartItemSnapshot.getValue(CartItem.class);
                        cartItems.add(cartItem);
                    }
                    cartAdapter.notifyDataSetChanged();
                    calculateTotal();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    // Remove item from Firebase
    private void removeItem(String foodKey) {
        UserData userData = UserData.getInstance();
        String userIdentifier = userData.getPhoneNumber();
        DatabaseReference userCartRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(userIdentifier)
                .child("cart")
                .child(foodKey);

        userCartRef.removeValue();
    }

    // Calculate total price
    private void calculateTotal() {
        float total = 0;
        for (CartItem cartItem : cartItems) {
            total += Float.parseFloat(cartItem.getcPrice())* cartItem.getcQuantity();
        }
        totalTextView.setText("Total: R" + total);
    }


    private void handleCheckout() {
        // Fetch cart items from Firebase
        fetchCartItems();

        // Calculate total
        calculateTotal();

        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Your cart is empty. Add items before checking out.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show an AlertDialog to inform the user
        showPaymentAlertDialog();
    }

    private void showPaymentAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Payment Information");
        builder.setMessage("Please note that you are expected to pay the full amount "+ totalTextView.getText().toString() +" when picking up your order. Otherwise, there will be legal action taken.!!!");

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Proceed with placing the order
                placeOrder();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User canceled, do nothing or handle accordingly
            }
        });

        builder.show();
    }

    private void placeOrder() {
        // Get user details
        UserData userData = UserData.getInstance();
        //String userName = userData.getName();
        String userPhone = userData.getPhoneNumber();

        // Get food items from the cart
        List<OrderedFoodItem> orderedFoodItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            orderedFoodItems.add(new OrderedFoodItem(
                    cartItem.getCkey(),
                    cartItem.getcName(),
                    cartItem.getcPrice(),
                    String.valueOf(cartItem.getcQuantity())
            ));
        }

        // Generate a unique order key (you might want to use a better way to generate this)
        String orderkey = String.valueOf(System.currentTimeMillis());

        // Create an instance of Order
        Order order = new Order(orderkey,userPhone, totalTextView.getText().toString().substring(7), "Placed", orderedFoodItems);

        // Save the order to the "Requests" node with the generated orderKey
        requestsRef.child(orderkey).setValue(order);

        // Clear the user's cart
        clearCart();


    }


    // Clear the user's cart in the database
    private void clearCart() {
        userCartRef.removeValue(); // Remove the "cart" node for the current user
        cartItems.clear(); // Clear the local list of cart items
        cartAdapter.notifyDataSetChanged(); // Notify the adapter to update the RecyclerView
    }

}

