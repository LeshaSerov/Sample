package telegram.Events;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.ChatMember;
import com.pengrad.telegrambot.model.ChatMemberUpdated;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

import static java.lang.Long.parseLong;

public class HandlerEventsModifyRightsBot {
    public static BaseRequest process(ChatMemberUpdated myChatMember) {
        SendMessage request = null;

        Chat chat = myChatMember.chat();
        Long chat_id = chat.id();

        String answer = null;

        ChatMember.Status status = myChatMember.newChatMember().status();
        ChatMember.Status statusKick = ChatMember.Status.kicked;
        ChatMember.Status statusMember = ChatMember.Status.member;
        ChatMember.Status statusAdministrator = ChatMember.Status.administrator;

        if (status == statusKick) {
            DeleteChat(chat);
        } else if (status == statusMember) {
            answer = """
                    Привет)
                    Пожалуйста, выдай мне админские права,
                    иначе я не смогу работать в этом чате.""";
            request = new SendMessage(chat_id, answer);
        } else if (status == statusAdministrator) {
            AddChat(chat);
            answer = """
                    Привет)
                    Я Помощник.
                    Админские права обнаружил,
                    Приступаю к своей работе""";
            request = new SendMessage(chat_id, answer);
        }
        return request;
    }


    //Эти методы добавляли данные о чатах в бд
    private static void AddChat(Chat chat) {
        try {
            Long chat_id = parseLong(chat.id().toString());
            //new ChatDao().addChat(chat_id, chat.title(), connector);
        } catch (Exception ignored) {
        }
    }

    private static void DeleteChat(Chat chat) {
        try {
            //new ChatDao().deleteChat(chat.id(), connector);
        } catch (Exception ignored) {
        }
    }

}
