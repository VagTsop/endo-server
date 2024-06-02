//package org.endofusion.endoserver.controller;
//
//import org.endofusion.endoserver.chat.Message;
//import org.springframework.messaging.converter.MessageConversionException;
//import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class ChatController {
//
//    @MessageMapping("/greet")
//    @SendTo("/topic/greetings")
//    @MessageExceptionHandler(MessageConversionException.class)
//    public Message send(@Payload Message message) {
//        System.out.println(message);
//        return message;
//    }
//}