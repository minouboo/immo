package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import studi.immo.entity.ChatRoom;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository <ChatRoom, Long>{

    @Query (value = "select * from chat_room cr inner join chat_room_user cru on cr.id =cru.chat_room_id inner join accommodation a on cr.accommodation_id = a.id inner join advertisement a2 on a2.accommodation_id = a.id where cru.user_id = :usertenantid" , nativeQuery = true)
    List<ChatRoom> getAllChatRoomByUserTenantId (@Param("usertenantid") Long userTenantId);


}
