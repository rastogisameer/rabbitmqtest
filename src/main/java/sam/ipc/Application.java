package sam.ipc;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public final static String QUEUE = "my-queue";

    @Bean
    public Queue queue(){
        return new Queue(QUEUE, false);
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange("my-exchange");
    }
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(QUEUE);
    }
    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory factory, MessageListenerAdapter adapter){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.setQueueNames(QUEUE);
        container.setMessageListener(adapter);
        return container;
    }
    @Bean
    public MessageListenerAdapter adapter(Subscriber sub){
        return new MessageListenerAdapter(sub, "onMessage");
    }


    public static void main(String...args){
        SpringApplication.run(Application.class, args);
    }

}
