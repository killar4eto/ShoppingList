package baa.shoppinglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences myPrefs = this.getSharedPreferences("User_info", MODE_PRIVATE);
        String Logged = myPrefs.getString("isLogged", null);

        if(Logged == null){
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, ShopList.class);
            startActivity(intent);
        }
    }
}
