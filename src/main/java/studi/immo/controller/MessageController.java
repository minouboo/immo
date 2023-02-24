package studi.immo.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import studi.immo.entity.Accommodation;
import studi.immo.entity.ChatRoom;
import studi.immo.entity.Message;
import studi.immo.entity.User;
import studi.immo.form.ChatForm;
import studi.immo.service.AccommodationService;
import studi.immo.service.ChatRoomService;
import studi.immo.service.MessageService;
import studi.immo.service.UserService;

import java.util.List;

@Log
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "chat")
public class MessageController {

    private AccommodationService accommodationService;
    private MessageService messageService;
    private UserService userService;
    private ChatRoomService chatRoomService;

    public MessageController(AccommodationService accommodationService, MessageService messageService, UserService userService, ChatRoomService chatRoomService) {
        this.accommodationService = accommodationService;
        this.messageService = messageService;
        this.userService = userService;
        this.chatRoomService = chatRoomService;
    }


    @GetMapping(value = "/message/{id}")
    public String myMessage (@PathVariable Long id, Model model){
        Accommodation accommodation = accommodationService.getAccommodationById(id);
        List<Message> myMessages = messageService.getMessageByChatRoomId(id);
        ChatForm chatForm = new ChatForm();
        model.addAttribute("SendMessage", chatForm);
        model.addAttribute("accommodation", accommodation);
        model.addAttribute("MyMessage",myMessages);
        return "Message";

    }

    @PostMapping(value = "/envoyer-message/{id}")
    public String sendMessage (@PathVariable Long id,
                               @ModelAttribute("SendMessage") ChatForm chatForm){
        User userFrom = userService.getCurrentUser();
        Accommodation a = accommodationService.getAccommodationById(id);
        User userTo = a.getUser();
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setUserLandlord(userTo);
        chatRoom.setUserTenant(userFrom);
        chatRoom.setAccommodation(a);
        chatRoomService.saveChatRoom(chatRoom);
        Message message = new Message();
        message.setUserFrom(userFrom);
        message.setUserTo(userTo);
        message.setAccommodation(a);
        message.setMessage(chatForm.getMessage());
        message.setChatRoom(chatRoom);
        messageService.saveMessage(message);


        return "redirect:/chat/message/{id}";
    }

    @GetMapping (value = "/mes-conversations")
    public String myChatRooms (Model model){
        User user = userService.getCurrentUser();
        List<ChatRoom> myChatRoom = chatRoomService.getChatRoomByUserTenantId(user.getId());
        model.addAttribute("MyChatRoom", myChatRoom);
        return "MyChatRooms";
    }

}
