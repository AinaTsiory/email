package school.hei.email.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.hei.email.repository.model.JImage;

public interface ImageRepository extends JpaRepository<JImage, Long> {}
