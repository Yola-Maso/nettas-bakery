package com.example.nettas_bakery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodActivity extends AppCompatActivity {

    private RecyclerView foodRecyclerView;
    private ArrayList<Food> foodList;
    private FoodAdapter foodAdapter;
    private DatabaseReference databaseReference;

   // public static void start(Context context, String categoryID) {
     //   Intent intent = new Intent(context, FoodActivity.class);
       // intent.putExtra("categoryID", categoryID);
        //context.startActivity(intent);
   // }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        String categoryKey = getIntent().getStringExtra("categoryKey");

        foodRecyclerView = findViewById(R.id.foodRecyclerView);
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodList = new ArrayList<>();
        foodAdapter = new FoodAdapter(this, foodList);
        foodRecyclerView.setAdapter(foodAdapter);

        //foodAdapter = new FoodAdapter(this, foodList, new FoodAdapter.OnItemClickListener() {
           // @Override
       //     public void onItemClick(int position, String foodKey) {
                // Handle item click here, e.g., start FoodDetail activity with foodKey
        //        String foodKey = foodList.get(position).getKey(); // Get the food key
         //       Intent intent = new Intent(FoodActivity.this, FoodDetail.class);
         //       intent.putExtra("foodKey", foodKey); // Pass the food key
         //       startActivity(intent);
       //     }
      //  });


        foodRecyclerView.setAdapter(foodAdapter);


        if (categoryKey != null && !categoryKey.isEmpty()) {
            // Use categoryKey to filter foods that match the MenuID
            databaseReference = FirebaseDatabase.getInstance().getReference("Foods");

            Query query = databaseReference.orderByChild("menuID").equalTo(categoryKey);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    foodList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Food food = snapshot.getValue(Food.class);
                        String foodKey = snapshot.getKey(); // Get the category key
                        food.setKey(foodKey);
                        foodList.add(food);
                    }
                    foodAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(FoodActivity.this, "Failed to load foods", Toast.LENGTH_SHORT).show();
                }
            });

            foodAdapter.setOnItemClickListener((position, foodkey) -> {
                Food selectedFFood = foodList.get(position);
                String foodKey = selectedFFood.getKey();

                // Start FoodActivity and pass category key
                Intent intent = new Intent(FoodActivity.this, FoodDetail.class);
                intent.putExtra("foodKey", foodKey);
                startActivity(intent);
            });

        } else {
            // Handle the case when categoryKey is null (e.g., display an error message)
            Toast.makeText(FoodActivity.this, "Failed to load foods", Toast.LENGTH_LONG).show();
        }
    }
}
