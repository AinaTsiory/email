package school.hei.email.endpoint.rest.controller;

import java.io.File;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import school.hei.email.endpoint.event.EventProducer;
import school.hei.email.endpoint.event.model.ImageBwRequested;
import school.hei.email.file.bucket.BucketComponent;
import school.hei.email.repository.ImageRepository;
import school.hei.email.repository.model.JImage;

@RestController
@AllArgsConstructor
public class ImageController {
  private final ImageRepository imageRepository;
  private final BucketComponent bucketComponent;
  private final EventProducer<ImageBwRequested> eventProducer;

  @PostMapping("/images")
  @ResponseStatus(HttpStatus.CREATED)
  @SneakyThrows
  public JImage submitImage(@RequestParam("file") MultipartFile file, @RequestParam String email) {
    String contentType = file.getContentType();
    if (contentType == null
        || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
      throw new IllegalArgumentException("Seuls les fichiers JPEG et PNG sont acceptés");
    }

    JImage image =
        imageRepository.save(
            JImage.builder().nomFichier(file.getOriginalFilename()).email(email).build());

    String extension = contentType.equals("image/png") ? "png" : "jpg";
    String bucketKeyOriginal = "original/" + image.getId() + "." + extension;
    File tempFile = File.createTempFile("upload-", "." + extension);
    file.transferTo(tempFile);
    bucketComponent.upload(tempFile, bucketKeyOriginal);

    var event =
        ImageBwRequested.builder()
            .imageId(image.getId())
            .bucketKeyOriginal(bucketKeyOriginal)
            .build();
    eventProducer.accept(List.of(event));

    return image;
  }

  @GetMapping("/images")
  public List<JImage> findAll() {
    return imageRepository.findAll();
  }
}
