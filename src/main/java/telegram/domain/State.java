package telegram.domain;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import kotlin.Pair;
import lombok.*;
import util.ConnectionPool.ConnectionPool;

import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.function.BiFunction;
import java.util.function.Function;

@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class State {

    private final String name;
    private final String description;
    private final String nameButton;

    @ToString.Exclude
    private final State stateReturn;

    private State stateNext = null;


    //Механизм, обеспечивающий возможность подключать свои функции для обработки состояний
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Data{
        Long idMember;
        MemberData data;
        Update update;
        ConnectionPool connector;
    }
    private Function<Data, List<BaseRequest>> operatorWhichRunsAtStartup = null;
    private Function<Data, List<BaseRequest>> operatorWhoProcessesMessages = null;
    private Function<Data,  List<Pair<String, String>>> operatorWhichGeneratesKeyboard = null;
    private MemberData.TypeReceivedInformation type = null;
    public void addGenerateKeyboard(Function<Data,  List<Pair<String, String>>> operator, MemberData.TypeReceivedInformation type, State stateNext) {
        this.operatorWhichGeneratesKeyboard = operator;
        this.type = type;
        this.stateNext = stateNext;
    }

    //Список Путей-Состояний по которым генерируется клавиатура
    private Vector<State> paths = new Vector<>();

    //Для Дефолтного Состояния - возможно стоит удалить
    public State(String name, String description) {
        this.name = name;
        this.description = description;
        this.nameButton = null;
        this.stateReturn = this;
    }
    public void addPath(State state) {
        paths.add(state);
    }

    public State next(String name) {
        if (stateNext != null) return stateNext;
        return paths.stream().filter(state -> Objects.equals(state.getName(), name)).findFirst().orElseThrow();
    }
    public State previous() {
        return stateReturn != null ? stateReturn : this;
    }
}
