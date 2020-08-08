package com.example.express;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslCertificate;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity  {

    private Button AddnewProduct,back;
    private EditText InputProductName,InputProductPrice,InputProductDescription,InputQuantity,InputDealerName,InputDealerphone;
    private ImageView InputProductImage;
    private String Description,Pname,saveCurrentDate,saveCurrentTime;

    private String Price;
    private String quantity;
    private String dname,dphone;

    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey,downloadImageuri;
    private StorageReference ProductImagesRef;
    private DatabaseReference productReference;
    private ProgressDialog loadingBar ;


    @Override
    protected void onCreate(Bundle savedInstanceState)throws NullPointerException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productReference =FirebaseDatabase.getInstance().getReference().child("Items");


        back = (Button)findViewById(R.id.back);
        AddnewProduct = (Button)findViewById(R.id.new_add_product);
        InputProductImage = (ImageView)findViewById(R.id.select_product_image);
        InputProductName =(EditText)findViewById(R.id.product_name);
        InputProductDescription = (EditText)findViewById(R.id.product_description);
        InputProductPrice = (EditText)findViewById(R.id.product_price);
        InputQuantity = (EditText)findViewById(R.id.product_quantity);
        InputDealerName=(EditText)findViewById(R.id.dealername);
        InputDealerphone =(EditText)findViewById(R.id.dealerphone);
        loadingBar = new ProgressDialog(this);

        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });

        AddnewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

             ValidateProductData();

            }


        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,ShopKeeperDashBoardActivity.class);
                startActivity(intent);
            }
        });


    }



    private  void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);

        }
    }





    private void ValidateProductData()
    {
        Description = InputProductDescription.getText().toString();
        Pname = InputProductName.getText().toString();
        Price = InputProductPrice.getText().toString();
        quantity = InputQuantity.getText().toString();
        dname =    InputDealerName.getText().toString();
        dphone = InputDealerphone.getText().toString();

        if(ImageUri == null)
        {
            Toast.makeText(this, "Product Image is Necessary", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "PLease enter product description", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "PLease enter product Price", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "PLease enter product Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(quantity))
        {
            Toast.makeText(this, "Please enter product quantity", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(dname))
        {
            Toast.makeText(this, "Please Enter Dealer Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(dphone))
        {
            Toast.makeText(this, "Please Enter Dealer Phone", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();
        }










    }

private void StoreProductInformation()
{
    loadingBar.setTitle("Adding new Item");
    loadingBar.setMessage("Please wait,while we are Adding The New Item");
    loadingBar.setCanceledOnTouchOutside(false);
    loadingBar.show();
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");

    saveCurrentDate = currentDate.format((calendar).getTime());
    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss  a");
    saveCurrentTime = currentTime.format(calendar.getTime());

    productRandomKey = Pname + Price+ dname;


    final StorageReference  filePath = ProductImagesRef.child(ImageUri.getLastPathSegment()+productRandomKey+".jpg");
    final UploadTask uploadTask =  filePath.putFile(ImageUri);

    uploadTask.addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e)
        {
            String message = e.toString();
            Toast.makeText(HomeActivity.this, "Error"+ message, Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }
    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
        {
            Toast.makeText(HomeActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();

                    }
                    downloadImageuri = filePath.getDownloadUrl().toString();
                    return  filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if(task.isSuccessful())
                    {
                        downloadImageuri =task.getResult().toString();
                        Toast.makeText(HomeActivity.this, "Getting product Image url succesfully", Toast.LENGTH_SHORT).show();
                        SaveProductInfoToDatabase();
                    }
                }

            });
        }
    });


}

    private void SaveProductInfoToDatabase()
    {

        HashMap<String,Object>productMap = new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",Description);
        productMap.put("image",downloadImageuri);
        productMap.put("price",Price);
        productMap.put("pname",Pname);
        productMap.put("dname",dname);
        productMap.put("dphone",dphone);
        productMap.put("quantity",quantity);
        productReference.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                       if(task.isSuccessful())
                       {
                           Intent intent = new Intent(HomeActivity.this,ShopKeeperDashBoardActivity.class);
                           InputProductDescription.setText("");
                           InputProductName.setText("");
                           InputQuantity.setText("");
                           InputProductPrice.setText("");
                           startActivity(intent);
                           loadingBar.dismiss();
                           Toast.makeText(HomeActivity.this, "Item along with all credentials has been added succesfully", Toast.LENGTH_SHORT).show();
                       }
                       else
                       {
                           loadingBar.dismiss();
                           String message =task.getException().toString();
                           Toast.makeText(HomeActivity.this, "Error:"+message, Toast.LENGTH_SHORT).show();

                       }
                    }
                });
    }

}


