package net.myapplication.myapp.object.order.service;

import org.springframework.stereotype.Component;

import net.myapplication.myapp.enumpack.OrderStatus;
import net.myapplication.myapp.enumpack.PaymentMethod;
import net.myapplication.myapp.enumpack.PaymentStatus;
import net.myapplication.myapp.object.order.entity.Order;

@Component 
public class PaymentStateValidator {

    public void validateCanMarkSuccess(Order order) {

        if (order.getPaymentStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException(
                    "Payment cannot be marked as PAID from status: "
                            + order.getPaymentStatus());
        }

        if (order.getPaymentMethod() == PaymentMethod.COD) {
            throw new IllegalStateException(
                    "COD payment must be completed through COD payment flow.");
        }
    }

    public void validateCanMarkFailed(Order order) {

        if (order.getPaymentStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException(
                    "Payment cannot be marked as FAILED from status: "
                            + order.getPaymentStatus());
        }

        if (order.getPaymentMethod() == PaymentMethod.COD) {
            throw new IllegalStateException(
                    "COD payment cannot be marked as failed through this flow.");
        }
    }

    public void validateCanCompleteCod(Order order) {

        if (order.getPaymentMethod() != PaymentMethod.COD) {
            throw new IllegalStateException(
                    "Order payment method is not COD.");
        }

        if (order.getPaymentStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException(
                    "COD payment cannot be completed from status: "
                            + order.getPaymentStatus());
        }

        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new IllegalStateException(
                    "COD payment can only be completed after order delivery.");
        }
    }
}