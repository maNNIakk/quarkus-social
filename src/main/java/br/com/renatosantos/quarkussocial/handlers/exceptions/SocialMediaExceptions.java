package br.com.renatosantos.quarkussocial.handlers.exceptions;

public class SocialMediaExceptions{
    public static class SelfFollowException extends RuntimeException{
        public SelfFollowException(String message){
            super(message);
        }
    }

    public static class AlreadyFollowedException extends RuntimeException{
        public AlreadyFollowedException(String message){
            super(message);
        }
    }
}
