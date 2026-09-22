package net.myapplication.myapp.object.admin.dto.dashboard.analytics;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderChartDto {
    private LocalDate date;

    private Long orders;
}
