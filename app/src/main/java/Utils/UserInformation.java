package Utils;

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
    private String name;
    private String login;
    private int numberOfAudios;


    //-------------Sets----------
    public void SetUserName(String name)
    {
        this.name = name;
    }
    public void SetLogin (String login)
    {
        this.login = login;
    }


    public void IncreaseNumberOfAudios ()
    {
        numberOfAudios ++;
    }

    //------------Gets----------
    public String GetUserName ()
    {
        return name;
    }
    public String GetLogin ()
    {
        return login;
    }
}
