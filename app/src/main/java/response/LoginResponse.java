package response;

public class LoginResponse {
    private String login;
    private String token;

    public LoginResponse(String login,String email){
        this.login=login;
        this.token=token;
    }

    public String getLogin(){
        return login;
    }

    public String getToken(){
        return token;
    }
}
