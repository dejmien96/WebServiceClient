package pl.lodz.uni.math.damianjanicki.webserviceclient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class PostActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextBody;
    private EditText editTextUserId;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextBody = findViewById(R.id.editTextBody);
        editTextUserId = findViewById(R.id.editTextUserIdToPost);

        Button buttonPostData = findViewById(R.id.buttonPostData);
        buttonPostData.setOnClickListener(buttonPostDataOnClickListener);

        progressBar = findViewById(R.id.progressBar);

    }

    private View.OnClickListener buttonPostDataOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonPostDataClicked();
        }
    };

    private void buttonPostDataClicked() {
        if(isEditTextsEmpties()){
            showShortToast("Please input all values");
        } else {
            if(ConnectivityHelper.isConnectedToNetwork(this)) {
                new PostTask().execute();
            } else{
                showShortToast("Check network status");
            }
        }
    }

    private void showShortToast(String text) {
        Toast.makeText(PostActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    private boolean isEditTextsEmpties() {
        return(editTextUserId.getText().toString().isEmpty() || editTextTitle.getText().toString().isEmpty() || editTextBody.getText().toString().isEmpty());

    }

    private class PostTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                URL restApiEndpoint = new URL(MainActivity.API_URL);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) restApiEndpoint.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                Map<String, String> data = new HashMap<>();
                data.put("title", editTextTitle.getText().toString());
                data.put("body", editTextBody.getText().toString());
                data.put("userId", editTextUserId.getText().toString());
                JSONObject postData = new JSONObject(data);
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.getOutputStream().write(postData.toString().getBytes());
                return httpsURLConnection.getResponseCode();

            } catch (Exception e) {
                showShortToast("Error");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            if (responseCode == 201) {
                onPostSuccessful();
            } else {
                onPostFail();
            }
            progressBar.setVisibility(View.GONE);
        }
    }


    private void onPostSuccessful() {
        showShortToast("Data added successfully!");
        editTextTitle.setText("");
        editTextBody.setText("");
        editTextUserId.setText("");
    }

    private void onPostFail() {
        showShortToast("Data not added, fail!");
    }
}
