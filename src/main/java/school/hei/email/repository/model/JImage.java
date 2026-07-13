package school.hei.email.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "image")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class JImage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nomFichier;
  private String email;
  private String bucketKeyBw;

  @CreationTimestamp private Instant createdAt;
}
