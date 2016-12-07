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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShopList extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        Firebase.setAndroidContext(this);

        mAuth = FirebaseAuth.getInstance();

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://shoppinglist-cb16c.firebaseio.com/");

        mListView = (ListView)findViewById(R.id.listView);

        FirebaseListAdapter<String> myAdapter = new FirebaseListAdapter<String>(this, String.class, android.R.layout.simple_list_item_1, mRef) {
            @Override
            protected void populateView(View view, String s, int i) {
                TextView textView = (TextView)findViewById(android.R.id.text1);
                textView.setText(s);
            }
        };
        mListView.setAdapter(myAdapter);

    }

    public void SignOut(View v){
        //Save token
        SharedPreferences preferences = getSharedPreferences("User_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("isLogged", "");
        editor.commit();

        Toast.makeText(ShopList.this, "Bye, bye hope to see you soon.", Toast.LENGTH_SHORT).show();

        mAuth.signOut();

        startActivity(new Intent(ShopList.this, MainActivity.class));
        finish();
    }
}
