package com.example.nettas_bakery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FoodDetail extends AppCompatActivity {


    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        // Initialize views
        FloatingActionButton AddToCart = findViewById(R.id.btn_addToCart);
        ImageView foodImageView = findViewById(R.id.img_food);
        TextView foodNameTextView = findViewById(R.id.food_name);
        TextView foodPriceTextView = findViewById(R.id.food_price);
        TextView foodPortionTextView = findViewById(R.id.portion_size);
        TextView foodIngredientsTextView = findViewById(R.id.food_Ingredients);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        AddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the food key from the intent
                Intent intent = getIntent();
                if (intent != null) {
                    String foodKey = intent.getStringExtra("foodKey");

                    // Add the food item to the cart using the foodKey
                    addToCart(foodKey);
                }
            }
        });

        // Get the food key from the intent
        Intent intent = getIntent();
        if (intent != null) {
            String foodKey = intent.getStringExtra("foodKey");

            // Use the food key to fetch the specific food details from Firebase
            // You'll need to implement the Firebase retrieval logic here
            // For example:
            DatabaseReference foodRef = FirebaseDatabase.getInstance().getReference("Foods").child(foodKey);
            foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Retrieve the food details
                        Food food = dataSnapshot.getValue(Food.class);

                        // Load the food image using Glide
                        Glide.with(FoodDetail.this).load(food.getImage()).into(foodImageView);

                        collapsingToolbarLayout.setTitle(food.getName());

                        // Set food details to TextViews
                        foodNameTextView.setText(food.getName());
                        foodPriceTextView.setText(food.getPrice());
                        foodPortionTextView.setText(food.getPortion());
                        foodIngredientsTextView.setText(food.getIngredients());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }



    }

    private void addToCart(String foodKey) {

        // Get the user's identifier from the user object
        UserData userData = UserData.getInstance();
        String userIdentifier = userData.getPhoneNumber();
        // Assuming "user" is your User object

        // Ensure the user identifier is not null or empty
        if (TextUtils.isEmpty(userIdentifier)) {
            Toast.makeText(this, "User identifier is missing. Cannot add to cart.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get a reference to the user's cart in the Firebase Realtime Database
        // Reference to the user's cart
        DatabaseReference userCartRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(userIdentifier)
                .child("cart")
                .child(foodKey); // Reference to the specific food item in the cart

        // Fetch the food details using the foodKey
        DatabaseReference foodRef = FirebaseDatabase.getInstance().getReference()
                .child("Foods")
                .child(foodKey);

        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the food details
                    Food food = dataSnapshot.getValue(Food.class);

                    // Create a CartItem object with additional details
                    CartItem cartItem = new CartItem();
                    cartItem.setcName(food.getName());
                    cartItem.setcPrice(food.getPrice());
                    cartItem.setcDiscount(food.getDiscount());
                    cartItem.setCKey(foodKey);
                    // You can add more details as needed

                    // Set the cart item in the user's cart
                    userCartRef.setValue(cartItem);

                    // Show a confirmation message to the user
                    Toast.makeText(FoodDetail.this, "Item added to cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }}

