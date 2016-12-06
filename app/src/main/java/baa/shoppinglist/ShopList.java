package baa.shoppinglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ShopList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
    }

    public void SignOut(View v){
        //Save token
        SharedPreferences preferences = getSharedPreferences("User_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("isLogged", "");
        editor.commit();

        startActivity(new Intent(ShopList.this, MainActivity.class));
        finish();
    }
}
