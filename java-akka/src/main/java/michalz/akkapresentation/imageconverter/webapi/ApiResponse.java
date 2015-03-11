package michalz.akkapresentation.imageconverter.webapi;

import lombok.Getter;

/**
 * Created by michal on 11.03.15.
 */
@Getter
public class ApiResponse {
    public enum Type {
        JOB_STARTED, JOB_FAILED, JOB_COMPLETED
    }

    public ApiResponse(Type type, String jobId) {
        this.type = type;
        this.jobId = jobId;
    }

    private Type type;
    private String jobId;

    public static ApiResponse jobStarted(String jobId) {
        return new ApiResponse(Type.JOB_STARTED, jobId);
    }
}
