package net.myapplication.myapp.object.admin.dto.dashboard.analytics;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder 
@NoArgsConstructor
@AllArgsConstructor
public class RevenueChartDto {

    private LocalDate date;

    private BigDecimal revenue;

}
