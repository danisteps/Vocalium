package Utils;

import android.util.Log;

import com.parse.ParseObject;

/**
 * Created by Délio on 13/12/2015.
 */

public class UserInformation {
    private static UserInformation ourInstance = new UserInformation();

    public static UserInformation getInstance() {
        return ourInstance;
    }

    private UserInformation() {
    }

    //-----------------------Attributes--------------
    private String login;

    private String tutorName = "";
    private int tutorId = -1;

    private String curStudentName = "";
    private int curStudentId = -1;

    private int audioId = -1;
    private int numberOfAudios;
    private UserType userType;

    //-------------Sets----------
    public void SetTutorName(String name)
    {
        this.tutorName = name;
    }
    public void SetTutorId(int id)
    {
        this.tutorId = id;
    }
    public void SetStudentName (String name)
    {
        this.curStudentName = name;
    }
    public void SetStudentId (int id)
    {
        this.curStudentId = id;
    }
    public void SetAudioId (int id) {audioId = id;}
    public void SetLogin (String login)
    {
        this.login = login;
    }
    public void SetUserType (UserType user) {userType = user;}

    public void IncreaseNumberOfAudios ()
    {
        numberOfAudios ++;
    }

    //------------Gets----------
    public String GetLogin ()
    {
        return login;
    }
    public String GetTutorName()
    {
        return tutorName;
    }
    public int GetTutorId()
    {
        return tutorId;
    }
    public int GetStudentId ()
    {
        return curStudentId;
    }
    public String GetStudentName ()
    {
        return curStudentName;
    }
    public int GetAudioId () { return audioId; }
    public UserType GetUserType () { return userType; }

    //---------------methods-----------------
    public void populateStudentInformation(ParseObject object, String tutorName)
    {
        SetUserType(UserType.Student);
        SetTutorName(tutorName);
        SetTutorId(object.getInt("TutorId"));
        SetStudentId(object.getInt("StudentId"));
        SetStudentName(object.getString("StudentName"));

        Log.e("DATABASE_ERROR", "login data student: " + this.tutorName + " " + tutorId +" / " +  curStudentName + " " + curStudentId);
    }
    public void populateTutorInformation(ParseObject object) {

        SetUserType(UserType.Tutor);
        SetTutorId(object.getInt("TutorId"));
        SetTutorName(object.getString("Name"));

        Log.e("DATABASE_ERROR", "login data tutor: " +tutorName + " " + tutorId);
    }
    public void populateTutorInformationForStudent(ParseObject object) {

        SetTutorId(object.getInt("TutorId"));
        SetTutorName(object.getString("Name"));

        Log.e("DATABASE_ERROR", "populated data tutor: " +tutorName + " " + tutorId);
    }

    public enum UserType
    {
        Tutor,
        Student
    }

    public void debugUser()
    {
        Log.e("USER_ERROR", ""+ tutorId);
        Log.e("USER_ERROR", ""+ tutorName);
        Log.e("USER_ERROR", ""+ curStudentId);
        Log.e("USER_ERROR", ""+ curStudentName);
    }
}
