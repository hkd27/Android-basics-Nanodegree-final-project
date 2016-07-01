package com.hemantdave.inventoryappfinalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class InventoryDetail extends AppCompatActivity {
    int id = -1;
    TextView title, quantity, price, sale;
    ImageView productImage;
    DbHelper sqlHelper;
    AppInventorPOJO data;
    File imgFile;
    ArrayList<AppInventorPOJO> dataFromPOJO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_detail);
        sqlHelper = new DbHelper(this);
        //checking for the extras with intent
        Intent i = getIntent();
        if (i.hasExtra("id")) {
            id = i.getExtras().getInt("id");
            title = (TextView) findViewById(R.id.detailsTitleTV);
            quantity = (TextView) findViewById(R.id.detailsQuantityTV);
            price = (TextView) findViewById(R.id.detailsPriceTV);
            sale = (TextView) findViewById(R.id.detailsSalesTV);
            productImage = (ImageView) findViewById(R.id.detailsIV);
            //initilizing the values
            reSyncView();
           // checking wether image was taken or not during item addition and deciding wether to show default image or not
            if (checkImageExists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                productImage.setImageBitmap(myBitmap);
            } else {
                productImage.setImageResource(R.drawable.na);
            }

        } else {
            Toast.makeText(InventoryDetail.this, "Item not present", Toast.LENGTH_SHORT).show();
            finish();
        }


    }
    //resyncing the view after Quantity is added or subtracted
    public void reSyncView() {
        dataFromPOJO = sqlHelper.getrowInventory(id);
        data = dataFromPOJO.get(0);

        title.setText("Name: " + data.getName());
        quantity.setText("Quantity: " + data.getQuantity());
        price.setText("Price: " + data.getPrice());
        sale.setText("Sales: " + data.getSales());
        imgFile = new File(data.getImagePath());
    }
    //adding Quantity
    public void quantityPlus(View v) {
        sqlHelper.addQuantity(id);
        reSyncView();
    }
    //reduncing Quantity
    public void quantityMinus(View v) {
        sqlHelper.subtarctQuantity(id);
        reSyncView();
    }
    //Sending user oder to email client of his choice
    public void orderMore(View v) {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        /* Fill it with Data */
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{data.getEmail()});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Order MOre item");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "We want some more of this item" + data.getName());

        /* Send it off to the Activity-Chooser */
        this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
    //Prompting user wether the user want to delete current item
    public void deleteThisItem(View v) {
        new AlertDialog.Builder(this)
                .setTitle("Add Products?")
                .setMessage("Enter Specific Details?")
                .setPositiveButton("Delete Current Item", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqlHelper.deleteInventory(id);
                        Toast.makeText(InventoryDetail.this, "Item deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(InventoryDetail.this, "Item deletion cancelled", Toast.LENGTH_SHORT).show();
                    }
                }).create().show();
    }
    //checking image path
    public boolean checkImageExists() {
        if (imgFile.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
