package michalz.akkapresentation.imageconverter.domain.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import michalz.akkapresentation.imageconverter.domain.JobStatus;

/**
 * Created by michal on 12.03.15.
 */
@Getter
public class JobStatusResp {
    private final String jobId;
    private final String imageId;
    private final JobStatus jobStatus;

    private JobStatusResp(String jobId, String imageId, JobStatus jobStatus) {
        this.jobId = jobId;
        this.imageId = imageId;
        this.jobStatus = jobStatus;
    }

    public static JobStatusResp started(String jobId) {
        return new JobStatusResp(jobId, null, JobStatus.JOB_STARTED);
    }

    public static JobStatusResp failed(String jobId) {
        return new JobStatusResp(jobId, null, JobStatus.JOB_FAILED);
    }

    public static JobStatusResp finished(String jobId, String imageId) {
        return new JobStatusResp(jobId, imageId, JobStatus.JOB_COMPLETED);
    }

}
