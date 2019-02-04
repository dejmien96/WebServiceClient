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
            new GetTask().execute();
        } else {
            Toast.makeText(GetActivity.this, "Check network status", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpsURLConnection httpsURLConnection = null;
            try {
                URL restApiEndpoint = new URL(MainActivity.API_URL + editText.getText().toString());
                httpsURLConnection = (HttpsURLConnection) restApiEndpoint.openConnection();

                if (httpsURLConnection.getResponseCode() == 200) {
                    InputStream responseBody = httpsURLConnection.getInputStream();
                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                    int data = responseBodyReader.read();
                    while (data != -1) {
                        stringBuilder.append((char) data);
                        data = responseBodyReader.read();
                    }
                    return stringBuilder.toString();
                }

            } catch (Exception e) {
                Toast.makeText(GetActivity.this, "Error", Toast.LENGTH_SHORT).show();
            } finally {
                if (httpsURLConnection != null) {
                    httpsURLConnection.disconnect();
                }
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String dataFromGet) {
            try {
                JSONObject jsonObject = new JSONObject(dataFromGet);
                String title = "TITLE: " + jsonObject.getString("title");
                textViewTitle.setText(title);
                String body = jsonObject.getString("body");
                textViewBody.setText(body);

            } catch (Exception e) {
                Toast.makeText(GetActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        }
    }
}
