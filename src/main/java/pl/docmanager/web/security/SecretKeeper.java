package pl.docmanager.web.security;

public class SecretKeeper {
    public static String getSecret() {
        return "SecretKeyToGenJWTs";
    }
}
