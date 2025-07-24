package persional.jobfinder_api.exception;


public class CustomMaxUploadSizeExceededException extends RuntimeException {

   public CustomMaxUploadSizeExceededException(String message) {
      super(message);

   }

}