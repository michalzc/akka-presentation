package michalz.akkapresentation.imageconverter.services;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import michalz.akkapresentation.imageconverter.domain.protocol.InitializeGatewayReq;
import michalz.akkapresentation.imageconverter.domain.protocol.JobStartedResp;
import michalz.akkapresentation.imageconverter.domain.protocol.StoreImageReq;
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

        receive(ReceiveBuilder.match(InitializeGatewayReq.class, msg -> {
            log.info("Gateway initialized");
        }).match(StoreImageReq.class, msg -> {
            sender().tell(storeImage(msg), self());
        }).matchAny(msg -> {
            log.info("Received unknown message: {}", msg);
        }).build());

        log.info("Created");
    }

    private JobStartedResp storeImage(StoreImageReq msg) {
        String jobId = createJobId();
        return new JobStartedResp(jobId);
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
