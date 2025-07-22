package persional.jobfinder_api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import persional.jobfinder_api.dto.request.SocielRequestDTO;
import persional.jobfinder_api.exception.BadRequestException;
import persional.jobfinder_api.exception.ResourNotFound;
import persional.jobfinder_api.mapper.SocielMapper;
import persional.jobfinder_api.model.Sociel;
import persional.jobfinder_api.repository.SocielRepository;
import persional.jobfinder_api.service.SocielService;
import persional.jobfinder_api.utils.HandleText;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocielServiceImpl implements SocielService {

    private final SocielRepository socielRepository;
    private final SocielMapper socielMapper;
    private final HandleText handleText;

    @Override
    public Sociel create(SocielRequestDTO socielDTO) {

        handleText.HandleText(socielDTO.getFacebook());
        handleText.HandleText(socielDTO.getInstagram());
        handleText.HandleText(socielDTO.getTwitter());
        handleText.HandleText(socielDTO.getLinkedIn());

        Sociel sociel = socielMapper.mapToSociel(socielDTO);
        return socielRepository.save(sociel);
    }

    @Override
    public Sociel getById(Long id) {

       return socielRepository.findById(id)
                .orElseThrow(() -> new ResourNotFound("Sociel " + id + " not found"));
    }

    @Override
    public Sociel update(Long id, SocielRequestDTO socielDTO) {
        Sociel socielId = getById(id);

        handleText.HandleText(socielDTO.getFacebook());
        handleText.HandleText(socielDTO.getInstagram());
        handleText.HandleText(socielDTO.getTwitter());
        handleText.HandleText(socielDTO.getLinkedIn());

        if (socielRepository.findByFacebook(socielDTO.getFacebook()).isPresent()){
            throw new BadRequestException("Facebook is already exist");
        }
        if (socielRepository.findByInstagram(socielDTO.getInstagram()).isPresent()){
            throw new BadRequestException("Instagram is already exist");
        }
        if (socielRepository.findByTwitter(socielDTO.getTwitter()).isPresent()){
            throw new BadRequestException("Twitter is already exist");
        }
        if (socielRepository.findByLinkedInIs(socielDTO.getLinkedIn()).isPresent()){
            throw new BadRequestException("LinkedIn is already exist");
        }

        socielId.setFacebook(socielDTO.getFacebook());
        socielId.setInstagram(socielDTO.getInstagram());
        socielId.setTwitter(socielDTO.getTwitter());
        socielId.setLinkedIn(socielDTO.getLinkedIn());

       return socielRepository.save(socielId);
    }

    @Override
    public List<Sociel> getAll() {
        return socielRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        Sociel sociel = getById(id);
        socielRepository.delete(sociel);
    }
}