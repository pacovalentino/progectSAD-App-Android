package response;

public class LoginResponse {
    private String login;
    private String email;

    public LoginResponse(String login,String email){
        this.login=login;
        this.email=email;
    }

    public String getLogin(){
        return login;
    }

    public String getEmail(){
        return email;
    }
}
