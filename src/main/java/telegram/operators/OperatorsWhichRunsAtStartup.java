package telegram.operators;

import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import telegram.domain.State;

import java.util.ArrayList;
import java.util.List;

public class OperatorsWhichRunsAtStartup {
//
//    public static List<BaseRequest> deleteMember(State.Data data) {
//        Boolean r = new GroupDao().deleteMember(data.getData().getIdMember(), data.getData().getIdGroup(), data.getConnector());
//
//        List<BaseRequest> list = new ArrayList<>();
//
//        if (r)
//            list.add(
//                new AnswerCallbackQuery(data.getUpdate().callbackQuery().id())
//                        .text("Пользователь удален.").showAlert(true)
//            );
//        else
//            list.add(
//                    new AnswerCallbackQuery(data.getUpdate().callbackQuery().id())
//                            .text("Ошибка при удалении пользователя из базы данных.").showAlert(true)
//            );
//        return list;
//    }
//
//    public static List<BaseRequest> changeRole(State.Data data) {
//        Boolean r = new GroupDao().editMember(data.getIdThisMember(),
//                data.getData().getIdGroup(),
//                data.getData().getIdRole(), data.getConnector());
//
//        List<BaseRequest> list = new ArrayList<>();
//
//        if (r)
//            list.add(
//                new AnswerCallbackQuery(data.getUpdate().callbackQuery().id())
//                        .text("Роль пользователя изменена.").showAlert(true)
//            );
//        else
//            list.add(
//                    new AnswerCallbackQuery(data.getUpdate().callbackQuery().id())
//                            .text("Ошибка при изменении роли пользователя в базе данных.").showAlert(true)
//            );
//
//        return list;
//    }
//
//    public static List<BaseRequest> addMember(State.Data data) {
//        Boolean r = new GroupDao().addMember(data.getData().getIdMember(),
//                data.getData().getIdGroup(),
//                1,
//                data.getConnector());
//
//        List<BaseRequest> list = new ArrayList<>();
//
//        if (r)
//            list.add(
//                    new AnswerCallbackQuery(data.getUpdate().callbackQuery().id())
//                            .text("Пользователь добавлен.").showAlert(true)
//            );
//        else
//            list.add(
//                    new AnswerCallbackQuery(data.getUpdate().callbackQuery().id())
//                            .text("Ошибка при добавлении пользователя в группу в базе данных.").showAlert(true)
//            );
//
//        return list;
//    }
//
//    public static List<BaseRequest> deleteCategory(State.Data data) {
//        Boolean r = new FileInGroupDao()
//                .deleteCategory(data.getData().getIdCategory(), data.getConnector());
//
//        List<BaseRequest> list = new ArrayList<>();
//
//        if (r)
//            list.add(
//                    new AnswerCallbackQuery(data.getUpdate().callbackQuery().id())
//                            .text("Категория удалена.").showAlert(true)
//            );
//        else
//            list.add(
//                    new AnswerCallbackQuery(data.getUpdate().callbackQuery().id())
//                            .text("Ошибка при удалении категории в базе данных.").showAlert(true)
//            );
//
//        return list;
//    }
}