package persional.jobfinder_api.service;

import persional.jobfinder_api.dto.request.SkillRequest;
import persional.jobfinder_api.model.Skill;

public interface SkillService {

    Skill create(SkillRequest skillRequest);
}
