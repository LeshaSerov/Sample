package telegram;

import kotlin.Pair;
import telegram.domain.MemberData;
import telegram.domain.State;
import telegram.domain.StateMachine;
import telegram.operators.OperatorsWhichGeneratesKeyboard;
import telegram.operators.OperatorsWhoProcessesMessages;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Initiator {

    public static State initializeDefaultState() {
        //Объявление машины состояний - тут главное описание.
        StateMachine stateMachine = new StateMachine(
                "Default",
                "Стандартное состояние"
        );

        stateMachine.addPathGenerateKeyboard(
                "ListGroup",
                "Выберите группу из списка",
                "Список Групп",
                Initiator::keyboard,
                MemberData.TypeReceivedInformation.IdGroup,
                "Группа",
                ""
        );

//        stateMachine.addPathProcessesMessages(
//                OperatorsWhoProcessesMessages::addGroup,
//                "AddGroup",
//                "Введите название группы",
//                "Добавить Группу"
//        );

//        stateMachine.addPathGenerateKeyboard(
//                "ListGroup",
//                "Выберите группу из списка",
//                "Список Групп",
//                OperatorsWhichGeneratesKeyboard::listGroup,
//                MemberData.TypeReceivedInformation.IdGroup,
//                "Группа",
//
//        ).next("ListGroup").next("Group");
//        {
//            //Состояние Group
//
//            stateMachine.addPathGenerateKeyboard(
//                    "ListMembersGroup",
//                    "Выберите участника из списка",
//                    "Список участников группы",
//                    OperatorsWhichGeneratesKeyboard::listMembersGroup,
//                    MemberData.TypeReceivedInformation.IdMember,
//                    "Member",
//                    ""
//            ).next("ListMembersGroup").next("Member");
//            {
//                //Состояние Member
//
//                stateMachine.addPathRunAtStartup(
//                        OperatorsWhichRunsAtStartup::deleteMember,
//                        "DeleteMember",
//                        "Удалить участника"
//                );
//
//                stateMachine.addPathRunAtStartup(
//                        OperatorsWhichRunsAtStartup::changeRole,
//                        "ChangeRole",
//                        ""
//                );
//                stateMachine.relocationPathInPathGenerateKeyboard(
//                        "ListRoles",
//                        "",
//                        "Изменить Роль",
//                        OperatorsWhichGeneratesKeyboard::listRoles,
//                        MemberData.TypeReceivedInformation.IdRole,
//                        "ChangeRole"
//                );
//
//                stateMachine.previous().previous();
//            }
//
//            stateMachine.addPathRunAtStartup(
//                    OperatorsWhichRunsAtStartup::addMember,
//                    "AddMember",
//                    ""
//            );
//            stateMachine.relocationPathInPathGenerateKeyboard(
//                    "ListNonMembers",
//                    "Нажмите на пользователя, чтобы добавить его в группу",
//                    "Добавить участников в группу",
//                    OperatorsWhichGeneratesKeyboard::listNonMembers,
//                    MemberData.TypeReceivedInformation.IdMember,
//                    "AddMember"
//            );
//
//            stateMachine.addPathRunAtStartup(
//                    operator,
//                    "Ping",
//                    null
//            );
//            stateMachine.relocationPathInPathGenerateKeyboard(
//                    "ListChats",
//                    "",
//                    "Пинг",
//                    operator,
//                    MemberData.TypeReceivedInformation.IdChat
//            );
//
//            stateMachine.addPath(
//                    "FileSystem",
//                    "Работа с файлами",
//                    "Файловая Система"
//            ).next("FileSystem");
//            {
//                //Категория FileSystem
//
//                stateMachine.addPathProcessesMessages(
//                        OperatorsWhoProcessesMessages::addCategory,
//                        "AddCategory",
//                        "",
//                        "Добавить Категорию"
//                );
//
//                stateMachine.addPathGenerateKeyboard(
//                        "ListCategories",
//                        "",
//                        "Список категорий",
//                        OperatorsWhichGeneratesKeyboard::listCategories,
//                        MemberData.TypeReceivedInformation.IdCategory,
//                        "Category",
//                        ""
//                ).next("ListCategories").next("Category");
//                {
//                    //Состояние Category
//
//                    stateMachine.addPathRunAtStartup(
//                            OperatorsWhichRunsAtStartup::deleteCategory,
//                            "DeleteCategory",
//                            "Удалить категорию"
//                    );
//
//                    stateMachine.addPathProcessesMessages(
//                            OperatorsWhoProcessesMessages::editCategory,
//                            "EditCategory",
//                            "",
//                            "Редактировать категорию"
//                    );
//
//                    stateMachine.previous().previous();
//                }
//
//                stateMachine.addPathProcessesMessages(
//                        OperatorsWhoProcessesMessages::addFile,
//                        "AddFile",
//                        "",
//                        null
//                );
//                stateMachine.relocationPathInPathGenerateKeyboard(
//                        "ListCategories",
//                        "",
//                        "Добавить Файл",
//                        OperatorsWhichGeneratesKeyboard::listCategories,
//                        MemberData.TypeReceivedInformation.IdCategory,
//                        "AddFile"
//                );
//
//                stateMachine.addPath(
//                        "Files",
//                        "",
//                        "Файлы"
//                ).next("Files");
//                {
//                    State stateGroupTime = null;
//
//                    stateMachine.addPath(
//                      "SelectFilesTime",
//                      "",
//                      "Группировка по времени"
//                    ).next("SelectFilesTime");
//                    {
//                        stateGroupTime = stateMachine.getCurrentState();
//
//                        stateMachine.addPathRunAtStartup(
//                                operator,
//                                "",
//                                "За последние 6 часов"
//                        );
//                        stateMachine.addPathRunAtStartup(
//                                operator,
//                                "",
//                                "За последние сутки"
//                        );
//                        stateMachine.addPathRunAtStartup(
//                                operator,
//                                "",
//                                "Последние 10"
//                        );
//                        stateMachine.addPathRunAtStartup(
//                                operator,
//                                "",
//                                "Последние 25   "
//                        );
//                        stateMachine.addPathProcessesMessages(
//                                operator,
//                                "",
//                                "выборка всех файлов от текущего момента и до той даты",
//                                "Своя дата"
//                        );
//
//                        stateMachine.previous();
//                    }
//
//                    stateMachine.addPathGenerateKeyboard(
//                            "ListCategories",
//                            "",
//                            "Группировка по категориям",
//                            operator,
//                            MemberData.TypeReceivedInformation.IdCategory,
//                            "FilesInCategory",
//                            ""
//                    ).next("ListCategories").next("FilesInCategory");
//                    {
//                        stateMachine.addPathRunAtStartup(
//                                operator,
//                                "AllFilesInCategory",
//                                "ВсеФайлыКатегории"
//                        );
//                        stateMachine.getCurrentState().addPath(stateGroupTime);
//
//                        stateMachine.previous().previous();
//                    }
//
//                    stateMachine.addPathProcessesMessages(
//                            operator,
//                            "Search",
//                            "поиск по всем файлам" +
//                                    " в названии которых есть" +
//                                    " такое сочетание букв",
//                            "Поиск"
//                    );
//
//                    stateMachine.addPathRunAtStartup(
//                            operator,
//                            "",
//                            "Все"
//                    );
//
//                    stateMachine.previous();
//                }
//
//                stateMachine.addPathProcessesMessages(
//                        operator,
//                        "",
//                        "Введите id удаляемого файла",
//                        "Удалить Файл"
//                );
//
//                stateMachine.previous();
//            }
//
//            stateMachine.addPathRunAtStartup(
//                null,
//                "DeleteGroup",
//                "Удалить группу"
//            );
//
//            stateMachine.previous().previous();
//        }

        return stateMachine.getDefaultState();
    }

    private static List<Pair<String, String>> keyboard(State.Data data){
        List<Pair<String, String>> result = new ArrayList<>();
        for (Integer i = 0; i < 42; i++)
        {
            result.add(new Pair<String, String>(i.toString() ,i.toString()));
        }
        return result;
    }
}
