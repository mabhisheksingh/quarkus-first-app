package org.acme.utils.customAOP;

import io.vertx.ext.web.RoutingContext;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import org.jboss.logging.Logger;

import java.nio.Buffer;

@Logged
@Interceptor
@Priority(2020)
public class LoggerInterceptor {

    @Inject
    Logger logger;
    @Inject
    RoutingContext routingContext;

    @AroundInvoke
    Object logInvocation(InvocationContext context) throws Exception {
        long start = System.currentTimeMillis();
        logger.info(context.getMethod().getDeclaringClass() + "::" + context.getMethod().getName());
        logger.info(routingContext.request().headers().entries());
        Object result = context.proceed();
        // Intercept the response payload
        if (result instanceof String) {
            // If the response is a String
            String responsePayload = (String) result;
            logger.debug("String Response payload: " + responsePayload);
        } else if (result instanceof Buffer) {
            // If the response is a Buffer (typically used in Vert.x)
            Buffer responseBuffer = (Buffer) result;
            String responsePayload = responseBuffer.toString();
            logger.debug("Buffer Response payload: " + responsePayload);
        }
        // logger.info(routingContext.request().headers().entries());
        logger.info(routingContext.response());
        long end = System.currentTimeMillis();
        logger.infof("Total time taken by method [%s] is %f ms.\n", context.getMethod().getName(), (end - start) / 1.0);
        return result;
    }

}
