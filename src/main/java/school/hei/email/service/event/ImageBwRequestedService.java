package school.hei.email.service.event;

import static java.io.File.createTempFile;
import static javax.imageio.ImageIO.read;
import static javax.imageio.ImageIO.write;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import school.hei.email.endpoint.event.model.ImageBwRequested;
import school.hei.email.file.bucket.BucketComponent;
import school.hei.email.repository.ImageRepository;
import school.hei.email.repository.model.JImage;

@Service
@AllArgsConstructor
public class ImageBwRequestedService implements Consumer<ImageBwRequested> {
  private final BucketComponent bucketComponent;
  private final ImageRepository imageRepository;

  @SneakyThrows
  @Override
  public void accept(ImageBwRequested event) {
    File originalFile = bucketComponent.download(event.getBucketKeyOriginal());

    BufferedImage originalImage = read(originalFile);
    ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
    BufferedImage bwImage = op.filter(originalImage, null);

    String extension =
        event.getBucketKeyOriginal().substring(event.getBucketKeyOriginal().lastIndexOf('.') + 1);
    File bwFile = createTempFile("bw-", "." + extension);
    write(bwImage, extension, bwFile);

    String bucketKeyBw = "bw/" + event.getImageId() + "." + extension;
    bucketComponent.upload(bwFile, bucketKeyBw);

    JImage image = imageRepository.findById(event.getImageId()).orElseThrow();
    image.setBucketKeyBw(bucketKeyBw);
    imageRepository.save(image);
  }
}
