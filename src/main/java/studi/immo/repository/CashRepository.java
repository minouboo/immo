package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import studi.immo.entity.Cash;

@Repository
public interface CashRepository extends JpaRepository <Cash, Long> {

    @Query (value = "select * from cash c join users u on c.user_id = u.id where u.id = :userid", nativeQuery = true)
    Cash getCashByUserId (@Param("userid")Long userId);

}
