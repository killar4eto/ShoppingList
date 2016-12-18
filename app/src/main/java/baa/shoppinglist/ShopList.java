package baa.shoppinglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import baa.shoppinglist.models.ShoppingItems;

public class ShopList extends AppCompatActivity {

    Firebase conn;
    RecyclerView mRecyclerView;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Firebase.setAndroidContext(this);

        //Connecting to DB
        conn = new Firebase("https://shoppinglist-cb16c.firebaseio.com/");

        //ShoppingItems as = new ShoppingItems("bananas", 5);

        //conn.push().setValue(as);

    }

    @Override
    protected void onStart(){
        super.onStart();

        //UserID
        final FirebaseUser userID = FirebaseAuth.getInstance().getCurrentUser();

        final Firebase ItemRef = conn;

        FirebaseRecyclerAdapter<ShoppingItems, ItemViewHolder> adapter = new FirebaseRecyclerAdapter<ShoppingItems, ItemViewHolder>(ShoppingItems.class, R.layout.custom_list, ItemViewHolder.class, ItemRef) {
            @Override
            protected void populateViewHolder(ItemViewHolder itemViewHolder, ShoppingItems item, final int position) {
                final Firebase posRef = this.getRef(position);

                itemViewHolder.label.setText(item.getLabel());
                itemViewHolder.qty.setText(String.valueOf(item.getQty()));

                itemViewHolder.itemView.findViewById(R.id.item_del).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        posRef.removeValue();

                        Toast.makeText(getApplication().getBaseContext(), posRef + " has been removed by " + userID, Toast.LENGTH_LONG).show();
                    }
                });

            }
        };
        mRecyclerView.setAdapter(adapter);

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView label;
        TextView qty;

        public ItemViewHolder(View v){
            super(v);
            label = (TextView)v.findViewById(R.id.item_label);
            qty = (TextView)v.findViewById(R.id.item_qty);
        }
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
