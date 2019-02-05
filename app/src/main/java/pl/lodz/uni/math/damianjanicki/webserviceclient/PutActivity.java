package pl.lodz.uni.math.damianjanicki.webserviceclient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class PutActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextBody;
    private EditText editTextUserId;
    private EditText editTextID;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put);

        editTextTitle = findViewById(R.id.editTextTitleToPut);
        editTextBody = findViewById(R.id.editTextBodyToPut);
        editTextUserId = findViewById(R.id.editTextUserIdToPut);
        editTextID = findViewById(R.id.editTextIdToPut);

        Button buttonPutData = findViewById(R.id.buttonPutData);
        buttonPutData.setOnClickListener(buttonPutDataOnClickListener);

        progressBar = findViewById(R.id.progressBarToPut);

    }

    private View.OnClickListener buttonPutDataOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonPutDataClicked();
        }
    };

    private void buttonPutDataClicked() {
        if(isEditTextsEmpties()){
            showShortToast("Please input all values");
        } else {
            if(ConnectivityHelper.isConnectedToNetwork(this)) {
                new PutActivity.PutTask().execute();
            } else{
                showShortToast("Check network status");
            }
        }
    }

    private void showShortToast(String text) {
        Toast.makeText(PutActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    private boolean isEditTextsEmpties() {
        return (editTextUserId.getText().toString().isEmpty() || editTextTitle.getText().toString().isEmpty()
                || editTextBody.getText().toString().isEmpty() || editTextID.getText().toString().isEmpty());
    }

    private class PutTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                URL restApiEndpoint = new URL(MainActivity.API_URL + editTextID.getText().toString());
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) restApiEndpoint.openConnection();
                httpsURLConnection.setRequestMethod("PUT");
                Map<String, String> data = new HashMap<>();
                data.put("id", editTextID.getText().toString());
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
            Log.d("PutActivity", responseCode.toString());
            if (responseCode == 200) {
                onPostSuccessful();
            } else {
                onPostFail();
            }
            progressBar.setVisibility(View.GONE);
        }
    }


    private void onPostSuccessful() {
        showShortToast("Data updated successfully!");
        editTextTitle.setText("");
        editTextBody.setText("");
        editTextUserId.setText("");
        editTextID.setText("");
    }

    private void onPostFail() {
        showShortToast("Data not updated, fail!");
    }
}
