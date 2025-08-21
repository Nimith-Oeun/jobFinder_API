package persional.jobfinder_api.service;

import persional.jobfinder_api.dto.request.ForgotPasswordRequest;
import persional.jobfinder_api.dto.request.RegisterRequest;
import persional.jobfinder_api.dto.request.VerifyOTPRequest;

public interface RegisterService {

    void register(RegisterRequest registerRequest);
    boolean verifyRegister(VerifyOTPRequest verifyOTPRequest);
    void resendOTP(String email);

}
