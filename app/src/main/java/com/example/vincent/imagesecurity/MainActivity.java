package com.example.vincent.imagesecurity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnLock;
    private Button btnUnlock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLock = (Button) findViewById(R.id.btnLock);
        btnUnlock  = (Button) findViewById(R.id.btnUnlock);

        btnLock.setOnClickListener(btnLockOnClickListener);
        btnUnlock.setOnClickListener(btnUnlockOnClickListener);
    }

    View.OnClickListener btnLockOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(MainActivity.this,LockImages.class);
            startActivity(intent);

        }
    };

    View.OnClickListener btnUnlockOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(MainActivity.this,UnlockImages.class);
            startActivity(intent);

        }
    };
}
