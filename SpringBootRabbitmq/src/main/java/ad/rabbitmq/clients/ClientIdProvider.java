package ad.rabbitmq.clients;

import java.util.List;

@FunctionalInterface
public interface ClientIdProvider {
	List<Long> fetchClientIds();
}
