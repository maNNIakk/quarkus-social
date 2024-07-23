package br.com.renatosantos.quarkussocial.utils.logs;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.ArrayList;
import java.util.List;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper <ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<Violation> violations = new ArrayList<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            Violation v = new Violation();
            v.setMessage(violation.getMessage());
            violations.add(v);
        }

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
        errorResponse.setTitle("Constraint Meu Ovo");
        errorResponse.setViolations(violations);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .build();
    }

    public static class Violation {

        private String message;


        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class ErrorResponse {
        private int status;
        private String title;
        private List<Violation> violations;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Violation> getViolations() {
            return violations;
        }

        public void setViolations(List<Violation> violations) {
            this.violations = violations;
        }
    }
}
