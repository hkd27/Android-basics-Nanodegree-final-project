package com.hemantdave.inventoryappfinalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by INDIA on 6/30/2016.
 */
public class InventoryListViewCustomAdapter extends BaseAdapter {
    ArrayList<AppInventorPOJO> InvnetoryPOJO;
    Context context;
    File imgFile;


    public InventoryListViewCustomAdapter(Context context, ArrayList<AppInventorPOJO> InvnetoryPOJO) {
        this.context = context;
        this.InvnetoryPOJO = InvnetoryPOJO;
    }

    public InventoryListViewCustomAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getCount() {
        return InvnetoryPOJO.size();
    }

    @Override
    public Object getItem(int position) {
        return InvnetoryPOJO.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.custom_list_view_row, parent, false);
        }
        TextView Name = (TextView) row.findViewById(R.id.inventoryTitleTV);
        TextView quantity = (TextView) row.findViewById(R.id.inventoryQuaTV);
        TextView price = (TextView) row.findViewById(R.id.inventoryPriceTV);
        TextView sales = (TextView) row.findViewById(R.id.inventorySalesTV);
        TextView email = (TextView) row.findViewById(R.id.inventoryEmailTV);
        ImageView image = (ImageView) row.findViewById(R.id.inventoryIV);
        Button saleOne = (Button) row.findViewById(R.id.inventorysaleButton);

        final AppInventorPOJO temp_obj = InvnetoryPOJO.get(position);
        imgFile = new File(temp_obj.getImagePath());
        if (checkImageExists()) {


            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            image.setImageBitmap(myBitmap);
        } else {
            image.setImageResource(R.drawable.na);
        }
        Name.setText("Item Name: " + temp_obj.getName());
        quantity.setText("Quantity: " + temp_obj.getQuantity());
        price.setText("Price: " + temp_obj.getPrice());
        sales.setText("Sales: " + temp_obj.getSales());
        email.setText("Email: " + temp_obj.getEmail());
        row.setTag(temp_obj.getId());
        saleOne.setTag(temp_obj.getId());
        //setting id as tag value  for use determinig the item id which user clicked
        row.setTag(temp_obj.getId());
        return row;
    }

    //checking image was taken or not previously
    public boolean checkImageExists() {
        if (imgFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

}
