package br.ufpe.cin.vocalium;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Method;

import Utils.DatabaseManager;
import Utils.FileManager;
import Utils.ServerConnection;
import Utils.UserInformation;

public class SendRecord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_record);

        Method callback = null;
        Method errorCallback = null;
        try {
            callback = this.getClass().getMethod("finishUpload", null);
            errorCallback = this.getClass().getMethod("errorUpload", null);
        } catch (NoSuchMethodException e) {
            Log.e("POST_ERROR", "Cannot find method");
        }
        ServerConnection.getInstance().setCallback(callback, this);
        ServerConnection.getInstance().setFailureCallback(errorCallback, this);

        final Context context = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerConnection.getInstance().PostFile(context, FileManager.SOUND_TEMP_NAME, ServerConnection.FileType.Sound);
            }
        }).start();
    }

    public void finishUpload()
    {
        Log.e("POST_ERROR", "Sound sent succesfully");
        int newId = ServerConnection.getInstance().getLastIdSet();

        FileManager.RenameFile(this, FileManager.SOUND_TEMP_NAME, ""+newId, ServerConnection.FileType.Sound);

        finish();
    }
    public void errorUpload()
    {
        final Activity activity = this;
        final AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setMessage("Houve um problema no envio do áudio");
        final AlertDialog alert = alertbox.create();
        alert.show();

        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alert.isShowing()) {
                    alert.dismiss();
                }
            }
        };

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
                activity.finish();
            }
        });

        handler.postDelayed(runnable, 18000);
    }

    @Override
    public void onBackPressed() {

    }
}
