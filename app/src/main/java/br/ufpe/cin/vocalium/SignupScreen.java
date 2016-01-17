package br.ufpe.cin.vocalium;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.lang.reflect.Method;

import Utils.DatabaseManager;
import Utils.FileManager;
import Utils.ServerConnection;
import Utils.UserInformation;

/**
 * Created by danielesoarespassos on 16/01/2016.
 */

public class SignupScreen extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);

        final EditText nameText = (EditText) findViewById(R.id.nameSignup);
        final EditText userText = (EditText) findViewById(R.id.usernameSignup);
        final EditText passwordText = (EditText) findViewById(R.id.senhaSignup);
        final EditText confirmpasswordText = (EditText) findViewById(R.id.confirmaSenha);
        //final EditText emailText = (EditText) findViewById(R.id.emailSignup);

        final RadioButton radioButton_professor = (RadioButton) findViewById(R.id.radioProfessorSignup);
        //RadioButton radioButton_student = (RadioButton) findViewById(R.id.radioAlunoSignup);

        //botão de confirmar cadastro
        Button signupButton = (Button) findViewById(R.id.confirmarSignup);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status = false;

                if (verifyPassword(passwordText.getText().toString(), confirmpasswordText.getText().toString())) {

                    if (radioButton_professor.isActivated()) {
                        status = DatabaseManager.signUpTutor(userText.getText().toString(), nameText.getText().toString(), passwordText.getText().hashCode());
                    } else {
                        status = DatabaseManager.signUpStudent(userText.getText().toString(), nameText.getText().toString(), passwordText.getText().hashCode());
                    }

                    if (status)
                        finish();
                    else
                        operationFailed("O nome de usuário escolhido já existe no sistema.");
                }
            }
        });
    }

    private boolean verifyPassword (String pass1, String pass2){
        if (!pass1.equals(pass2)){
            operationFailed("Senhas diferentes. Por favor, verifique a senha e digite novamente!");
            return false;
        }
        return true;
    }

    private void operationFailed (String message)
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

}