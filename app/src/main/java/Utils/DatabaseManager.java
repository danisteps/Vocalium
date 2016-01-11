package Utils;

import android.content.Context;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Délio on 10/01/2016.
 */
public class DatabaseManager {

    public static boolean login (String userName, int passwordHash)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Login");

        query.whereEqualTo("UserName", userName).whereEqualTo("PasswordHash", passwordHash);
        List<ParseObject> results = null;
        try {
            results = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (results.size() == 0)
        {
            Log.e("DATABASE_ERROR", "failed to login");
            return false;
        }
        ParseObject result = results.get(0);

        if(result.getString("LoginType").compareTo("Tutor") == 0)
        {
            ParseObject tutorInformation = getInformation(result.getInt("id"), LoginType.Tutor);

            if(tutorInformation == null) return false;

            UserInformation.getInstance().populateTutorInformation(tutorInformation);
            return true;
        }


        ParseObject studentInformation = getInformation(result.getInt("id"), LoginType.Student);
        if(studentInformation == null) return false;
        ParseObject tutorInformation = getInformation(result.getInt("id"), LoginType.Tutor);
        if(tutorInformation == null) return false;
        String tutorName = tutorInformation.getString("Name");

        UserInformation.getInstance().populateStudentInformation(studentInformation, tutorName);

        return true;
    }



/*------------NEVER CALL THIS AGAIN!!!------------------
    public static void createSingletonClass(Context context)
    {Parse.enableLocalDatastore(context);

        Parse.initialize(context);

        ParseObject obj = new ParseObject("Ids");
        obj.put("LastTutorId", 0);
        obj.put("LastStudentId", 0);
        obj.saveInBackground();

        Log.e("DATABASE_ERROR", "Class created");
    }
    ---------------------ID FUNCTIONS-----------------------------------*/
    public enum LoginType
    {
        Tutor,
        Student
    }
    private static String getColumnName (LoginType loginType)
    {
        String columnName = "";
        switch (loginType)
        {
            case Tutor:
                columnName = "LastTutorId";
                break;
            case Student:
                columnName = "LastStudentId";
        }
        return columnName;
    }

    private static int getLastId(LoginType loginType)
    {
        String columnName = getColumnName(loginType);

        ParseQuery<ParseObject> query=ParseQuery.getQuery("Ids");

        List<ParseObject> results = null;
        int lastId = -1;
        try {
            results = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (results.size() > 0)
        {
            lastId = (int)results.get(0).get(columnName);
        }
        Log.e("DATABASE_ERROR", "" + lastId);
        return lastId;
    }

    private static void increaseLastId(LoginType loginType)
    {

        ParseQuery<ParseObject> query=ParseQuery.getQuery("Ids");
        List<ParseObject> results = null;
        try {
            results = query.find();
        } catch (ParseException e) {
            Log.e("DATABASE_ERROR", "Something wrong with database");
        }
        increaseLastId(results.get(0), loginType);
    }
    private static void increaseLastId (ParseObject object, LoginType loginType)
    {
        String columnName = getColumnName(loginType);

        object.increment(columnName);
        object.saveInBackground();
    }

    //--------------------------------GET INFORMATION---------------------------------------

    private static ParseObject getInformation(int id, LoginType type)
    {
        String tableName = "";
        switch (type)
        {
            case Tutor:
                tableName = "Tutor";
                break;
            case Student:
                tableName = "Student";
        }

        ParseQuery<ParseObject> query=ParseQuery.getQuery(tableName);

        List<ParseObject> results = null;
        try {
            results = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return results.get(0);
    }

    private static ParseObject getTutorInformation(int id)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Tutor");
        query.whereEqualTo("TutorId", id);

        List<ParseObject> results = null;
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return results.get(0);
    }

    public static List<ParseObject> getStudentsFromTutor(int id)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Student");
        query.whereEqualTo("TutorId", id);

        List<ParseObject> results = null;
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static List<ParseObject> getSounds (int tutorId, int studentId)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Sound");
        query.whereEqualTo("TutorId", tutorId).whereEqualTo("StudentId", studentId);

        List<ParseObject> results = null;
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return results;
    }





    //------------------------------------------------------------------------------------

    //DEBUG METHODS!!!!!
    public static void signUpTutor(String userName, String name, int passwordHash)
    {
        int tutorId = getLastId(LoginType.Tutor);

        ParseObject obj = new ParseObject("Tutor");
        obj.put("Name", name);
        obj.put("TutorId", tutorId);
        obj.saveInBackground();

        signUpInformation(userName, passwordHash, tutorId, LoginType.Tutor);

        increaseLastId(LoginType.Tutor);
    }
    public static void signUpStudent(String userName, String name, int passwordHash, int tutorId)
    {
        int studentId = getLastId(LoginType.Student);

        ParseObject obj = new ParseObject("Student");
        obj.put("StudentName", name);
        obj.put("StudentId", studentId);
        obj.put("TutorId", tutorId);
        obj.saveInBackground();

        signUpInformation(userName, passwordHash, studentId, LoginType.Student);

        increaseLastId(LoginType.Student);
    }
    private static void signUpInformation (String userName, int passwordHash, int id, LoginType type)
    {
        String typeName = "";
        switch (type)
        {
            case Tutor:
                typeName = "Tutor";
                break;
            case Student:
                typeName = "Student";
        }

        ParseObject obj = new ParseObject("Login");
        obj.put("UserName", userName);
        obj.put("PasswordHash", passwordHash);
        obj.put("Id", id);
        obj.put("LoginType", typeName);
        obj.saveInBackground();
    }
    public static void saveSound(int tutorId, int studentId, int soundId)
    {
        ParseObject obj = new ParseObject("Sound");
        obj.put("TutorId", tutorId);
        obj.put("StudentId", studentId);
        obj.put("SoundId", soundId);
        obj.saveInBackground();
    }

    //-------------------

    public static void initializeParse(Context context)
    {

        Parse.enableLocalDatastore(context);

        Parse.initialize(context);
    }


    //DatabaseManager.signUpTutor("joao", "João da silva", "123456".hashCode());
    //DatabaseManager.signUpStudent("creuza", "Creuza ana", "987654".hashCode(), 1);
    //DatabaseManager.signUpStudent("marco", "Marco Araujo", "321654".hashCode(), 1);
    //DatabaseManager.signUpStudent("olindo", "Olindo Tobias", "0123".hashCode(), 1);
    //DatabaseManager.signUpStudent("rafael", "Rafael Tajares", "54321".hashCode(), 1);

    /*
    private void poupulateSounds()
    {
        DatabaseManager.saveSound(1, 4, 1);
        DatabaseManager.saveSound(1, 4, 2);
        DatabaseManager.saveSound(1, 4, 3);
        DatabaseManager.saveSound(1, 5, 0);
        DatabaseManager.saveSound(1, 5, 4);
        DatabaseManager.saveSound(1, 5, 5);
        DatabaseManager.saveSound(1, 6, 6);
        DatabaseManager.saveSound(1, 6, 7);
        DatabaseManager.saveSound(1, 6, 8);
        DatabaseManager.saveSound(1, 6, 9);
        DatabaseManager.saveSound(1, 6, 10);
        DatabaseManager.saveSound(1, 7, 11);
        DatabaseManager.saveSound(1, 7, 12);
        DatabaseManager.saveSound(1, 7, 13);
    }
    */
}
