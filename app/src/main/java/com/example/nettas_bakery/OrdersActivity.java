package com.example.nettas_bakery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Order> orderList;
    private OrderAdapter orderAdapter;
    private DatabaseReference ordersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    startActivity(new Intent(OrdersActivity.this, Home.class));
                    return true;

                case R.id.action_Orders:
                    startActivity(new Intent(OrdersActivity.this, OrdersActivity.class));
                    return true;
                // Handle other items similarly
                default:
                    return false;
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(orderAdapter);

        // Assuming "Requests" is the node where orders are stored
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Requests");

        fetchOrders();
    }

    private void fetchOrders() {
        // Get the current user's phone number
        UserData userData = UserData.getInstance();
        String userPhone = userData.getPhoneNumber();

        ordersRef.orderByChild("userPhone").equalTo(userPhone)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            orderList.clear();
                            for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                                Order order = orderSnapshot.getValue(Order.class);
                                //String orderKey = orderSnapshot.getKey();
                                //order.setOrderkey(orderKey);
                                orderList.add(order);
                            }
                            orderAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors
                    }
                });
    }
}