package ad.rabbitmq.clients;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ad.rabbitmq.shared.BatchBuffer;
import ad.rabbitmq.shared.models.RabbitEnvelope;
import ad.rabbitmq.shared.models.RabbitMessageEventClientMsg;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ClientQueueSyncScheduler {

	@Autowired
	private ClientQueueManagerService clientQueueManagerService;
	@Autowired
	private ClientIdProviderService clientProvider;

    /**
     * Se ejecuta una sola vez cuando la aplicación está lista.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("[Scheduler] Application ready. Initializing clients queues...");
        List<Long> ids = this.clientProvider.fetchClientIds();
        clientQueueManagerService.initializeQueueClients(ids);
    }

    @Scheduled(initialDelay = 5000, fixedDelayString = "${rabbitmq.client.sync.interval-ms:5000}")
    public void syncClients() {
        log.debug("Executing sync clients...");
        List<Long> ids = this.clientProvider.fetchClientIds();
        clientQueueManagerService.syncQueueClients(ids);
    }
    
    @Scheduled(fixedDelayString = "${app.batch.max-wait-ms:5000}")
    public void executeFlushBuffers() {
    	log.debug("Executing flush for all active buffers...");
    	List<BatchBuffer<RabbitEnvelope<RabbitMessageEventClientMsg>>> buffers = clientQueueManagerService.getAllBuffers();
    	for(int i = 0; i < buffers.size(); i++) {
    		buffers.get(i).flushIfNotEmpty();
    	}
    }
}
