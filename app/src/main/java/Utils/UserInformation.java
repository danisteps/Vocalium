package Utils;

/**
 * Created by Délio on 13/12/2015.
 */
enum UserType
{
    Teacher,
    Student
}
public class UserInformation {
    private static UserInformation ourInstance = new UserInformation();

    public static UserInformation getInstance() {
        return ourInstance;
    }

    private UserInformation() {
    }

    //-----------------------Attributes--------------
    private String login;

    private String teacherName;
    private int teacherId;

    private String curStudentName;
    private int curStudentId;

    private int numberOfAudios;
    private UserType userType;
    //-------------Sets----------
    public void SetTeacherName(String name)
    {
        this.teacherName = name;
    }
    public void SetTeacherId(int id)
    {
        this.teacherId = id;
    }
    public void SetStudentName (String name)
    {
        this.curStudentName = name;
    }
    public void SetStudentId (int id)
    {
        this.curStudentId = id;
    }
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
    public String GetTeacherName()
    {
        return teacherName;
    }
    public int GetTeacherId()
    {
        return teacherId;
    }
    public int GetStudentId ()
    {
        return curStudentId;
    }
    public String GetStudentName ()
    {
        return curStudentName;
    }
    public UserType GetUserType () { return userType; }
}
