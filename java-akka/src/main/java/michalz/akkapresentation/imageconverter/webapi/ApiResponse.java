package michalz.akkapresentation.imageconverter.webapi;

import lombok.Getter;
import michalz.akkapresentation.imageconverter.domain.JobStatus;

/**
 * Created by michal on 11.03.15.
 */
@Getter
public class ApiResponse {

    public ApiResponse(JobStatus type, String jobId) {
        this.type = type;
        this.jobId = jobId;
    }

    private JobStatus type;
    private String jobId;

    public static ApiResponse jobStarted(String jobId) {
        return new ApiResponse(JobStatus.JOB_STARTED, jobId);
    }
}
