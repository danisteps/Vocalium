package br.ufpe.cin.vocalium;

import android.content.Intent;
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
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.signup_screen);

        final EditText nameText = (EditText) findViewById(R.id.nameSignup);
        final EditText userText = (EditText) findViewById(R.id.usernameSignup);
        final EditText passwordText = (EditText) findViewById(R.id.senhaSignup);
        final EditText emailText = (EditText) findViewById(R.id.emailSignup);

        final RadioButton radioButton_professor = (RadioButton) findViewById(R.id.radioProfessorSignup);
        RadioButton radioButton_student = (RadioButton) findViewById(R.id.radioAlunoSignup);

        //botão de confirmar cadastro
        Button signupButton = (Button) findViewById(R.id.confirmarSignup);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButton_professor.isActivated()){
                    user.signUpTutor(userText.getText(), nameText.getText(), passwordText.getText());
                }
                else{
                    user.signUpStudent(userText.getText(), nameText.getText(), passwordText.getText());
                }
            }
        });


    }
}