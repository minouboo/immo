package studi.immo.service;

import studi.immo.entity.Message;

import java.util.List;

public interface MessageService {

    Message saveMessage (Message message);

    List<Message> getMessageByAccommodationAndUser(Long userId, Long accommodationId);

    List<Message> getMessageByChatRoomId (Long chatRoomId);

}
