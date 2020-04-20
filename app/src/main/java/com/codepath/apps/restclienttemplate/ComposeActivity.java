package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    private EditText etCompose;
    private Button btnTweet;
    private TwitterClient client;
    private TextView tvcount;
    public static   final int Max_Tweet_Lenght=140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);
        tvcount = findViewById(R.id.tvcount);

        client = TwitterApp.getRestClient(this);
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent = etCompose.getText().toString();
                if(tweetContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this, "Your tweet is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(tweetContent.length() > 140){
                    Toast.makeText(ComposeActivity.this, "Your tweet is too long", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_LONG).show();
                client.composeTweet(tweetContent, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Toast.makeText(ComposeActivity.this, "Success, Tweet Added", Toast.LENGTH_SHORT).show();
                        try {
                            Tweet tweet = Tweet.fromJson(response);
                            Intent data = new Intent();
                            data.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK, data);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Toast.makeText(ComposeActivity.this, "Failed to Post Tweet", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvcount.setText(String.valueOf(editable.toString().length()) + "/140");

            }
        });



    }


}




