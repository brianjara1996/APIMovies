package com.w2m.crud.exceptions;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class SpaceshipAspect {
    private static final Logger logger = LoggerFactory.getLogger(SpaceshipAspect.class);

    @Before("execution(* com.w2m.crud.controllers.SpaceController.getSpaceshipById(..)) && args(id)")
    public void logNegativeIdAccess(JoinPoint joinPoint, Long id) {
        if (id < 0) {
            logger.warn("Se ha solicitado una nave con un ID negativo: ", id);
        }
    }
}