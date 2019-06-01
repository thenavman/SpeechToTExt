package com.example.speechtotext;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button b1;
    TextView t1;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String timesysa;
    private final int REQ_CODE_SPEECH_INPUT = 2;
    private static final int STORAGE_PERMISSION_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        operation();
        b1.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                performoperation();
            }

            private void performoperation() {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);//performs an action for speech recognisation
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);//supports multiple lang and maintain a lang free form
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());//using default system lang
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "hi Rohan speak something");//displaying additional messege to the prompt
                try {
                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);//we pass object of intent and request code in start activity for result
                    // start activity for result method is followed by on activity result method
                } catch (ActivityNotFoundException a) {

                }
            }
        });

    }

    private void operation() {
        b1 = findViewById(R.id.imageButton);
        t1 = findViewById(R.id.textView2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    t1.setText(result.get(0));
                    //save speech to text file
                    writedatainfile(t1.getText().toString());

                }
                break;
            }
        }
    }

    private void writedatainfile(String text) {
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        timesysa = simpleDateFormat.format(calendar.getTime());
        timesysa = "ExternalData" + timesysa + ".txt";
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File myFile = new File(folder, timesysa);
        writeData(myFile, text);

    }

    private void writeData(File myFile, String result) {
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(myFile);
            fileOutputStream.write(result.getBytes());
            Toast.makeText(this, "Done" + myFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

