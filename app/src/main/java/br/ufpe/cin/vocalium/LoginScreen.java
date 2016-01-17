package br.ufpe.cin.vocalium;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import Utils.DatabaseManager;
import Utils.FileManager;
import Utils.UserInformation;

public class LoginScreen extends AppCompatActivity {
    private final static Class nextTutorActivity = TutorStudentList.class;
    private final static Class nextStudentActivity = StudentSoundList.class;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_screen);

        Button loginButton = (Button) findViewById(R.id.login_button);
        final EditText userText = (EditText) findViewById(R.id.text_user_login);
        final EditText passwordText = (EditText) findViewById(R.id.text_password_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userText.getText().toString();
                String password = passwordText.getText().toString();

                validateLogin(userName, password);
            }
        });

        Button signupButton = (Button) findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(SignupScreen.class);
            }
        });
    }

    private void validateLogin(String userName, String password)
    {
        if (TextUtils.isEmpty(userName)) {
            showError("Usuário em branco");

            return;
        }

        if (TextUtils.isEmpty(password)) {
            showError("Senha em branco");

            return;
        }

        if(DatabaseManager.login(userName, password.hashCode()))
        {
            UserInformation user = UserInformation.getInstance();
            Class nextActivity;
            if(user.GetUserType() == UserInformation.UserType.Student)
            {
                nextActivity = nextStudentActivity;
            }
            else
            {
                nextActivity = nextTutorActivity;
            }

            changeActivity(nextActivity);
        }
        else
        {
            showError("O usuário e a senha não coincidem");
        }
    }

    private void showError (String message)
    {
        final AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setMessage(message);
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
            }
        });

        handler.postDelayed(runnable, 3000);
    }

    private void changeActivity(Class activity)
    {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        finish();
    }

}
