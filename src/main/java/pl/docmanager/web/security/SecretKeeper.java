package pl.docmanager.web.security;

public class SecretKeeper {
    private static SecretKeeper instance;

    private SecretKeeper() { }

    public synchronized static SecretKeeper getInstance() {
        if (instance == null) {
            instance = new SecretKeeper();
        }
        return instance;
    }

    public String getSecret() {
        return "SecretKeyToGenJWTs";
    }
}
