package br.ufpe.cin.vocalium;

import Utils.DatabaseManager;
import Utils.LayoutOutput;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by danielesoarespassos on 16/01/2016.
 */

public class SignupScreen extends AppCompatActivity {

    private final static Class nextActivity = LoginScreen.class;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);

        final EditText nameText = (EditText) findViewById(R.id.nameSignup);
        final EditText userText = (EditText) findViewById(R.id.usernameSignup);
        final EditText passwordText = (EditText) findViewById(R.id.senhaSignup);
        final EditText confirmpasswordText = (EditText) findViewById(R.id.confirmaSenha);

        final RadioGroup radioFuncao = (RadioGroup) findViewById(R.id.radioFuncao);

        //botão de confirmar cadastro
        Button signupButton = (Button) findViewById(R.id.confirmarSignup);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status = false;

                if (verifyPassword(passwordText.getText().toString(), confirmpasswordText.getText().toString())) {
                    switch (radioFuncao.getCheckedRadioButtonId()) {
                        case R.id.radioAlunoSignup:
                            status = DatabaseManager.signUpStudent(userText.getText().toString(), nameText.getText().toString(), passwordText.getText().hashCode());

                            break;
                        case R.id.radioProfessorSignup:
                            status = DatabaseManager.signUpTutor(userText.getText().toString(), nameText.getText().toString(), passwordText.getText().hashCode());

                            break;
                    }

                    if (status)
                        changeActivity(nextActivity);
                    else
                        LayoutOutput.showErrorDialog(SignupScreen.this, "O nome de usuário escolhido já existe no sistema.");
                }
            }
        });
    }

    private boolean verifyPassword (String pass1, String pass2){
        if (!pass1.equals(pass2)){
            LayoutOutput.showErrorDialog(this, "Senhas diferentes. Por favor, verifique a senha e digite novamente!");
            return false;
        }
        return true;
    }

    private void changeActivity(Class activity)
    {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        finish();
    }
}