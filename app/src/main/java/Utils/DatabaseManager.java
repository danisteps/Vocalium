package Utils;

import android.content.Context;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
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
            ParseObject tutorInformation = getInformation(result.getInt("Id"), LoginType.Tutor);

            if(tutorInformation == null) return false;

            UserInformation.getInstance().populateTutorInformation(tutorInformation);
            return true;
        }


        ParseObject studentInformation = getInformation(result.getInt("Id"), LoginType.Student);
        if(studentInformation == null) return false;

        int tutorId = studentInformation.getInt("TutorId");
        String tutorName = "";
        if(tutorId != -1)
        {
            ParseObject tutorInformation = getInformation(studentInformation.getInt("TutorId"), LoginType.Tutor);
            if(tutorInformation == null) return false;
            tutorName = tutorInformation.getString("Name");
        }

        UserInformation.getInstance().populateStudentInformation(studentInformation, tutorName);

        return true;
    }


    //------------------------SETS-------------------------------------



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
        String condition = "";
        switch (type)
        {
            case Tutor:
                tableName = "Tutor";
                condition = "TutorId";
                break;
            case Student:
                tableName = "Student";
                condition = "StudentId";
        }

        ParseQuery<ParseObject> query=ParseQuery.getQuery(tableName);
        query.whereEqualTo(condition, id);

        List<ParseObject> results = null;
        try {
            results = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return results.get(0);
    }
    public static ParseObject getInformationByUsername(String username)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Login");
        query.whereEqualTo("UserName", username);

        List<ParseObject> results = null;
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(results.size() == 0) return null;

        ParseObject result = results.get(0);

        String type = result.getString("LoginType");
        LoginType loginType;
        if(type.compareTo("Tutor") == 0)
        {
            loginType = LoginType.Tutor;
        }
        else
        {
            loginType = LoginType.Student;
        }

        return getInformation(result.getInt("Id"), loginType);
    }

    public static ParseObject getTutorInformation(int id)
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
    private static String getStudentName(int id)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Student");
        query.whereEqualTo("StudentId", id);

        List<ParseObject> results = null;
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return results.get(0).getString("StudentName");
    }
    private static String getTutorName(int id)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Tutor");
        query.whereEqualTo("TutorId", id);

        List<ParseObject> results = null;
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return results.get(0).getString("Name");
    }
    public static int getCommentId(int soundId)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Sound");
        query.whereEqualTo("SoundId", soundId);

        List<ParseObject> results = null;
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return results.get(0).getInt("CommentId");
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
    public static List<ParseObject> getSoundsCommented (int tutorId, int studentId)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Sound");
        query.whereEqualTo("TutorId", tutorId).whereEqualTo("StudentId", studentId).whereNotEqualTo("CommentId", -1);

        List<ParseObject> results = null;
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return results;
    }
    public static boolean isCommented (int soundId)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Sound");
        query.whereEqualTo("SoundId", soundId);

        List<ParseObject> results = null;
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(results.size() > 0)
        {
            if(results.get(0).getInt("CommentId") != -1)
            {
                return true;
            }
        }
        return false;
    }

    public static ParseObject getRequest (int tutorId, int studentId)
    {

        ParseQuery<ParseObject> query=ParseQuery.getQuery("Request");
        query.whereEqualTo("TutorId", tutorId).whereEqualTo("StudentId", studentId);

        List<ParseObject> results = null;

        try {
            results = query.find();
        } catch (ParseException e) {
            Log.e("DATABASE_ERROR", "cannot request query");
        }

        if(results.size() > 0)
        {
            return results.get(0);
        }
        return null;
    }

    public static boolean checkUsernameAvailable (String username)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Login");
        query.whereEqualTo("UserName", username);

        List<ParseObject> results = null;
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return results.size() == 0;
    }

    public static int getIdByUsername(String username)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Login");
        query.whereEqualTo("UserName", username);

        List<ParseObject> results = null;
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(results.get(0) == null) return -1;

        return results.get(0).getInt("Id");
    }
    public static String getTypeByUsername(String username)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Login");
        query.whereEqualTo("UserName", username);

        List<ParseObject> results = null;
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return results.get(0).getString("LoginType");
    }



    //------------------------------------------------------------------------------------

    public static void signUpTutor(String userName, String name, int passwordHash)
    {
        if(!checkUsernameAvailable(userName))
        {
            Log.e("DATABASE_ERROR", "username already exists");
            return;
        }
        int tutorId = getLastId(LoginType.Tutor);

        ParseObject obj = new ParseObject("Tutor");
        obj.put("Name", name);
        obj.put("TutorId", tutorId);
        obj.saveInBackground();

        signUpInformation(userName, passwordHash, tutorId, LoginType.Tutor);

        increaseLastId(LoginType.Tutor);
    }
    public static void signUpStudent(String userName, String name, int passwordHash)
    {
        if(!checkUsernameAvailable(userName))
        {
            Log.e("DATABASE_ERROR", "username already exists");
            return;
        }
        int studentId = getLastId(LoginType.Student);

        ParseObject obj = new ParseObject("Student");
        obj.put("StudentName", name);
        obj.put("StudentId", studentId);
        obj.put("TutorId", -1);
        obj.saveInBackground();

        signUpInformation(userName, passwordHash, studentId, LoginType.Student);

        increaseLastId(LoginType.Student);
    }
    public static void setStudentTutor (int studentId, int tutorId)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Student");
        query.whereEqualTo("StudentId", studentId);

        List<ParseObject> results = null;
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(results.size() > 0)
        {
            ParseObject student = results.get(0);
            student.put("TutorId", tutorId);
            student.saveInBackground();
        }
    }
    private static void signUpInformation (String userName, int passwordHash, int id, LoginType type)
    {
        if(!checkUsernameAvailable(userName))
        {
            Log.e("DATABASE_ERROR", "username already exists");
            return;
        }
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
        obj.put("CommentId", -1);
        obj.saveInBackground();
    }
    public static void saveComment(int soundId, int commentId)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Sound");
        query.whereEqualTo("SoundId", soundId);

        List<ParseObject> results = null;
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(results.size() == 0)
            return;
        ParseObject obj = results.get(0);
        obj.put("CommentId", commentId);
        obj.saveInBackground();
    }
    public static void addRatingName (int tutorId, String ratingName)
    {
        if(!checkRatingName(tutorId, ratingName))
            return;;
        ParseObject obj = new ParseObject("Rating");
        obj.put("TutorId", tutorId);
        obj.put("RatingName", ratingName);
        obj.saveInBackground();
    }
    public static void removeRatingName (int tutorId, String ratingName)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Rating");
        query.whereEqualTo("TutorId", tutorId).whereEqualTo("RatingName", ratingName);

        List<ParseObject> results = null;
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(results.size() == 0)
            return;
        try {
            results.get(0).delete();
        } catch (ParseException e) {
            Log.e("DATABASE_ERROR", "error deleting rating");
        }
    }
    public static String[] getRatingNames (int tutorId)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Rating");
        query.whereEqualTo("TutorId", tutorId);

        List<ParseObject> results = null;
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] ratingNames = new String[results.size()];
        for(int i = 0; i < results.size(); i ++)
        {
            ratingNames[i] = results.get(i).getString("RatingName");
        }
        return ratingNames;
    }

    public static ParseObject getFirstRequestFromStudent (int studentId)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Request");
        query.whereEqualTo("StudentId", studentId);

        List<ParseObject> results = null;
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(results.size() > 0)
            return results.get(0);
        else
            return null;
    }
    public static boolean checkRatingName (int tutorId, String name)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Rating");
        query.whereEqualTo("TutorId", tutorId);

        List<ParseObject> results = null;
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(results.size() > 15)
        {
            return false;
        }
        for (ParseObject obj : results)
        {
            if(obj.getString("RatingName").compareTo(name) == 0)
                return false;
        }
        return true;
    }
    public static void requestFriendship (int tutorId, int studentId)
    {
        ParseObject result = getRequest(tutorId, studentId);

        if(result != null)
        {
            //already exists!!! don't do anything
            return;
        }

        ParseObject obj = new ParseObject("Request");
        obj.put("TutorId", tutorId);
        obj.put("StudentId", studentId);
        obj.saveInBackground();
}
    public static boolean requestFromUsername (int tutorId, String username)
    {
        ParseObject student = getInformationByUsername(username);

        if(student == null || student.getClassName().compareTo("Student") != 0)
        {
            Log.e("DATABASE_ERROR", "not a student");
            return false;
        }
        else
        {
            requestFriendship(tutorId, student.getInt("StudentId"));
            return true;
        }
    }

    public static void createDefaultRatingNames (int tutorId)
    {
        String[] names = {"Afinação", "Impostação", "Respiração", "Dicção", "Projeção", "Apoio", "Cobertura"};
        for(String ratingName : names)
        {
            addRatingName(tutorId, ratingName);
        }
    }
    public static void deleteRequest (ParseObject request)
    {
        if(request.getClassName().compareTo("Request") == 0)
        {
            try {
                request.delete();
            } catch (ParseException e) {
                Log.e("DATABASE_ERROR", "error deleting object");
            }
        }
    }

    //---------------------------Help functions------------------
    public static LoginType checkObjectType (ParseObject object)
    {
        String objName = object.getClassName();
        if(objName.compareTo("Tutor") == 0)
        {
            return LoginType.Tutor;
        }
        else if(objName.compareTo("Student") == 0)
        {
            return LoginType.Student;
        }
        else
            return null;
    }

    public static int getIdFromObject (ParseObject object, LoginType type)
    {
        if(type == LoginType.Tutor && object.getClassName().compareTo("Tutor") == 0)
        {
            return object.getInt("TutorId");
        }
        else if (type == LoginType.Student && object.getClassName().compareTo("Student") == 0)
        {
            return object.getInt("StudentId");
        }
        return -1;
    }

    public static int getTutorIdFromStudentObject (ParseObject student)
    {
        return student.getInt("TutorId");
    }

    public static ParseObject getTutorObjectFromRequest (ParseObject request)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Tutor");
        query.whereEqualTo("TutorId", request.getInt("TutorId"));

        List<ParseObject> results = null;
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(results.size() > 0)
            return results.get(0);
        else
            return null;
    }
    public static String getTutorNameFromObject (ParseObject tutor)
    {
        return tutor.getString("Name");
    }
    public static int getTutorIdFromObject (ParseObject tutor)
    {
        return tutor.getInt("TutorId");
    }

    //-------------------

    public static void initializeParse(Context context)
    {

        Parse.enableLocalDatastore(context);

        Parse.initialize(context);
    }


    public static void unsetTutor(int studentId)
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Student");
        query.whereEqualTo("StudentId", studentId);

        List<ParseObject> results = null;
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(results.size() > 0)
        {
            ParseObject student = results.get(0);
            student.put("TutorId", -1);
            student.saveInBackground();
        }
    }
    public static void invalidateAllComments()
    {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Sound");

        List<ParseObject> results = null;
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(ParseObject sound : results)
        {
            sound.put("CommentId", -1);
            try {
                sound.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static void eraseComment (ParseObject sound)
    {
        sound.put("CommentId", -1);
        sound.saveInBackground();
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
