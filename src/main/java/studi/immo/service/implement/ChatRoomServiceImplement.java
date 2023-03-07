package studi.immo.service.implement;

import org.springframework.stereotype.Service;
import studi.immo.entity.ChatRoom;
import studi.immo.repository.ChatRoomRepository;
import studi.immo.service.ChatRoomService;

import java.util.List;

@Service
public class ChatRoomServiceImplement implements ChatRoomService {

    private ChatRoomRepository chatRoomRepository;

    public ChatRoomServiceImplement (ChatRoomRepository chatRoomRepository){
        super ();
        this.chatRoomRepository = chatRoomRepository;
    }

    @Override
    public ChatRoom saveChatRoom(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public List<ChatRoom> getAllChatRoomByUserTenantId(Long userTenantId) {
        return chatRoomRepository.getAllChatRoomByUserTenantId(userTenantId);
    }

    @Override
    public ChatRoom getChatRoomById(Long id) {
        return chatRoomRepository.findById(id).get();
    }

    @Override
    public void deleteChatRoombyId(Long id) {
         chatRoomRepository.deleteById(id);
    }


}
