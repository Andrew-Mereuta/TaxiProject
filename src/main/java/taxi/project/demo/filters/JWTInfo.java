package taxi.project.demo.filters;

public class JWTInfo {

    private String secretKey = "VerySecretVerySecretVerySecretVerySecretVerySecretVerySecretVerySecretVerySecretVerySecretVerySecretVerySecretVerySecretVerySecret";
    private String bearer = "Bearer";

    public JWTInfo() {
    }


    public String getSecretKey() {
        return secretKey;
    }

    public String getBearer() {
        return bearer;
    }
}
