package baa.shoppinglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

public class ShopList extends AppCompatActivity {

    Firebase conn;
    ListView mListView;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        mListView = (ListView)findViewById(R.id.listView);

        Firebase.setAndroidContext(this);

        //Connecting to DB
        conn = new Firebase("https://shoppinglist-cb16c.firebaseio.com/");
    }

    @Override
    protected void onStart(){
        super.onStart();

        Firebase ItemRef = conn.child("Items");

            final FirebaseListAdapter<String> adapter =
                new FirebaseListAdapter<String>(this, String.class, android.R.layout.simple_list_item_1, ItemRef) {
                    @Override
                    protected void populateView(View v, String s, final int i) {
                        TextView textView = (TextView)v.findViewById(android.R.id.text1);
                        textView.setText(s);

                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(ShopList.this, "Remove item", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                };
        mListView.setAdapter(adapter);

    }

    public void SignOut(View view){
        //Save token
        SharedPreferences preferences = getSharedPreferences("User_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("isLogged", "");
        editor.commit();

        conn.unauth();

        Intent intent = new Intent(ShopList.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
