package com.example.david.callwatcher;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.button_id);
        final TextView textView = (TextView) findViewById(R.id.text_id);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            }
        });



    }



}
