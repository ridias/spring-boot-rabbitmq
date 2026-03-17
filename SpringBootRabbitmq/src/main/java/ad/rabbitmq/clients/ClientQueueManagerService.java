package ad.rabbitmq.clients;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import ad.rabbitmq.shared.BatchBuffer;
import ad.rabbitmq.shared.models.RabbitEnvelope;
import ad.rabbitmq.shared.models.RabbitMessageEventClientMsg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio orquestador que coordina la creación y dar de baja colas RabbitMQ
 * y sus consumers asociados de forma dinámica.
 * 
 * Nota: Dar de baja un cliente significa detener su consumer y eliminarlo del tracking interno.
 * La cola y el exchange permanecen en el broker.
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientQueueManagerService {

	private final DynamicQueueCreatorService queueCreatorService;
    private final DynamicConsumerService dynamicConsumerService;
    private final ClientQueueProperties queueProperties;
    
    public void initializeQueueClients(Collection<Long> clientIds) {
        log.info("Initializing {} clients...", clientIds.size());
        clientIds.forEach(id -> registerClient(id));
    }

    public void syncQueueClients(Collection<Long> updatedClientIds) {
        Set<Long> incoming = new HashSet<>(updatedClientIds);
        Set<Long> current  = new HashSet<>(dynamicConsumerService.getActiveClientsIds());

        Set<Long> toAdd    = difference(incoming, current);
        Set<Long> toRemove = difference(current, incoming);

        if (toAdd.isEmpty() && toRemove.isEmpty()) {
            log.debug("No changes in list clients.");
            return;
        }

        log.info("Sync — New clients: {}, Clients to remove: {}", toAdd, toRemove);
        toAdd.forEach(id    -> registerClient(id));
        toRemove.forEach(id -> unregisterClient(id));
    }

    public void registerClient(Long clientId) {
        String queueName  = queueProperties.buildQueueName(clientId);
        String routingKey = queueProperties.buildRoutingKey(clientId);
        String exchange   = queueProperties.getExchangeName();

        queueCreatorService.createQueueAndBinding(queueName, exchange, routingKey);
        log.info("Queue declared: '{}' → exchange='{}' routingKey='{}'",
                queueName, exchange, routingKey);

        dynamicConsumerService.startConsumer(clientId, queueName);
    }

    public void unregisterClient(Long clientId) {
        dynamicConsumerService.stopConsumer(clientId);
        log.info("Cliente {} removed correctly.", clientId);
    }

    public Set<Long> getActiveClientIds() {
        return dynamicConsumerService.getActiveClientsIds();
    }
    
    public List<BatchBuffer<RabbitEnvelope<RabbitMessageEventClientMsg>>> getAllBuffers(){
    	return dynamicConsumerService.getAllBatchBuffers();
    }

    private static Set<Long> difference(Set<Long> a, Set<Long> b) {
        Set<Long> result = new HashSet<>(a);
        result.removeAll(b);
        return result;
    }
}
