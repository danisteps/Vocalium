package br.ufpe.cin.vocalium;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.Method;

import Utils.DatabaseManager;
import Utils.FileManager;
import Utils.ServerConnection;
import Utils.UserInformation;

public class DownloadStudent extends AppCompatActivity {
    private final static Class nextActivity = StudentListenComment.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_student);

        Method callFunction = null;
        Method errorFunction = null;
        try {
            callFunction = this.getClass().getMethod("changeActivity", null);
            errorFunction = this.getClass().getMethod("backActivity", null);
        } catch (NoSuchMethodException e) {
            backActivity();
            Log.e("POST_ERROR", "problem calling function");
        }

        TextView textView = (TextView) findViewById(R.id.audio_name_student_download_textview);
        int itemNumber = getIntent().getIntExtra(StudentSoundList.EXTRA_INTENT_MESSAGE, -1);
        textView.setText("Áudio " + itemNumber);

        UserInformation user = UserInformation.getInstance();
        Log.e("POST_ERROR", "Clicado no item: " + itemNumber + ", com id : " + user.GetAudioId());
        String path = getFilesDir() + "/";
        path += user.GetAudioId() + FileManager.getExtension(ServerConnection.FileType.Sound);


        int commentId = DatabaseManager.getCommentId(user.GetAudioId());
        String commentPath = getFilesDir() + "/";
        commentPath += commentId + FileManager.getExtension(ServerConnection.FileType.Comment);

        FileManager.verifyOrCreate(path, "" + user.GetAudioId(), ServerConnection.FileType.Sound, this, callFunction, errorFunction);
    }

    public void nextStep ()
    {
        Log.e("UPDATE_ERROR", "starting next step");
        Method callFunction = null;
        Method errorFunction = null;
        try {
            callFunction = this.getClass().getMethod("changeActivity", null);
            errorFunction = this.getClass().getMethod("backActivity", null);
        } catch (NoSuchMethodException e) {
            Log.e("UPDATE_ERROR", "could not find method downloadStudent");
        }

        int commentId = DatabaseManager.getCommentId(UserInformation.getInstance().GetAudioId());
        String commentPath = getFilesDir() + "/";
        commentPath += commentId + FileManager.getExtension(ServerConnection.FileType.Comment);

        FileManager.verifyOrCreate(commentPath, "" + commentId, ServerConnection.FileType.Comment, this, callFunction, errorFunction);
    }
    public void changeActivity() {
        Log.e("UPDATE_ERROR", "changing activity");
        int itemNumber = getIntent().getIntExtra(TutorSoundList.EXTRA_INTENT_MESSAGE, -1);
        Log.e("POST_ERROR", "" + itemNumber);
        Intent intent = new Intent(this, nextActivity);
        intent.putExtra(TutorSoundList.EXTRA_INTENT_MESSAGE, itemNumber);
        startActivity(intent);
        finish();
    }

    public void backActivity() {
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}