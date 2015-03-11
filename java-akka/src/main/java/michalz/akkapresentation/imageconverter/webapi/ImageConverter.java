package michalz.akkapresentation.imageconverter.webapi;

import lombok.extern.slf4j.Slf4j;
import michalz.akkapresentation.imageconverter.services.AkkaService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by michal on 11.03.15.
 */
@RequestMapping("img")
@Slf4j
@RestController
public class ImageConverter {

    @Autowired
    private AkkaService akkaService;

    @RequestMapping(value = "upload", method = RequestMethod.PUT)
    public ApiResponse uploadFile(HttpServletRequest request) throws Exception {
        byte[] bytes = IOUtils.toByteArray(request.getInputStream());
        return ApiResponse.jobStarted(akkaService.storeImage(bytes).getJobId());
    }
}
