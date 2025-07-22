package persional.jobfinder_api.service;


import persional.jobfinder_api.dto.request.SocielRequestDTO;
import persional.jobfinder_api.model.Sociel;

import java.util.List;

public interface SocielService {

    Sociel create (SocielRequestDTO socielRequestDTO);
    Sociel update (Long id, SocielRequestDTO socielRequestDTO);
    Sociel getById (Long id);
    List<Sociel> getAll ();
    void delete (Long id);
}