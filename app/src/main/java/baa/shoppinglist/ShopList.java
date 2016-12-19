package baa.shoppinglist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;

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

    }

    //MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.additem:
                AddItem();
                return true;

            case R.id.share:
                Share();
                return true;

            case R.id.clearAll:
                ClearAll();
                return true;

            case R.id.changeBG:
                ChangeBackground();
                return true;

            case R.id.revertBG:
                RevertBackground();
                return true;

            case R.id.signout:
                SignOut();
                return true;

            default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void RevertBackground() {

        View mLayout = findViewById(R.id.activity_shop_list);

        mLayout.setBackgroundColor(Color.rgb(219,164,0));

    }

    private void ChangeBackground() {
        View mLayout = findViewById(R.id.activity_shop_list);

        mLayout.setBackgroundColor(Color.rgb(153, 204, 0));
    }

    private void Share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); //MIME type
        String textToShare = String.valueOf(mRecyclerView);
        intent.putExtra(Intent.EXTRA_TEXT, textToShare);
        startActivity(intent);
    }

    private void ClearAll() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        conn.removeValue();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Clearing the list? Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    private void AddItem() {

        Intent intent = new Intent(getBaseContext(), AddItem.class);
        startActivity(intent);
        finish();

    }

    private void SignOut() {
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

    @Override
    protected void onStart() {
        super.onStart();

        //UserID
        //final FirebaseUser userID = FirebaseAuth.getInstance().getCurrentUser();

        final Firebase ItemRef = conn;

        FirebaseRecyclerAdapter<ShoppingItems, ItemViewHolder> adapter = new FirebaseRecyclerAdapter<ShoppingItems, ItemViewHolder>(ShoppingItems.class, R.layout.custom_list, ItemViewHolder.class, ItemRef) {
            @Override
            protected void populateViewHolder(ItemViewHolder itemViewHolder, final ShoppingItems item, final int position) {
                final Firebase posRef = this.getRef(position);

                itemViewHolder.label.setText(item.getLabel());
                itemViewHolder.qty.setText(String.valueOf(item.getQty()));

                itemViewHolder.itemView.findViewById(R.id.item_del).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String oldItem = item.getLabel();
                        final int oldQty = item.getQty();

                        posRef.removeValue();

                        View coordinatorLayout = getCurrentFocus();
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, item.getLabel() + " has been deleted!", Snackbar.LENGTH_LONG)
                                .setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        ShoppingItems restoreData = new ShoppingItems(oldItem, oldQty);
                                        conn.push().setValue(restoreData);

                                        Snackbar snackbar1 = Snackbar.make(getCurrentFocus(), oldItem + " was restored!", Snackbar.LENGTH_SHORT);

                                        snackbar1.show();
                                    }
                                });

                        snackbar.show();

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
}
