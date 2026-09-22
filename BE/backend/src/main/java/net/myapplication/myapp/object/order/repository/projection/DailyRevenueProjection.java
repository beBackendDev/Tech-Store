package net.myapplication.myapp.object.order.repository.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DailyRevenueProjection {
    LocalDate getDate();

    BigDecimal getRevenue();
}
