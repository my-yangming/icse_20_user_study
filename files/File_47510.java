package com.us.example.controller;



import com.us.example.bean.Message;
import com.us.example.bean.Response;
import com.us.example.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

/**
 * Created by yangyibo on 16/12/29.
 *
 */
@CrossOrigin
@Controller
public class WebSocketController {
    @Autowired
    private WebSocketService ws;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @RequestMapping(value = "/login")
    public String login(){
        return  "login";
    }
    @RequestMapping(value = "/ws")
    public String ws(){
        return  "ws";
    }
    @RequestMapping(value = "/chat")
    public String chat(){
        return  "chat";
    }
    //http://localhost:8080/ws
    @MessageMapping("/welcome")//�?览器�?��?请求通过@messageMapping 映射/welcome 这个地�?�。
    @SendTo("/topic/getResponse")//�?务器端有消�?�时,会订阅@SendTo 中的路径的�?览器�?��?消�?�。
    public Response say(Message message) throws Exception {
        Thread.sleep(1000);
        return new Response("Welcome, " + message.getName() + "!");
    }

    //http://localhost:8080/Welcome1
    @RequestMapping("/Welcome1")
    @ResponseBody
    public String say2()throws Exception
    {
        ws.sendMessage();
        return "is ok";
    }

    @MessageMapping("/chat")
    //在springmvc 中�?�以直接获得principal,principal 中包�?�当�?用户的信�?�
    public void handleChat(Principal principal, Message message) {

        /**
         * 此处是一段硬编�?。如果�?��?人是wyf 则�?��?给 wisely 如果�?��?人是wisely 就�?��?给 wyf。
         * 通过当�?用户,然�?�查找消�?�,如果查找到未读消�?�,则�?��?给当�?用户。
         */
        if (principal.getName().equals("admin")) {
            //通过convertAndSendToUser �?�用户�?��?信�?�,
            // 第一个�?�数是接收消�?�的用户,第二个�?�数是�?览器订阅的地�?�,第三个�?�数是消�?�本身

            messagingTemplate.convertAndSendToUser("abel",
                    "/queue/notifications", principal.getName() + "-send:"
                            + message.getName());
            /**
             * 72 行�?作相等于 
             * messagingTemplate.convertAndSend("/user/abel/queue/notifications",principal.getName() + "-send:"
             + message.getName());
             */
        } else {
            messagingTemplate.convertAndSendToUser("admin",
                    "/queue/notifications", principal.getName() + "-send:"
                            + message.getName());
        }
    }
}
