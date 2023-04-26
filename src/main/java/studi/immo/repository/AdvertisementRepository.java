package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import studi.immo.entity.Advertisement;

import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository <Advertisement, Long> {

    @Query (value = "select a from Advertisement a inner join a.accommodation")
    List<Advertisement> getAllAdvertisementAccommodation ();

    @Query (value = "select * from advertisement ad join accommodation ac on ad.accommodation_id = ac.id join users u on ac.user_id = u.id where ad.id = :advertisementid" , nativeQuery = true)
/*
    (value = "select a from Advertisement a inner join a.accommodation where a.id = :advertisementid")
*/
    Advertisement getAdvertisementAccommodationById (@Param("advertisementid") Long advertisementId);

    @Query (value = "select * from advertisement ad join accommodation ac on ad.accommodation_id = ac.id where ac.user_id = :userid" , nativeQuery = true)
    List<Advertisement> getAdvertisementAccommodationByUserId (@Param("userid")Long userId);

    @Query (value = "select * from advertisement a join accommodation ac on a.accommodation_id = ac.id inner join address a2 ON a2.id = ac.address_id join city c on a2.city_id = c.id  where c.name like %:keyword% or ac.title like %:keyword% " , nativeQuery = true)
    List<Advertisement> searchAdvertisement (@Param("keyword")String keyword);



}
