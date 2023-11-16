package com.example.nettas_bakery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;
    private ArrayList<Category> categoryList;
    private CategoryAdapter categoryAdapter;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_home);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) FloatingActionButton addAdmin = findViewById(R.id.btnviewCart);

        addAdmin.setOnClickListener(view -> {

            startActivity(new Intent(Home.this, CartActivity.class));

        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    startActivity(new Intent(Home.this, Home.class));
                    return true;

                case R.id.action_Orders:
                    startActivity(new Intent(Home.this, OrdersActivity.class));
                    return true;

                // Handle other items similarly
                default:
                    return false;
            }
        });


        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categoryList);
        categoryRecyclerView.setAdapter(categoryAdapter);



        databaseReference = FirebaseDatabase.getInstance().getReference("Category");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category category = snapshot.getValue(Category.class);
                    String categoryKey = snapshot.getKey(); // Get the category key
                    category.setKey(categoryKey); // Set the key in your Category class
                    categoryList.add(category);
                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Home.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
            }
        });

        categoryAdapter.setOnItemClickListener((position, categoryID) -> {
            Category selectedCategory = categoryList.get(position);
            String categoryKey = selectedCategory.getKey();

            // Start FoodActivity and pass category key
            Intent intent = new Intent(Home.this, FoodActivity.class);
            intent.putExtra("categoryKey", categoryKey);
            startActivity(intent);
        });
    }
}