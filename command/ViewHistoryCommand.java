package command;

// Concrete Command - 목록 조회하기
public class ViewHistoryCommand implements Command {
    private final ReservationReceiver receiver;

    public ViewHistoryCommand(ReservationReceiver receiver) {
        this.receiver = receiver;
    }
    @Override
    public void execute() { receiver.viewHistory(); }
}
