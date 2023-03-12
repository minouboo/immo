package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import studi.immo.entity.Photo;

import java.util.List;

public interface PhotoRepository extends JpaRepository <Photo, Long>{

    @Query (value = "select * from photo p where advertisement_id = :advertisementid" ,nativeQuery = true)
    List<Photo> getPhotosByAdvertisementId (@Param("advertisementid")Long advertismentId);

    @Query(value = "select * from photo p where main_photos = true and advertisement_id = :advertisementid", nativeQuery = true)
    Photo getMainPhotoByAdvertisementId (@Param("advertisementid")Long advertisementId);

}
