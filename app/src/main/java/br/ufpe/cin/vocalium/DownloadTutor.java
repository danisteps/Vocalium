package br.ufpe.cin.vocalium;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.Method;

import Utils.FileManager;
import Utils.ServerConnection;
import Utils.UserInformation;

public class DownloadTutor extends AppCompatActivity {
    private final static Class nextActivity = TutorHearComm.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_tutor);

        Method callFunction = null;
        Method errorFunction = null;
        try {
            callFunction = this.getClass().getMethod("changeActivity", null);
            errorFunction = this.getClass().getMethod("backActivity", null);
        } catch (NoSuchMethodException e) {
            backActivity();
            Log.e("POST_ERROR", "problem calling function");
        }

        TextView textView = (TextView) findViewById(R.id.audio_name_tutor_download_textview);
        textView.setText(UserInformation.getInstance().GetStudentName());

        int itemNumber = getIntent().getIntExtra(TutorSoundList.EXTRA_INTENT_MESSAGE, -1);

        UserInformation user = UserInformation.getInstance();
        Log.e("POST_ERROR", "Clicado no item: " + itemNumber + ", com id : "+ user.GetAudioId());
        String path = getFilesDir() + "/";
        path += user.GetAudioId() + FileManager.getExtension(ServerConnection.FileType.Sound);

        FileManager.verifyOrCreate(path, "" + user.GetAudioId(), ServerConnection.FileType.Sound, this, callFunction, errorFunction);
    }

    public void changeActivity ()
    {
        int itemNumber = getIntent().getIntExtra(TutorSoundList.EXTRA_INTENT_MESSAGE, -1);
        Log.e("POST_ERROR", "" + itemNumber);
        Intent intent = new Intent(this, nextActivity);
        intent.putExtra(TutorSoundList.EXTRA_INTENT_MESSAGE, itemNumber);
        startActivity(intent);
        finish();
    }
    public void backActivity ()
    {
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}
