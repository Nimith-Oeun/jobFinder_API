package persional.jobfinder_api.service;

import persional.jobfinder_api.dto.request.ForgotPasswordRequest;
import persional.jobfinder_api.dto.request.ResetPasswordRequest;

public interface ForgotPasswordService {

    void forgotPassword(ForgotPasswordRequest email);
    void resetPassword(ResetPasswordRequest reset);
}
