package com.seohyun.sublindwaya;

import static android.content.ContentValues.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;
import java.util.Locale;

public class TextToSpeech extends AppCompatActivity {

    private android.speech.tts.TextToSpeech tts;
    private Button ttsButton;
    private EditText txtText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);

        // 텍스트를 음성으로 변환하기 위한 TextToSpeech 객체 생성
        tts = new android.speech.tts.TextToSpeech(this, new android.speech.tts.TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=android.speech.tts.TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

        ttsButton = findViewById(R.id.btnSpeak);
        txtText = findViewById(R.id.txtText);

        ttsButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override public void onClick(View v) {
                String text = txtText.getText().toString();
//                String text = "한성대입구역";
                // 자모 분리 코드 추가 부분
                List<List<String>> jamoSplit = JamoUtils.split(text.trim()); // str에서 개행 문자 제거 후 자모 분리
                StringBuilder sb = new StringBuilder();
                for (List<String> jamo : jamoSplit) {
                    sb.append(String.join("", jamo)); // 자모를 쉼표로 연결하여 한 글자씩 줄바꿈 추가
                }
                Log.d(TAG, "분리된 자모:\n" + sb.toString());


                tts.setPitch(1.0f);
                tts.setSpeechRate(1.0f);
                tts.speak(text, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }
    @Override public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
        super.onDestroy();
    }

}