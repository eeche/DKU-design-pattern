package command;

// Concrete Command - 예약 취소하기
public class CancelCommand implements Command {
    private final ReservationReceiver receiver;
    private final String roomId;

    public CancelCommand(ReservationReceiver receiver, String roomId) {
        this.receiver = receiver;
        this.roomId = roomId;
    }
    @Override
    public void execute() { receiver.cancelBooking(roomId); }
}
