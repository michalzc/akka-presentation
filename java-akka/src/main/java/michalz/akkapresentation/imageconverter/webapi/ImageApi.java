package michalz.akkapresentation.imageconverter.webapi;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import michalz.akkapresentation.imageconverter.domain.JobStatus;
import michalz.akkapresentation.imageconverter.domain.protocol.JobStartedResp;
import michalz.akkapresentation.imageconverter.domain.protocol.JobStatusResp;
import michalz.akkapresentation.imageconverter.services.AkkaService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by michal on 11.03.15.
 */
@RequestMapping("img")
@Slf4j
@RestController
public class ImageApi {

  @Autowired
  private AkkaService akkaService;

  @RequestMapping(value = "upload", method = RequestMethod.PUT)
  public Map<String, Object> uploadFile(HttpServletRequest request) throws Exception {
    byte[] bytes = IOUtils.toByteArray(request.getInputStream());
    JobStartedResp jobStartedResp = akkaService.storeImage(bytes);
    return ImmutableMap.of(
      "jobStatus", JobStatus.JOB_STARTED,
      "jobId", jobStartedResp.getJobId());
  }

  @RequestMapping(value = "status/{jobId}", method = RequestMethod.PUT)
  public Map<String, Object> jobStatus(@PathVariable String jobId) {
    JobStatusResp jobStatusResp = akkaService.getJobStatus(jobId);
    ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder()
      .put("jobId", jobStatusResp.getJobId())
      .put("jobStatus", jobStatusResp.getJobStatus());

    if (JobStatus.JOB_COMPLETED == jobStatusResp.getJobStatus()) {
      builder.put("imageDataUrl", MvcUriComponentsBuilder.fromMethodName(ImageApi.class, "getImageData",
        jobStatusResp.getImageId()).build().toUriString());
    }

    return builder.build();
  }

  @RequestMapping(value = "imageData/{imageId}", method = RequestMethod.GET)
  public Map<String, Object> getImageData(@PathVariable String imageId) {
    return null;
  }
}
