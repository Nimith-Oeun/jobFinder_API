package persional.jobfinder_api.utils;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import persional.jobfinder_api.exception.BadRequestException;


@Service
public class HandleText {

    public void HandleText(String text) {
        if (StringUtils.hasText(text)){
            return;
        }
        throw new BadRequestException("Invalid text");
    }
}