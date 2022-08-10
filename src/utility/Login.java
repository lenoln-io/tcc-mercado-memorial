package utility;

public class Login {

    private static long id_login;
    
    public static void setLogin(long id){
        id_login = id;
    }
    
    public static long getLogin(){
        return id_login;
    }
    
    public static void logout(){
        id_login = 0;
    }
    
}
