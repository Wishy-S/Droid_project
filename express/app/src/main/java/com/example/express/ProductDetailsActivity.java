package com.example.express;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.express.model.Items;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import com.example.express.R;

public class ProductDetailsActivity extends AppCompatActivity {
    private Button  Delbtn,AddBtn,ProdStat;
    Vibrator vibrator;
    private ImageView imageView;

    private static Uri alarmSound;
    private final long []pattern = {100,300,300,300};
    //private FloatingActionButton add;
    private ElegantNumberButton elegantNumberButton;
    private TextView productPrice,productDescriptioin,productName,productQuantity;
    public String productID ="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        vibrator= (Vibrator)getSystemService(VIBRATOR_SERVICE);
        imageView =(ImageView)findViewById(R.id.product_image_details);

                Delbtn=(Button)findViewById(R.id.del_btn);
                ProdStat=(Button)findViewById(R.id.stats);
        //add=(FloatingActionButton)findViewById(R.id.addbtn);
                productPrice=(TextView )findViewById(R.id.productdet);
        productDescriptioin=(TextView )findViewById(R.id.proddes);
                productName=(TextView )findViewById(R.id.product_price);
                productQuantity =(TextView)findViewById(R.id.product_quantity);
                elegantNumberButton =(ElegantNumberButton)findViewById(R.id.elegant);
                AddBtn = (Button)findViewById(R.id.readd);

                alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);




                productID= getIntent().getStringExtra("pid");

                getProductDetails(productID);



                Delbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        deleproduct();

                    }
                });
                AddBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addproduct();
                    }
                });
                ProdStat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(ProductDetailsActivity.this,Item_Analytics.class);
                        startActivity(intent);
                    }
                });


    }

    private void addproduct()
    {


        int p = Integer.parseInt(elegantNumberButton.getNumber());
        int q = Integer.parseInt(productQuantity.getText().toString());
        int  m = p+q;
        String upadatedquantity = String.valueOf(m);
        DatabaseReference productsref = FirebaseDatabase.getInstance().getReference().child("Items");
        HashMap<String,Object>productMap = new HashMap<>();


        productMap.put("quantity",upadatedquantity);
        productsref
                .child(productID).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Intent intent = new Intent(ProductDetailsActivity.this,ShopKeeperDashBoardActivity.class);

                            Toast.makeText(ProductDetailsActivity.this, "Item has been Updated succesfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {


                            Toast.makeText(ProductDetailsActivity.this, "Oops!!! Some Network Erroe Might have Occured", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void deleproduct()
    { int n = Integer.parseInt(elegantNumberButton.getNumber());
    int k = Integer.parseInt(productQuantity.getText().toString());
    if(n == k) {


        FirebaseDatabase.getInstance().getReference().child("Items").child(productID).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        vibrator.vibrate(500);
                        Toast.makeText(ProductDetailsActivity.this, "The Item has been deleted Successfully", Toast.LENGTH_SHORT).show();

                      Intent intent = new Intent(ProductDetailsActivity.this,ShopKeeperDashBoardActivity.class);
                        startActivity(intent);

                    }
                });
    }
    else if(n<k)
    {
        int  m = k-n;
        String upadatedquantity = String.valueOf(m);
        DatabaseReference productsref = FirebaseDatabase.getInstance().getReference().child("Items");
        HashMap<String,Object>productMap = new HashMap<>();


        productMap.put("quantity",upadatedquantity);
        productsref
                .child(productID).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Intent intent = new Intent(ProductDetailsActivity.this,ShopKeeperDashBoardActivity.class);

                            Toast.makeText(ProductDetailsActivity.this, "Item has been Updated succesfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {


                            Toast.makeText(ProductDetailsActivity.this, "You cant Delete The Item", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
    }


    private void getProductDetails(String productID) {
        DatabaseReference productsref = FirebaseDatabase.getInstance().getReference().child("Items");
        productsref.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Items items = dataSnapshot.getValue(Items.class);
                    productName.setText(items.getPname());
                    productPrice.setText(items.getPrice());
                    productQuantity.setText(items.getQuantity());
                    productDescriptioin.setText(items.getDescription());
                    Picasso.get().load(items.getImage()).into(imageView);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {


            }
        });
    }

}
