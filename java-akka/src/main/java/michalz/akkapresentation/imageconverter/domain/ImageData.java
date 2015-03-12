package michalz.akkapresentation.imageconverter.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by michal on 12.03.15.
 */
@Getter
@Setter
public class ImageData {
    private String imageId;
    private String createdFrom;
    private ImageOperation resultOf;
}
