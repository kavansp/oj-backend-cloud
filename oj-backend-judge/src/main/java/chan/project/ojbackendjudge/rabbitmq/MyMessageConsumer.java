package chan.project.ojbackendjudge.rabbitmq;

import chan.project.ojbackendjudge.Judge.JudgeService;
import chan.project.ojbackendmodel.model.entity.QuestionSubmit;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class MyMessageConsumer {

    @Resource
    private JudgeService judgeService;
    @Autowired
    private HttpMessageConverters messageConverters;

    // 指定程序监听的消息队列和确认机制
    @SneakyThrows
    @RabbitListener(queues = {"code_queue"}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("receiveMessage message = {}", message);
        System.out.println("获取消费信息" + message);
        long questionSubmitId = Long.parseLong(message);
        try {
            judgeService.doCode(questionSubmitId);
            channel.basicAck(deliveryTag, false);
        }catch (Exception e) {
            channel.basicNack(deliveryTag,false,false);
        }
    }

}
