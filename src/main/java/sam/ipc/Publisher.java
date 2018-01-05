package sam.ipc;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Publisher implements CommandLineRunner {

    private RabbitTemplate template;
    private Subscriber subscriber;
    private ConfigurableApplicationContext context;


    public Publisher(Subscriber subscriber, RabbitTemplate rabbitTemplate,
                  ConfigurableApplicationContext context) {
        this.subscriber = subscriber;
        this.template = rabbitTemplate;
        this.context = context;
    }

    @Override
    public void run(String... args) throws Exception {
        template.convertAndSend(Application.QUEUE, "message from me");
        subscriber.getLatch().await();
        context.close();
    }
}
