package telegram.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberData {
    private State state;
    private Integer idMessage = null;
    private Long idChat = null;
    private Integer idGroup = null;
    private Integer idCategory = null;
    private Long idMember = null;
    private Integer idRole = null;

    public enum TypeReceivedInformation{
        IdMessage,
        IdChat,
        IdGroup,
        IdCategory,
        IdMember,
        IdRole
    }
    public void addInfo(TypeReceivedInformation type, String x){
        switch (type)
        {
            case IdMessage -> idMessage = Integer.valueOf(x);
            case IdChat -> idChat = Long.valueOf(x);
            case IdGroup -> idGroup = Integer.valueOf(x);
            case IdCategory -> idCategory = Integer.valueOf(x);
            case IdMember -> idMember = Long.valueOf(x);
            case IdRole -> idRole = Integer.valueOf(x);
        }
    }

    public MemberData(State state) {
        this.state = state;
    }

    //Переходы Между Состояниями
    public void next(String nameButton) {
        state = state.next(nameButton);
    }
    public void previous() {
        state = state.previous();
    }

}
