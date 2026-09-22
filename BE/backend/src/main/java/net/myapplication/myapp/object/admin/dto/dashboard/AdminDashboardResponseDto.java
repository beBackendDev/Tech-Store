package net.myapplication.myapp.object.admin.dto.dashboard;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponseDto {
    private DashboardOverviewDto overview;

    private OrderStatisticDto orderStatistics;

    private ProductStatisticDto productStatistics;

    private InventoryStatisticDto inventoryStatistics;

    private List<RecentOrderDto> recentOrders;

    private List<LowStockProductDto> lowStockProducts;

}
