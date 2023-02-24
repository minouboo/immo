package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import studi.immo.entity.ChatRoom;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository <ChatRoom, Long>{

    @Query (value = "select * from chat_room cr join accommodation a on cr.accommodation_id = a.id join advertisement a2 on a2.accommodation_id = a.id  where user_tenant_id = :usertenantid" , nativeQuery = true)
    List<ChatRoom> getChatRoomByUserTenantId (@Param("usertenantid") Long userTenantId);

}
