package ru.jekajops.quadcopterbot.service;

import org.springframework.stereotype.Service;
import ru.jekajops.quadcopterbot.exceptions.ProcessException;
import ru.jekajops.quadcopterbot.exceptions.StatusType;
import ru.jekajops.quadcopterbot.models.Order;
import ru.jekajops.quadcopterbot.repository.OrderRepository;

@Service
public class OrderService implements DataService<Order, Long, OrderRepository> {
   private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void acceptOrder(Order order) throws ProcessException {
        if (!order.getStatus().equals(Order.Status.CREATED))
            throw new ProcessException(StatusType.builder()
                    .code(-1)
                    .desc("Illegal order status")
                    .build());
        order.setStatus(Order.Status.ACCEPTED);
        orderRepository.save(order);
    }

    public void rejectOrder(Order order) throws ProcessException {
        if (!order.getStatus().equals(Order.Status.CREATED))
            throw new ProcessException(StatusType.builder()
                    .code(-1)
                    .desc("Illegal order status")
                    .build());
        order.setStatus(Order.Status.REJECTED);
        orderRepository.save(order);
    }

    public void saveOrder(Order order) throws ProcessException {
        order.setStatus(Order.Status.CREATED);
        orderRepository.save(order);
    }

    @Override
    public OrderRepository repository() {
        return orderRepository;
    }

}
