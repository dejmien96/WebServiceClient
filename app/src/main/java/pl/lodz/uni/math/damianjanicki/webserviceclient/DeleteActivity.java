package pl.lodz.uni.math.damianjanicki.webserviceclient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DeleteActivity extends AppCompatActivity {

    private EditText editTextNumberToDelete;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        editTextNumberToDelete = findViewById(R.id.editTextNumbetToDelete);
        Button buttonDeleteData = findViewById(R.id.buttonDeleteData);
        buttonDeleteData.setOnClickListener(buttonDeleteDataOnClickListener);

        progressBar = findViewById(R.id.progressBarOnDelete);
    }

    private View.OnClickListener buttonDeleteDataOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonDeleteDataClicked();
        }
    };

    private void buttonDeleteDataClicked() {
        if(ConnectivityHelper.isConnectedToNetwork(this)){
            new DeleteTask().execute();
        }
        else{
            Toast.makeText(DeleteActivity.this, "Check network status", Toast.LENGTH_SHORT).show();
        }

    }

    private class DeleteTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                URL restApiEndpoint = new URL(MainActivity.API_URL + editTextNumberToDelete.getText().toString());
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) restApiEndpoint.openConnection();
                httpsURLConnection.setRequestMethod("DELETE");
                return httpsURLConnection.getResponseCode();

            } catch (Exception e) {
                Toast.makeText(DeleteActivity.this, "Error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            if (responseCode == 200) {
                onDeleteSuccessful();
            } else {
                onDeleteFail();
            }
            progressBar.setVisibility(View.GONE);

        }
    }

    private void onDeleteSuccessful() {
        Toast.makeText(DeleteActivity.this, "Post deleted successfully", Toast.LENGTH_SHORT).show();
    }

    private void onDeleteFail() {
        Toast.makeText(DeleteActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
    }
}
