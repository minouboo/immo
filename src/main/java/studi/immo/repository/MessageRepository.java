package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import studi.immo.entity.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository <Message, Long> {

    @Query (value = "select * from message m where m.user_from_id = :userid and  m.accommodation_id = :accommodationid", nativeQuery = true)
    List<Message> getMessageByAccommodationAndUser(@Param("userid") Long userId,@Param("accommodationid")Long accommodationId);

    @Query (value = "select * from message m where chat_room_id = :chatroomid" , nativeQuery = true)
    List<Message> getMessageByChatRoomId(@Param("chatroomid")Long chatRoomId);
}
