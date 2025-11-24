package command;

import domain.PaymentMethod;
import domain.Region;
import java.time.LocalDateTime;
import java.util.List;

// Concrete Command - 예약하기
public class BookCommand implements Command {
    private final ReservationReceiver receiver;
    private final String roomId;
    private final Region region;
    private final List<String> options;
    private final PaymentMethod payMethod;
    private final LocalDateTime start;
    private final LocalDateTime end;

    public BookCommand(ReservationReceiver receiver, String roomId, Region region, List<String> options, 
                       PaymentMethod payMethod, LocalDateTime start, LocalDateTime end) {
        this.receiver = receiver;
        this.roomId = roomId;
        this.region = region;
        this.options = options;
        this.payMethod = payMethod;
        this.start = start;
        this.end = end;
    }

    @Override
    public void execute() {
        receiver.makeReservation(roomId, region, options, payMethod, start, end);
    }
}