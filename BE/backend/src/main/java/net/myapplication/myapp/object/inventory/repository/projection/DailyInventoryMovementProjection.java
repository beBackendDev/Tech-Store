package net.myapplication.myapp.object.inventory.repository.projection;

import java.time.LocalDate;

public interface DailyInventoryMovementProjection {
    LocalDate getDate();

    Long getQuantity();
}
