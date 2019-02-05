package pl.lodz.uni.math.damianjanicki.webserviceclient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GetActivity extends AppCompatActivity {

    private EditText editText;
    private TextView textViewTitle;
    private TextView textViewBody;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);
        Button buttonGetData = findViewById(R.id.buttonGETdata);
        buttonGetData.setOnClickListener(buttonGetDataOnClickListener);
        editText = findViewById(R.id.editText2);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewBody = findViewById(R.id.textViewBody);
        progressBar = findViewById(R.id.progressBarOnGet);
    }


    private View.OnClickListener buttonGetDataOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonGetDataClicked();
        }
    };


    private void buttonGetDataClicked() {
        if (ConnectivityHelper.isConnectedToNetwork(this)) {
            new GetTask().execute(); //tutaj wywolujemy nasze asynchroniczne zadanie
        } else {
            Toast.makeText(GetActivity.this, "Check network status", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {  //przed wykonaniem zadania w tle
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);  //pokazujemy progress bar
        }

        @Override
        protected String doInBackground(Void... voids) {  //zadanie wykonywane w tle
            StringBuilder stringBuilder = new StringBuilder(); //sluzy do tworzenia stringa, jest lepsze niz += poniewaz nie tworzy za kazdym razem obiektu na nowo
            HttpsURLConnection httpsURLConnection = null;
            try {
                URL restApiEndpoint = new URL(MainActivity.API_URL + editText.getText().toString());  //łaczymy sie z api doklejajac do adresu id
                httpsURLConnection = (HttpsURLConnection) restApiEndpoint.openConnection();  //otwieramy polaczenie
                //nie musimy ustawiac setRequestMethod na GET, poniewaz jest to domyslnie

                if (httpsURLConnection.getResponseCode() == 200) {  //jesli otrzymamy kod zwrotny 200 czyli sukces pobrania zawartosci żądanego dokumentu
                    InputStream responseBody = httpsURLConnection.getInputStream();  //pobieramy zawartosc
                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8"); //dekodujemy zawartosc
                    int data = responseBodyReader.read();
                    while (data != -1) { //zczytujemy znak po znaku dopoki są jakies dane
                        stringBuilder.append((char) data);
                        data = responseBodyReader.read();
                    }
                    return stringBuilder.toString();
                }

            } catch (Exception e) {
                Toast.makeText(GetActivity.this, "Error", Toast.LENGTH_SHORT).show();
            } finally {
                if (httpsURLConnection != null) {
                    httpsURLConnection.disconnect();  //zamykamy polaczenie
                }
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String dataFromGet) {  //po wykonaniu zadania w tle
            try {
                JSONObject jsonObject = new JSONObject(dataFromGet);  //tworzymy JSONa na podstawie naszych danych przekazanych w argumencie do tej funkcji
                String title = "TITLE: " + jsonObject.getString("title");  //wyciagamy tytul z naszego JSONa
                textViewTitle.setText(title);
                String body = jsonObject.getString("body");  //wyviagamy body
                textViewBody.setText(body);

            } catch (Exception e) {
                Toast.makeText(GetActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);  //ukrywamy progress bar
        }
    }
}
