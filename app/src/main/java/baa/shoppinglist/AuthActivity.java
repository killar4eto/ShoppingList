package baa.shoppinglist;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
    }

    public void PickPicture(View v){
        Context context = getApplicationContext();
        Toast.makeText(context.getApplicationContext(), "Not working at the moment. Try again later", Toast.LENGTH_LONG).show();
    }
}
