package br.ufpe.cin.vocalium;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import Utils.DatabaseManager;
import Utils.UserInformation;

public class LoginScreen extends AppCompatActivity {
    private final static Class nextTutorActivity = TutorStudentList.class;
    private final static Class nextStudentActivity = ProfileStudent.class;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);


        DatabaseManager.initializeParse(this);

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



    }

    private void validateLogin(String userName, String password)
    {

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
            loginFailed();
        }
    }
    private void loginFailed ()
    {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setMessage("Nome de usuário e senha não combinam");
        alertbox.create();
        alertbox.show();
    }
    private void changeActivity(Class activity)
    {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

}
