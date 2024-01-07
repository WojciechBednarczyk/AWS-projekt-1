package pl.edu.pwr.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pwr.backend.model.Move;

@Repository
public interface MoveRepository extends JpaRepository<Move,Long> {
}
