package baa.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;

import baa.shoppinglist.models.ShoppingItems;

public class AddItem extends AppCompatActivity {

    private Button saveNow;
    private EditText newItemName;
    private EditText newQty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        final Firebase conn;

        //Connecting to DB
        conn = new Firebase("https://shoppinglist-cb16c.firebaseio.com/");


        saveNow = (Button)findViewById(R.id.saveNow);
        newItemName = (EditText)findViewById(R.id.newItemName);
        newQty = (EditText)findViewById(R.id.newQty);

        newQty.setText("1");

        saveNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newItem = newItemName.getText().toString();
                final int newItemQty = Integer.parseInt(newQty.getText().toString());


                if(TextUtils.isEmpty(newItem)){

                    Toast.makeText(getApplication().getBaseContext(), "Please enter item name.", Toast.LENGTH_LONG).show();
                    return;

                }
                if(newItemQty < 1){

                    Toast.makeText(getApplication().getBaseContext(), "Please enter quantity.", Toast.LENGTH_LONG).show();
                    return;

                }
                else{
                    ShoppingItems newInfo = new ShoppingItems(newItem, newItemQty);
                    conn.push().setValue(newInfo);

                    Toast.makeText(getApplication().getBaseContext(), newItem + " has been added.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getBaseContext(), ShopList.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }
}
