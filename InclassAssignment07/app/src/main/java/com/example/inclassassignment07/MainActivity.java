/*Group : 34
* Name: Tejaswini Oduru
* Name: Sumit Kawale
* */package com.example.inclassassignment07;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText song;
    SeekBar seekBar;
    Button search;
    RadioButton trackRating;
    RadioButton artistRating;
    ProgressBar progressBar;
    TextView sbValue;
    RadioGroup radioGroup;
    String selectRadio = "s_track_rating";
    int selectedRating;
    int sb_limit=5;
    ListView lv;
    ArrayList<musix> musicArrayList = new ArrayList<musix>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        song = findViewById(R.id.editText);
        seekBar = findViewById(R.id.seekBar);
        search = findViewById(R.id.button);
        radioGroup = findViewById(R.id.radioGroup);
        trackRating = findViewById(R.id.radioButton);
        artistRating = findViewById(R.id.radioButton2);
        progressBar = findViewById(R.id.progressBar);
        sbValue = findViewById(R.id.seekbarvalue);
        lv = findViewById(R.id.listView);
        seekBar.setMax(25);
        seekBar.setMin(5);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sb_limit = progress;
                sbValue.setText(progress+" ");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedRating=group.getCheckedRadioButtonId();
                String songName = song.getText().toString();
                if(selectedRating==R.id.radioButton)
                {
                    selectRadio="s_track_rating";
                    if (songName.length() != 0 && sb_limit != 0) {
                        new GetMusixAsync().execute("http://api.musixmatch.com/ws/1.1/track.search&" + "q=" + songName + "&page_size=" + sb_limit + "&" + selectRadio + "desc&apikey=e58bcbda890fe810632950d3cc39bb2e");
                    } else {
                        song.setError("Need song name");
                    }


                }
                else if (selectedRating==R.id.radioButton2)
                {
                    selectRadio="s_artist_rating";
                    if (songName.length() != 0 && sb_limit != 0) {
                        new GetMusixAsync().execute("http://api.musixmatch.com/ws/1.1/track.search&" + "q=" + songName + "&page_size=" + sb_limit + "&" + selectRadio + "desc&apikey=e58bcbda890fe810632950d3cc39bb2e");
                    } else {
                        song.setError("Need song name");
                    }
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String songName = song.getText().toString();
                if (songName.length() != 0) {
                    if (isConnected()) {
                        progressBar.setVisibility(View.VISIBLE);
                        new GetMusixAsync().execute("http://api.musixmatch.com/ws/1.1/track.search&" + "q=" + songName
                                + "&page_size=" + sb_limit + "&" + selectRadio + "desc&apikey=e58bcbda890fe810632950d3cc39bb2e");
                    } else {
                        Toast.makeText(MainActivity.this, "Internet is not connected", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(MainActivity.this, "Need song name", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private class GetMusixAsync extends AsyncTask<String, Void, ArrayList<musix>> {
        @Override
        protected ArrayList<musix> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<musix> result = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF-8");
                    JSONObject root = new JSONObject(json);
                    JSONObject message = root.getJSONObject("message");
                    JSONObject body = message.getJSONObject("body");
                    JSONArray musix = body.getJSONArray("track_list");
                    for (int i = 0; i < musix.length(); i++) {
                        JSONObject newsJson = musix.getJSONObject(i);
                        JSONObject obj = newsJson.getJSONObject("track");
                        musix music = new musix();
                        music.track_name = obj.getString("track_name");
                        music.album_name = obj.getString("album_name");
                        music.artist_name = obj.getString("artist_name");
                        music.updated_time = obj.getString("updated_time");
                        music.track_share_url = obj.getString("track_share_url");
                        result.add(music);

                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }
        protected void onPostExecute(ArrayList<musix> music) {

            displayMusic(music);
        }
    }
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected() &&
                (networkInfo.getType() == ConnectivityManager.TYPE_WIFI
                        || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }
    protected void displayMusic(ArrayList<musix> music){
        musicArrayList=music;
        MusicAdapter adapter = new MusicAdapter(this, R.layout.musix_list, music);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(musicArrayList.get(position).track_share_url));
                startActivity(i);
            }
        });
        progressBar.setVisibility(View.INVISIBLE);

    }
}
