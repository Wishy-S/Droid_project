package com.example.express;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.rey.material.widget.EditText;

import com.example.express.model.Items;
import com.example.express.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import com.example.express.R;

public class Searchproductsactivity extends AppCompatActivity {
    private Button SearchBtn;
    private EditText InputText;
    private RecyclerView  searchList;
    private String SearchInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchproductsactivity);

        InputText =findViewById(R.id.search_product_name);
        SearchBtn = findViewById(R.id.search_btn);
        searchList = findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(Searchproductsactivity.this));

        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SearchInput =InputText.getText().toString();
                onStart();

            }
        });


    }
    protected void onStart() {
        super.onStart();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Items");
        FirebaseRecyclerOptions<Items> options  =
                new FirebaseRecyclerOptions.Builder<Items>()
                .setQuery(reference.orderByChild("pname").startAt(SearchInput), Items.class).build();

        FirebaseRecyclerAdapter<Items, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Items, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull Items model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductPrice.setText(model.getPrice()+"$");
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtquantity.setText(model.getQuantity());


                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Searchproductsactivity.this,ProductDetailsActivity.class);
                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_items_layout,parent,false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return  holder;
                    }
                };

        adapter.startListening();
        searchList.setAdapter(adapter);


    }

}
