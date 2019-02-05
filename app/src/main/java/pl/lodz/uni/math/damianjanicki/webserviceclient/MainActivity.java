package pl.lodz.uni.math.damianjanicki.webserviceclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public final static String API_URL="https://jsonplaceholder.typicode.com/posts/";  //JSON Place Holder to strona udostępniająca Fake REST API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //przypisujemy zmienne do kazdego przycisku i ustawiamy listenera, ktory wykrywa klikniecie na przycisk i wywoluje metode onClick
        Button buttonGET = findViewById(R.id.buttonGET);
        buttonGET.setOnClickListener(buttonGetOnClickListener);

        Button buttonPostActivity = findViewById(R.id.buttonPostActivity);
        buttonPostActivity.setOnClickListener(buttonPostActivityOnClickListener);

        Button buttonPutActivity = findViewById(R.id.buttonPut);
        buttonPutActivity.setOnClickListener(buttonPutActivityOnClickListener);

        Button buttonDeleteActivity = findViewById(R.id.buttonDeleteActivity);
        buttonDeleteActivity.setOnClickListener(buttonDeleteActivityOnClickListener);
    }

    private View.OnClickListener buttonGetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonGETClicked();
        }
    };

    private void buttonGETClicked() {
        Intent intent = new Intent(this, GetActivity.class);
        startActivity(intent);
    }

    private View.OnClickListener buttonPostActivityOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonPostActivityClicked();
        }
    };

    private void buttonPostActivityClicked() {
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);
    }

    private View.OnClickListener buttonPutActivityOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonPutActivityClicked();
        }
    };

    private void buttonPutActivityClicked() {
        Intent intent = new Intent(this, PutActivity.class);
        startActivity(intent);
    }

    private View.OnClickListener buttonDeleteActivityOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonDeleteActivityClicked();
        }
    };

    private void buttonDeleteActivityClicked() {
        Intent intent = new Intent(this, DeleteActivity.class);
        startActivity(intent);
    }
}
