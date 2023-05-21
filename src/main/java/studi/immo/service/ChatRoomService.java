package studi.immo.service;


import studi.immo.entity.ChatRoom;

import java.util.List;

public interface ChatRoomService {

    ChatRoom saveChatRoom (ChatRoom chatRoom);

    List<ChatRoom> getAllChatRoomByUserTenantId (Long userTenantId);

    ChatRoom getChatRoomById (Long id);

    void deleteChatRoombyId (Long id);
    List<ChatRoom> getAllChatRoomArchivedByUserTenantId (Long userTenantId);

    ChatRoom findByAccommodationId (Long accommodationId);


}
