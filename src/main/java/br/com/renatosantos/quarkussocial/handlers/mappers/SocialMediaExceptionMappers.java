package br.com.renatosantos.quarkussocial.handlers.mappers;


import br.com.renatosantos.quarkussocial.handlers.exceptions.SocialMediaExceptions;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

public class SocialMediaExceptionMappers {

    @Provider
    @Slf4j
    public static class SelfFollowExceptionMapper implements ExceptionMapper<SocialMediaExceptions.SelfFollowException> {
        @Override
        public Response toResponse(SocialMediaExceptions.SelfFollowException exception) {
            log.error("Caught a SelfFollowException: {}", exception.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(exception.getMessage())
                    .build();
        }
    }

    @Provider
    @Slf4j
    public static class AlreadyFollowedExceptionMapper implements ExceptionMapper<SocialMediaExceptions.AlreadyFollowedException> {
        @Override
        public Response toResponse(SocialMediaExceptions.AlreadyFollowedException exception) {
            log.error("Caught an AlreadyFollowedException: {}", exception.getMessage());
            return Response.status(Response.Status.CONFLICT)
                    .entity(exception.getMessage())
                    .build();
        }
    }

    @Provider
    @Slf4j
    public static class NullPointerExceptionMapper implements ExceptionMapper<NullPointerException> {
        @Override
        public Response toResponse(NullPointerException exception) {
            log.error("Caught a NullPointerException: {}", exception.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(exception.getMessage())
                    .build();
        }
    }
}
