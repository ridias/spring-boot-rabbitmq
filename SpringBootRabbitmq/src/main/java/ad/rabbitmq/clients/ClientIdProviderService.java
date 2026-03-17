package ad.rabbitmq.clients;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ClientIdProviderService implements ClientIdProvider {

	@Override
	public List<Long> fetchClientIds() {
		return List.of(1L, 2L, 3L, 4L, 5L);
	}

}
