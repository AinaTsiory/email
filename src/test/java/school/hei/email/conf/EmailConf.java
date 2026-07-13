package school.hei.email.conf;

import org.springframework.test.context.DynamicPropertyRegistry;
import school.hei.email.PojaGenerated;

@PojaGenerated
public class EmailConf {

  void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("aws.ses.source", () -> "dummy-ses-source");
  }
}
