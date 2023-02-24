package studi.immo.service;


import studi.immo.entity.ChatRoom;

import java.util.List;

public interface ChatRoomService {

    ChatRoom saveChatRoom (ChatRoom chatRoom);

    List<ChatRoom> getChatRoomByUserTenantId (Long userTenantId);

}
