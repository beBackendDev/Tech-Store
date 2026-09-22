package net.myapplication.myapp.object.admin.dto.dashboard;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data 
@Builder 
public class DashboardOverviewDto {

    private BigDecimal totalRevenue;

    private Long totalOrders;

    private Long totalProducts;

    private Long totalCustomers;

}
