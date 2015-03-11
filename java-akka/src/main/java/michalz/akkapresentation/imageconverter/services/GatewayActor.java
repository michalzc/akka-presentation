package michalz.akkapresentation.imageconverter.services;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import michalz.akkapresentation.imageconverter.domain.protocol.InitializeGateway;
import michalz.akkapresentation.imageconverter.domain.protocol.JobStarted;
import michalz.akkapresentation.imageconverter.domain.protocol.StoreImage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * Created by michal on 11.03.15.
 */
@Component
@Scope("prototype")
public class GatewayActor extends AbstractActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);


    @PostConstruct
    public void setupActor() {
        log.info("Creating");

        receive(ReceiveBuilder.match(InitializeGateway.class, msg -> {
            log.info("Gateway initialized");
        }).match(StoreImage.class, msg -> {
            sender().tell(storeImage(msg), self());
        }).matchAny(msg -> {
            log.info("Received unknown message: {}", msg);
        }).build());

        log.info("Created");
    }

    private JobStarted storeImage(StoreImage msg) {
        String jobId = createJobId();
        return new JobStarted(jobId);
    }

    private String createJobId() {
        return UUID.randomUUID().toString();
    }

    public static Props props(ApplicationContext applicationContext) {
        return Props.create(GatewayActor.class, () -> {
            return applicationContext.getBean(GatewayActor.class);
        });
    }
}
