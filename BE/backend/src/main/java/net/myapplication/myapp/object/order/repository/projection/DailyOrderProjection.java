package net.myapplication.myapp.object.order.repository.projection;

import java.time.LocalDate;

public interface DailyOrderProjection {
    LocalDate getDate();

    Long getOrders();
}
