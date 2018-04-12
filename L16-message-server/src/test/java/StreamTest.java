import com.jbtits.otus.lecture16.ms.app.Address;
import com.jbtits.otus.lecture16.ms.app.ClientType;
import org.junit.Test;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.HashMap;

public class StreamTest {

    @Test
    public void randomElementTest() {
        Map<Address, String> clients = new HashMap<>();
        clients.put(new Address(ClientType.DB_SERVICE), "worker-db1");
        clients.put(new Address(ClientType.DB_SERVICE), "worker-db2");
        clients.put(new Address(ClientType.DB_SERVICE), "worker-db3");
        clients.put(new Address(ClientType.DB_SERVICE), "worker-db4");
        clients.put(new Address(ClientType.FRONTEND_SERVICE), "worker-front1");
        System.out.println(getRandomAddresstByType(clients, ClientType.DB_SERVICE));
    }

    private Address getRandomAddresstByType(Map<Address, ?> clients, ClientType type) {
        Random generator = new Random();
        Object values[] = clients.entrySet().stream()
                .map(Map.Entry::getKey)
                .filter(typeFilter(type))
                .toArray();
        if (values.length < 1) {
            return null;
        }
        return (Address) values[generator.nextInt(values.length)];
    }

    private Predicate<Address> typeFilter(ClientType type) {
        return address -> {
            return address.getType().equals(type);
        };
    }
}
