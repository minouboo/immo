package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import studi.immo.entity.CommentInventory;

import java.util.List;

@Repository
public interface CommentInventoryRepository extends JpaRepository <CommentInventory,Long>{

    @Query(value = "select * from comment_inventory ci where apartment_inventory_id = :apartmentinventoryid", nativeQuery = true)
    List<CommentInventory> getCommentInventoryByApartmentId (@Param("apartmentinventoryid")Long ApartmentInventoryId);

}
