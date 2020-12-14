package com.example.cddlemptyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    Button entrarButton;
    EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        entrarButton = findViewById(R.id.entrarBtn);
        emailEditText = findViewById(R.id.emailET);


        entrarButton.setOnClickListener(action());
    }



    private View.OnClickListener action(){

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("email", emailEditText.getText().toString());
                intent.putExtras(bundle);

                startActivity(intent);
                finish();
            }
        };
    }
}