package com.example.moyiza_be.chat.controller;

import com.example.moyiza_be.chat.dto.ChatMessageInput;
import com.example.moyiza_be.chat.dto.ChatMessageOutput;
import com.example.moyiza_be.chat.dto.ChatRoomInfo;
import com.example.moyiza_be.chat.dto.ChatUserPrincipal;
import com.example.moyiza_be.chat.service.ChatService;
import com.example.moyiza_be.common.redis.RedisCacheService;
import com.example.moyiza_be.common.security.jwt.JwtUtil;
import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final JwtUtil jwtUtil;
    private final RedisCacheService redisCacheService;


    //채팅 메시지 전송, 수신
    @MessageMapping("/chat/{chatId}")
    public void receiveAndSendChat(
            @DestinationVariable Long chatId, ChatMessageInput chatMessageInput,
            Message<?> message
     ) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        String sessionId = headerAccessor.getSessionId();
        if(StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())){
            System.out.println("\"SUBSCRIBE message comming through to controller\" = " + "SUBSCRIBE message comming through to controller");
            System.out.println("headerAccessor = " + headerAccessor);
        }
        ChatUserPrincipal userInfo = redisCacheService.getUserInfoFromCache(sessionId);
        chatService.receiveAndSendChat(userInfo, chatId, chatMessageInput);
    }

    //채팅방 목록 조회
    @GetMapping("/chat")
    public ResponseEntity<List<ChatRoomInfo>> getChatRoomList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null){
            throw new NullPointerException("사용자 정보가 없습니다");
        }
        return chatService.getChatRoomList(userDetails.getUser());
    }

    //채팅 내역 조회
    @GetMapping("/chat/{chatId}")
    public ResponseEntity<Page<ChatMessageOutput>> getChatRecordList(
            @PageableDefault(page = 0, size = 50, sort = "CreatedAt", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable Long chatId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return chatService.getChatRoomRecord(userDetails.getUser(), chatId, pageable);
    }

}
