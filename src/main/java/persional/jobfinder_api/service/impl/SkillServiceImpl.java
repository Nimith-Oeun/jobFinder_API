package persional.jobfinder_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import persional.jobfinder_api.dto.request.SkillRequest;
import persional.jobfinder_api.exception.BadRequestException;
import persional.jobfinder_api.exception.InternalServerError;
import persional.jobfinder_api.model.Skill;
import persional.jobfinder_api.repository.SkillRepository;
import persional.jobfinder_api.service.SkillService;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    @Override
    public Skill create(SkillRequest skillRequest) {

        if (skillRequest.getSkillName().isEmpty()){
            throw new BadRequestException("Skill name must not be empty");
        }

        if (skillRepository.findByName(skillRequest.getSkillName()).isPresent()){
            throw new InternalServerError("Skill already exists");
        }

        Skill skill = new Skill();
        skill.setName(skillRequest.getSkillName());
        return skillRepository.save(skill);
    }
}
