package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import studi.immo.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUserName (String username);

    @Query(value = "select * from users u where u.last_name like %:keyword% or u.user_name like %:keyword% or u.first_name like %:keyword% or u.email like %:keyword%", nativeQuery = true)
    List<User> searchUser (@Param("keyword") String keyword);

}
