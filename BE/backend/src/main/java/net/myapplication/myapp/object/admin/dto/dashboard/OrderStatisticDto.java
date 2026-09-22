package net.myapplication.myapp.object.admin.dto.dashboard;

import lombok.Builder;
import lombok.Data;

@Data 
@Builder 
public class OrderStatisticDto {

    private Long totalOrders;

    private Long pendingOrders;

    private Long confirmedOrders;

    private Long processingOrders;

    private Long shippedOrders;

    private Long deliveredOrders;

    private Long cancelledOrders;

    private Long returnedOrders;
    
    private Long returnRequests;


}
