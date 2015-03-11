package michalz.akkapresentation.imageconverter.domain.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by michal on 11.03.15.
 */
@Getter
@AllArgsConstructor
public class StoreImage {
    public byte[] imageData;
}
