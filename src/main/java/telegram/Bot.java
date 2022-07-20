package telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.SendResponse;
import kotlin.Pair;
import telegram.Events.HandlerCommands;
import telegram.Events.HandlerEventsInGroups;
import telegram.Events.HandlerEventsModifyRightsBot;
import telegram.domain.MemberData;
import telegram.domain.State;

import java.util.*;

public class Bot {
    private final TelegramBot bot = new TelegramBot("");
    private final Map<Long, MemberData> controllerStates = new HashMap<Long, MemberData>();
    private final State stateDefault = Initiator.initializeDefaultState();

    public void start() {
        //Initiator.reset(connector);
        System.out.println("Start");
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::process);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void process(Update update) {
        try {
            System.out.println("Update");
            List<BaseRequest> requests = new ArrayList<>();
            InformantUpdate informant = definitionIdAndTypeUpdate(update);

            switch (informant.type) {
                case MyChatMember -> requests.add(HandlerEventsModifyRightsBot.process(update.myChatMember()));
                case Message -> {
                    requests.add(HandlerEventsInGroups.process(update.message()));
                    requests.add(HandlerCommands.process(update.message()));
                }
            }

            for (BaseRequest x : requests) {
                if (x != null)
                    bot.execute(x);
            }

            //Обработка Состояний
            if (Objects.equals(informant.idMember, informant.idChat)) {

                MemberData vault = controllerStates.get(informant.idMember);
                State vaultState = null;
                if (vault != null) {
                    vaultState = vault.getState();
                    //bot.execute(new SendMessage(informant.idChat, "Для тестирования - текущее состояние - " + vaultState.getName())).message();
                }

                if (informant.type == TypeUpdate.Message) {
                    Message message = update.message();

                    if (message.text() != null && message.text().startsWith("/default")) {
                        User user = message.from();

                        //new MemberDao().addMember(user.id(), user.firstName(), user.lastName(), user.username());
                        controllerStates.put(informant.idMember, new MemberData(stateDefault));
                    } else if (message.text() != null && message.text().startsWith("/exit")) {
                        controllerStates.remove(informant.idMember);

                    } else if (vaultState != null && vaultState.getOperatorWhoProcessesMessages() != null) {
                        requests.addAll(vaultState.getOperatorWhoProcessesMessages()
                                .apply(new State.Data
                                        (informant.idMember, vault, update, bot)));
                        //Переход обратно, если конечный этап.
                        if (vaultState.getStateNext() == null && vaultState.getPaths().isEmpty()) {
                            vault.setState(vaultState.previous());
                        }
                    }

                } else if (informant.type == TypeUpdate.CallbackQuery) {

                    Integer idMessage = update.callbackQuery().message().messageId();
                    String text = update.callbackQuery().data();

                    if ((vault == null) || (!Objects.equals(vault.getIdMessage(), idMessage)))
                        requests.add(new DeleteMessage(informant.idChat, update.callbackQuery().message().messageId()));
                        //Переход в следующее состояние
                    else {
                        if (Objects.equals(text, "Назад")) {
                            if (vaultState.getOperatorWhichGeneratesKeyboard() != null){
                                vault.setPreviousStateGenerateKeyboard(null);
                                vault.setNumberSublist(0);
                            }
                            vault.setState(vaultState.previous());
                        } else if (Objects.equals(text, "⬅⬅⬅")) {
                            vault.setNumberSublist(vault.getNumberSublist() - 1);
                        } else if (Objects.equals(text, "➡➡➡")) {
                            vault.setNumberSublist(vault.getNumberSublist() + 1);
                        } else if (Objects.equals(text, "Int/Int")) {
                            requests.add(new AnswerCallbackQuery(update.callbackQuery().id())
                                    .text("Делать больше нечего?))")
                                    .showAlert(true));
                        }else {
                            State stateNext = null;
                            //Сохранение данных с кнопки, которую только что нажали
                            if (vaultState.getOperatorWhichGeneratesKeyboard() != null) {
                                vault.addInfo(vaultState.getType(), text);
                                stateNext = vaultState.getStateNext();
                            } else {
                                stateNext = vaultState.getPaths()
                                        .stream()
                                        .filter(element -> Objects.equals(element.getName(), text))
                                        .findFirst().orElseThrow();
                            }

                            vault.setState(stateNext);
                            vaultState = vault.getState();
                            //bot.execute(new SendMessage(informant.idChat, "Для тестирования - текущее состояние - " + vaultState.getName()));

                            if (vaultState.getOperatorWhichRunsAtStartup() != null) {
                                //Специальный метод - запускаемый при запуске - конкретную реализацию необходимо создавать отдельно
                                requests.addAll(vaultState.getOperatorWhichRunsAtStartup()
                                        .apply(new State.Data
                                                (informant.idMember, vault, update, bot)));
                                //Переход обратно, если конечный этап.
                                if (vaultState.getStateNext() == null && vaultState.getPaths().isEmpty()) {
                                    vault.setState(vaultState.previous());
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("Неизвестный Update");
                }
                //TODO: Удалить эту часть кода - но не понятно может и работает
            } else if (informant.type == TypeUpdate.Message) {
                //Добавление чата и пользователя в бд
                User user = update.message().from();
                //new MemberDao().addMember(user.id(), user.firstName(), user.lastName(), user.username(), connector);
                //new ChatDao().addChat(update.message().chat().id(), update.message().chat().title(), connector);
            }

            //Генерация Сообщений С клавиатурами
            MemberData vault = controllerStates.get(informant.idMember);
            if (vault != null) {
                State vaultState = vault.getState();
                //Создание клавиатуры специальным методом
                String description = vaultState.getDescription();
                if (vaultState.getOperatorWhoGeneratesDescription() != null)
                    description = vaultState.getOperatorWhoGeneratesDescription().apply(
                            new State.Data(informant.idMember, vault, update, bot)
                    );
                InlineKeyboardMarkup inlineKeyboardMarkup = GenerationKeyboard(informant.idMember, vault, update);
                if (vault.getIdMessage() == null) {
                    SendResponse response = bot.execute(new SendMessage(informant.idChat, description).replyMarkup(inlineKeyboardMarkup));
                    vault.setIdMessage(response.message().messageId());
                } else {
                    requests.add(new EditMessageText(informant.idChat, vault.getIdMessage(), vaultState.getDescription()));
                    requests.add(new EditMessageReplyMarkup(informant.idChat, vault.getIdMessage()).replyMarkup(inlineKeyboardMarkup));
                }
            }

            for (BaseRequest x : requests) {
                if (x != null) {
                    bot.execute(x);
                    //            new Thread(() -> {
//                try {
//                    TimeUnit.SECONDS.sleep(25);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }).start();
                }
            }

        } catch (RuntimeException e) {
            //System.out.println("Ошибка CallbackQuery");
            if (update.callbackQuery() != null)
                bot.execute(new AnswerCallbackQuery(update.callbackQuery().id()).text("Ошибка обработки нажатия кнопки").showAlert(true));
            else
                bot.execute(new SendMessage(update.message().chat().id(), "Ошибка обработки текста"));
        } catch (Exception e) {
            System.out.println("Ошибка чего то сломалось! как ты смог!");
        }
    }

    private InformantUpdate definitionIdAndTypeUpdate(Update update) {

        Message message = update.message();
        CallbackQuery callbackQuery = update.callbackQuery();
        ChatMemberUpdated myChatMember = update.myChatMember();

        boolean isMessage = message != null;
        boolean isCallbackQuery = callbackQuery != null;
        boolean isMyChatMember = myChatMember != null;

        InformantUpdate informant = new InformantUpdate();

        if (isMyChatMember) {
            informant.idMember = myChatMember.from().id();
            informant.idChat = myChatMember.chat().id();
            informant.type = TypeUpdate.MyChatMember;
        } else if (isCallbackQuery) {
            informant.idMember = callbackQuery.from().id();
            informant.idChat = callbackQuery.message().chat().id();
            informant.idMessage = callbackQuery.message().messageId();
            informant.type = TypeUpdate.CallbackQuery;
        } else if (isMessage) {
            informant.idMember = message.from().id();
            informant.idChat = message.chat().id();
            informant.idMessage = message.messageId();
            informant.type = TypeUpdate.Message;
        } else {
            informant.idMember = null;
            informant.idChat = null;
            informant.idMessage = null;
            informant.type = TypeUpdate.Others;
        }
        return informant;
    }

    private InlineKeyboardMarkup GenerationKeyboard(Long idMember, MemberData data, Update update) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        if (data.getState().getOperatorWhichGeneratesKeyboard() != null) {
            //GenerationButton
            if (data.getPreviousStateGenerateKeyboard() != data.getState())
                data.setNumberSublist(0);
            data.setPreviousStateGenerateKeyboard(data.getState());
            List<Pair<String, String>> ListResult = data.getState().getOperatorWhichGeneratesKeyboard()
                    .apply(new State.Data(idMember, data, update, bot));

            //Подсписок из результата 8 элементов
            //Так же тут определяется наличие кнопок
            //для перехода влево и вправо
            // - для перемещения по списку.

            int divider = 8;

            List <Pair<String,String>> ListButtons = null;
            int size = ListResult.size();

            int countSubLists = size / divider;
            if (countSubLists % divider > 0)
                countSubLists = countSubLists + 1;

            Integer numberSubList = data.getNumberSublist();
            if (numberSubList == -1) {
                numberSubList = countSubLists - 1;
                data.setNumberSublist(numberSubList);
            }
            else if (countSubLists - 1 < numberSubList)  {
                numberSubList = numberSubList - countSubLists;
                data.setNumberSublist(numberSubList);
            }

            if (size <= divider) {
                ListButtons = ListResult;
                data.setNumberSublist(0);
            }else if (numberSubList != countSubLists - 1) {
                ListButtons = ListResult.subList(numberSubList * divider, (numberSubList + 1) * divider);
            }else {
                ListButtons = ListResult.subList(numberSubList * divider, size);
            }

            for (Pair<String, String> x : ListButtons) {
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton(x.component2())
                        .callbackData(x.component1()));
            }
            if (countSubLists > 0)
                inlineKeyboardMarkup.addRow(
                        new InlineKeyboardButton("⬅").callbackData("⬅⬅⬅"),
                        new InlineKeyboardButton((numberSubList + 1) + "/" + countSubLists).callbackData("Int/Int"),
                        new InlineKeyboardButton("➡").callbackData("➡➡➡")
                );

        } else {
            for (State element :
                    data.getState().getPaths()) {
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton(element.getNameButton()).callbackData(element.getName()));
            }
        }
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Назад").callbackData("Назад"));
        return inlineKeyboardMarkup;
    }

    private enum TypeUpdate {
        Message,
        CallbackQuery,
        MyChatMember,
        Others
    }

    private static class InformantUpdate {
        Long idMember;
        Long idChat;
        Integer idMessage;
        TypeUpdate type;
    }
}
