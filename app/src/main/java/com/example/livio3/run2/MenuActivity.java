package com.example.livio3.run2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by livio3 on 23/07/18.
 */

public class MenuActivity extends AppCompatActivity {



    Button nextRaces;
    Button loggout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        nextRaces = findViewById(R.id.nextRaces);
        nextRaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = getIntent().getExtras();
                String idMember = data.getString(LoginActivity.KEY_ID);
                Intent intent = new Intent(MenuActivity.this, ListaGare.class);
                intent.putExtra(LoginActivity.KEY_ID, idMember); //si deve passare l'id del socio
                startActivity(intent);
            }
        });
        loggout = findViewById(R.id.loggout);
        loggout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
