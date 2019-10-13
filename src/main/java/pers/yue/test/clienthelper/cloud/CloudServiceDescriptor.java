package pers.yue.test.clienthelper.cloud;

/**
 * Created by Zhang Yue on 5/15/2019
 */
public class CloudServiceDescriptor {
    private String endpoint;
    private String region;
    private String pin;
    private String userId;
    private String userName;
    private String accessKeyId;
    private String secretAccessKey;

    public CloudServiceDescriptor(String endpoint, String region, String pin, String userId,
                                  String userName, String accessKeyId, String secretAccessKey) {
        this.endpoint = endpoint;
        this.region = region;
        this.pin = pin;
        this.userId = userId;
        this.userName = userName;
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getRegion() {
        return region;
    }

    public String getPin() {
        return pin;
    }

    public String getUserId() {
        return userId;
    }

    public String getUser() {
        if(userName != null && !userName.isEmpty()) {
            return pin + " - " + userName;
        }
        return pin;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }
}
