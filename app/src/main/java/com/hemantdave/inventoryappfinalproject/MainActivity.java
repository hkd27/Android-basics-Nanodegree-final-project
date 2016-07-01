package com.hemantdave.inventoryappfinalproject;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText name, qunatity, price, emailID;
    File imageFile;
    Uri imageUri;
    String imageName = "Item_";
    Button imageCaputre;
    LinearLayout dialogLayout;
    ArrayList<AppInventorPOJO> dataPOJO;
    DbHelper sqlHelper;
    ListView inventory;


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initilizing the items
        sqlHelper = new DbHelper(MainActivity.this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        inventory = (ListView) findViewById(R.id.appInventorLV);
        dataPOJO = sqlHelper.getInventory();
        inventory.setAdapter(new InventoryListViewCustomAdapter(MainActivity.this, dataPOJO));
        imageName += imageName + sqlHelper.getCount();
        assert fab != null;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // A Linear Layout having views for input and image caputre
                dialogLayout = new LinearLayout(MainActivity.this);
                dialogLayout.setOrientation(LinearLayout.VERTICAL);

                //Name Field
                name = new EditText(MainActivity.this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                name.setHint("Enter the product name");
                name.setLayoutParams(params);
                dialogLayout.addView(name);
                //The Quantity
                qunatity = new EditText(MainActivity.this);
                LinearLayout.LayoutParams QunatityParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                qunatity.setHint("Enter the product qunatity");
                qunatity.setLayoutParams(QunatityParams);
                dialogLayout.addView(qunatity);
                //Price Field
                price = new EditText(MainActivity.this);
                LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                price.setHint("Enter the product price");
                price.setLayoutParams(priceParams);
                dialogLayout.addView(price);
                //Email Field
                emailID = new EditText(MainActivity.this);
                final LinearLayout.LayoutParams emailIdParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                emailID.setHint("Enter the Suppliers Email Id");
                emailID.setLayoutParams(emailIdParams);
                dialogLayout.addView(emailID);
                //Image Capture Button
                imageCaputre = new Button(MainActivity.this);
                LinearLayout.LayoutParams imgParameter = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                imageCaputre.setText("Capture Image for Project");
                imageCaputre.setLayoutParams(imgParameter);
                //setting OnClick listner
                imageCaputre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        takePicture();
                    }
                });

                dialogLayout.addView(imageCaputre);
                //Creating Dialog box
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add Products?")
                        .setMessage("Enter Specific Details?")
                        .setView(dialogLayout)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String input_name = name.getText().toString();
                                String input_Quantity = qunatity.getText().toString();
                                String input_price = price.getText().toString();
                                String intput_email = emailID.getText().toString();
                                //Checking if input field is empty
                                if (isValidEmail(intput_email) == true && isvaliditem(input_name) == true && isvaliditem(input_Quantity) && isvaliditem(input_price)) {
                                   //Checking if Quantity field is integer or not
                                    if (isVaildInt(input_Quantity)) {
                                        if (imageFile != null) {
                                            sqlHelper.addInventoryData(new AppInventorPOJO(input_name, input_Quantity, input_price, "0", intput_email, imageFile.getAbsolutePath()));
                                        } else {
                                            sqlHelper.addInventoryData(new AppInventorPOJO(input_name, input_Quantity, input_price, "0", intput_email, ""));
                                        }
                                        //Resyncing the list view for latest data
                                        refresh();
                                        Toast.makeText(MainActivity.this, "Entry added", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Not a Number", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "You Must Enter Something", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Item Entry Cancelled !", Toast.LENGTH_LONG).show();

                    }
                }).create().show();
            }
        });
        inventory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(MainActivity.this, InventoryDetail.class);
                //getting the current clicked item id from the tag of the item view
                int currrentItemId = Integer.parseInt(view.getTag().toString());
                i.putExtra("id", currrentItemId);
                startActivity(i);
            }
        });
    }
    //resyncing the data in listview
    public void refresh() {
        InventoryListViewCustomAdapter adapter = new InventoryListViewCustomAdapter(MainActivity.this, sqlHelper.getInventory());
        inventory.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //resyncing for changes done from detail activity
        refresh();

    }
    //starting/Managing camera intent
    private void takePicture() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageName);
        imageUri = Uri.fromFile(imageFile);
        i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        i.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(i, 0);
    }
    //checking integer validity
    public boolean isVaildInt(String data) {
        return TextUtils.isDigitsOnly(data);
    }
    //checking is valid email or not
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    //increasing the sales and decresing the quantity on clicking sold one button
    public void modifyInventory(View v) {
        int id = Integer.parseInt(v.getTag().toString());
        sqlHelper.modifyitemsSalesStats(id);
        Toast.makeText(MainActivity.this, "Item Sold !", Toast.LENGTH_SHORT).show();
        refresh();
    }
    //checking supplied item is blank or not
    public final static boolean isvaliditem(String text) {
        if (text == null | text.equals("")) {
            return false;
        } else {
            return true;
        }
    }
    //Cheking the camera activity resultcode for making decision wether pic was taken or not
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {;
            imageCaputre.setText(imageFile.getName());
        } else if (requestCode == RESULT_CANCELED) {
            Toast.makeText(MainActivity.this, "did not worked", Toast.LENGTH_SHORT).show();

        }
    }
}

