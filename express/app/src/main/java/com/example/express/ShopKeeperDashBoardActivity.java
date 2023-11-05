package com.example.express;

import android.content.Intent;
import android.os.Bundle;

import com.example.express.Prevalent.Prevalent;

import com.example.express.ViewHolder.ProductViewHolder;
import com.example.express.model.Items;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;



import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.express.R;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class ShopKeeperDashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_keeper_dash_board);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Items");
        ProductsRef.keepSynced(true);


        TextView textView = findViewById(R.id.textview);
        String msg =getIntent().getStringExtra("message");
        textView.setText(msg);


        Paper.init(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");

        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerview = navigationView.getHeaderView(0);
        TextView usernameTextView = headerview.findViewById(R.id.username);
        CircleImageView profileImageview = headerview.findViewById(R.id.profile_image);


        if(Prevalent.currentOnlineUser!=null)
        usernameTextView.setText(Prevalent.currentOnlineUser.getName());

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



       

        
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
       // NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Items> options =
                new FirebaseRecyclerOptions.Builder<Items>()
                .setQuery(ProductsRef, Items.class)
                .build();


        FirebaseRecyclerAdapter<Items, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Items, ProductViewHolder>(options) {


                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull final Items model)
                    {

                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductPrice.setText(model.getPrice()+"$");
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtquantity.setText(model.getQuantity());


                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ShopKeeperDashBoardActivity.this,ProductDetailsActivity.class);

                                intent.putExtra("pid" ,model.getPid());
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
        recyclerView.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shop_keeper_dash_board, menu);
        return true;
    }
   

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem Item) {

        int id = Item.getItemId();
        if (id == R.id.nav_additem)
        {
            Intent intent = new Intent(ShopKeeperDashBoardActivity.this,HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();




        }
        else if (id == R.id.nav_logout)
        {
            Paper.book().destroy();
            Intent intent = new Intent(ShopKeeperDashBoardActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();


        }
         else if (id == R.id.nav_search)
        {
            Intent intent = new Intent(ShopKeeperDashBoardActivity.this,Searchproductsactivity.class);

            startActivity(intent);


        }
        else if (id == R.id.settings)
        {


        }
        else
        {
            Toast.makeText(this, "Please click on Any Option", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
