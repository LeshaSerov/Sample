package telegram.operators;

import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import telegram.domain.State;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OperatorsWhoProcessesMessages {
//
//    public static List<BaseRequest> addGroup(State.Data data) {
//        Integer id = new GroupDao().addGroup(
//                data.getUpdate().message().text(),
//                data.getConnector()
//        );
//        Boolean r = new GroupDao().addMember(
//                data.getIdThisMember(), id, 6, data.getConnector());
//
//        List<BaseRequest> list = new ArrayList<>();
//
//        if (r)
//            list.add(
//                    new SendMessage(data.getUpdate().message().chat().id(),
//                            "Добавлена группа." )
//            );
//        else
//            list.add(
//                    new SendMessage(data.getUpdate().message().chat().id(),
//                            "Ошибка при добавлении группы в базу данных." )
//            );
//
//        return list;
//    }
//
//    public static List<BaseRequest> addCategory(State.Data data) {
//        Boolean r = new FileInGroupDao().addCategory(
//                data.getData().getIdGroup(),
//                data.getUpdate().message().text(),
//                data.getConnector()
//        );
//
//        List<BaseRequest> list = new ArrayList<>();
//
//        if (r)
//            list.add(
//                    new SendMessage(data.getUpdate().message().chat().id(),
//                            "Добавлена категория." )
//            );
//        else
//            list.add(
//                    new SendMessage(data.getUpdate().message().chat().id(),
//                            "Ошибка при добавлении категории в базу данных." )
//            );
//
//        return list;
//    }
//
//    public static List<BaseRequest> editCategory(State.Data data) {
//        Boolean r = new FileInGroupDao().editCategory(
//                data.getData().getIdGroup(),
//                data.getUpdate().message().text(),
//                data.getConnector()
//        );
//        List<BaseRequest> list = new ArrayList<>();
//
//        if (r)
//            list.add(
//                    new SendMessage(data.getUpdate().message().chat().id(),
//                            "Изменена категория.")
//            );
//        else
//            list.add(
//                    new SendMessage(data.getUpdate().message().chat().id(),
//                            "Ошибка при изменении категории в базе данных.")
//            );
//        return list;
//    }
//
//    public static List<BaseRequest> addFile(State.Data data) {
//        SendResponse response = data.getBot().execute(new ForwardMessage(
//                new FileInGroupDao().getIdResourceChat(data.getConnector()),
//                data.getUpdate().message().chat().id(),
//                data.getUpdate().message().messageId()
//        ));
//        Boolean r1 = response != null;
//        Boolean r2;
//
//        if (data.getUpdate().message().text() != null) {
//            r2 = new FileInGroupDao().addFile(
//                    response.message().messageId(),
//                    data.getData().getIdCategory(),
//                    data.getUpdate().message().text(),
//                    data.getConnector()
//            );
//        }
//        else {
//            r2 = new FileInGroupDao().addFile(
//                    response.message().messageId(),
//                    data.getData().getIdCategory(),
//                    data.getUpdate().message().caption(),
//                    data.getConnector()
//            );
//        }
//
//        List<BaseRequest> list = new ArrayList<>();
//        if (r1 && r2)
//            list.add(
//                new SendMessage(data.getUpdate().message().chat().id(),
//                        "Добавлен Файл.")
//            );
//        else if (r2)
//            list.add(
//                    new SendMessage(data.getUpdate().message().chat().id(),
//                            "Ошибка при отправке файла в хранилище.")
//            );
//        else
//            list.add(
//                    new SendMessage(data.getUpdate().message().chat().id(),
//                            "Ошибка при добавлении файла в базу данных.")
//            );
//        return list;
//    }
//

}
