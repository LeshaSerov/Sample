package telegram.Events;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.Arrays;

public class HandlerEventsInGroups {
    public static BaseRequest process(Message message) {
    BaseRequest request = null;
    Long chat_id = message.chat().id();
    User[] updateNewUser = message.newChatMembers();
    User updateLeftUser = message.leftChatMember();

    if (updateNewUser != null && Arrays.stream(updateNewUser).noneMatch(User::isBot)) {
        AddMemberInChat(chat_id, Arrays.stream(updateNewUser).findFirst().orElse(null));
        request = new SendMessage(chat_id, "Пользователь пришел в чат.");
    }
    else if (updateLeftUser != null) {
        DeleteMemberInChat(chat_id, updateLeftUser.id());
        request = new SendMessage(chat_id, "Пользователь вышел из чата.");
    }
    else {
        AddMemberInChat(chat_id, new User(message.from().id()));
    }

    return request;
}

    private static void AddMemberInChat(Long chat_id, User user) {
        try {
//            Добавление пользователя в чат в бд
//            new MemberDao().addMember(user.id(), user.firstName(), user.lastName(), user.username(), connector);
//            new ChatDao().addMember(user.id(), chat_id, connector);
        } catch (Exception ignored) {
        }
    }

    private static void DeleteMemberInChat(Long chat_id, Long id) {
        try {
            //Удаление пользователя из чата в бд
            //new ChatDao().deleteMember(id, chat_id, connector);
        } catch (Exception ignored) {
        }
    }
}
