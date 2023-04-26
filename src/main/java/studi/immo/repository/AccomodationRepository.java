package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import studi.immo.entity.Accommodation;

import java.util.List;

@Repository
public interface AccomodationRepository extends JpaRepository <Accommodation, Long> {

    @Query(value = "select * from accommodation a inner join users u on a.user_id = u.id where a.id = :accommodationid", nativeQuery = true)
    Accommodation getAccommodationAndUserById (@Param("accommodationid")Long accommodationId);

    @Query (value = "select * from accommodation a where a.user_id = :userid", nativeQuery = true)
    List<Accommodation> getAccommodationByUserId (@Param("userid")Long userId);

    @Query (value = "select * from accommodation a join address a2 on a2.id = a.address_id join city c on c.id = a2.city_id join users u on a.user_id = u.id where c.name like %:searchword% or u.user_name like %:searchword%", nativeQuery = true)
    List<Accommodation> searchAccommodation (@Param("searchword")String searchword);

}
