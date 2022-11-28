package de.schneider.robin.backend.across.aop;

import de.schneider.robin.backend.db.model.StatisticEvent;
import de.schneider.robin.backend.db.repository.StatisticEventRepository;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
@Log4j2
public class AopAspect { // aspect

    private final StatisticEventRepository statisticEventRepository;

    public AopAspect(
            StatisticEventRepository statisticEventRepository
    ) {
        this.statisticEventRepository = statisticEventRepository;
    }

    @Before("within(de.schneider.robin.backend.api.rest..*)") // pointcut
    public void beforeApiController( // advice
            JoinPoint joinPoint // some method in the controller
    ) {
        log.info("beforeApiController joinPoint: " + joinPoint);
        logMethodArguments(joinPoint.getArgs());

        String method = joinPoint.toShortString();
        String arguments = Arrays.toString(joinPoint.getArgs());
        StatisticEvent statisticEvent = new StatisticEvent(method + " " + arguments);
        statisticEventRepository.save(statisticEvent);
    }

    private void logMethodArguments(Object[] signatureArgs) {
        for (Object signatureArg: signatureArgs) {
            log.info("method argument: " + signatureArg);
        }
    }

    @Before("@annotation(de.schneider.robin.backend.across.aop.Printer)") // pointcut
    public void beforePrinterAnnotation( // advice
            JoinPoint joinPoint // some method with this annotation
    ) {
        log.info("beforePrinterAnnotation joinPoint: " + joinPoint);
        logPrinterMessage((MethodSignature) joinPoint.getSignature());
    }

    private void logPrinterMessage(
            MethodSignature signature
    ) {
        Method method = signature.getMethod(); // via reflection
        Printer printerAnnotation = method.getAnnotation(Printer.class);
        log.info("printer annotation message: " + printerAnnotation.message());
    }

}
