package com.example.express.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.express.Interface.ItemClickListener;

import Droid_project.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName,txtProductDescription,txtProductPrice,txtquantity;
    public ImageView imageView;
    public ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = (ImageView)itemView.findViewById(R.id.itemimage) ;
        txtProductName = (TextView)itemView.findViewById(R.id.itemname) ;
        txtProductDescription = (TextView)itemView.findViewById(R.id.itemdescription) ;
        txtProductPrice  = (TextView)itemView.findViewById(R.id.itemprice) ;
        txtquantity = (TextView)itemView.findViewById(R.id.itemquantity) ;

    }
    public  void  setItemClickListener(ItemClickListener listener)
    {
        this.listener=listener;
    }


    @Override
    public  void onClick(View view)
    {
        listener.onClick(view,getAdapterPosition(),false);
    }

}
