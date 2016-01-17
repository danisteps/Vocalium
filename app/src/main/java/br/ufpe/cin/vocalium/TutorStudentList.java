package br.ufpe.cin.vocalium;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseObject;

import java.util.List;

import Utils.DatabaseManager;
import Utils.UserInformation;

public class TutorStudentList extends AppCompatActivity {
    private final static Class nextActivity = TutorSoundList.class;
    private final static Class addStudentActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tutor_student_list);

        TextView textView = (TextView) findViewById(R.id.name_tutor_textview);
        textView.setText(UserInformation.getInstance().GetTutorName());


        ListView listView = (ListView) findViewById(R.id.list_view_tutor_student_list);
        inflateListView(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
                updateUserInformation(adapter, position);

                changeActivity();
            }
        });

        FloatingActionButton floatButton = (FloatingActionButton) findViewById(R.id.add_button_tutor_student_list);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAddStudentActivity();
            }
        });
    }
    private void inflateListView (ListView listView)
    {
        Pair<Integer, String>[] elementsPair;

        List<ParseObject> results = DatabaseManager.getStudentsFromTutor(UserInformation.getInstance().GetTutorId());
        elementsPair = new Pair[results.size()];

        for(int i = 0; i < results.size(); i ++)
        {
            ParseObject student = results.get(i);
            elementsPair[i] = new Pair<>(student.getInt("StudentId"), student.getString("StudentName"));

        }

        listView.setAdapter(new SoundRowAdapter(this, elementsPair));
    }
    private void updateUserInformation(AdapterView<?> adapter, int position)
    {
        UserInformation user = UserInformation.getInstance();
        user.SetStudentId((int)adapter.getItemIdAtPosition(position));
        user.SetStudentName((String) adapter.getItemAtPosition(position));
    }
    private void changeActivity()
    {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
    }
    private void changeAddStudentActivity()
    {
        Log.e("CONFIG_ERROR", "button clicked");
        createAddStudentDialog();
    }
    private void createAddStudentDialog()
    {
        Log.e("COMMENT_ERROR", "calling dialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Usuário que deseja adicionar");
        final EditText editText = new EditText(this);
        builder.setView(editText);


        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                createRequest(editText.getText().toString());
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        final AlertDialog alert = builder.create();
        alert.show();
    }
    private void createRequest(String username)
    {
        String message = "";

        ParseObject user = DatabaseManager.getInformationByUsername(username);
        if(user == null) message = "Nome de usuário não encontrado";
        else
        {
            DatabaseManager.LoginType type = DatabaseManager.checkObjectType(user);
            if(type != DatabaseManager.LoginType.Student) message = "Usuário não é estudante";
            else
            {
                if(DatabaseManager.getTutorIdFromStudentObject(user) != -1)  message = "Usuário já possui um tutor";
                else
                {
                    int studentId = DatabaseManager.getIdFromObject(user, DatabaseManager.LoginType.Student);
                    DatabaseManager.requestFriendship(UserInformation.getInstance().GetTutorId(), studentId);
                    message = "Requisição enviada";
                }
            }
        }
        showRequestDialog(message);
    }
    private void showRequestDialog(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);


        final AlertDialog alert = builder.create();
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

        handler.postDelayed(runnable, 2200);
    }

    @Override
    public void onBackPressed() {

    }

}
