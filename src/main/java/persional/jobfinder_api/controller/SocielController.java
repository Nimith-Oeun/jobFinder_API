package persional.jobfinder_api.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import persional.jobfinder_api.dto.request.SocielRequestDTO;
import persional.jobfinder_api.exception.SuccessRespone;
import persional.jobfinder_api.mapper.SocielMapper;
import persional.jobfinder_api.model.Sociel;
import persional.jobfinder_api.service.SocielService;

import java.util.List;

@RestController
@RequestMapping("/jobfinder_api/v1/sociel")
@RequiredArgsConstructor
public class SocielController {

    private final SocielService socielService;



    @PostMapping("/create")
    public ResponseEntity<?> createSociel(@RequestBody SocielRequestDTO socielDTO) {
        Sociel sociel = socielService.create(socielDTO);
        return ResponseEntity.ok(
                SuccessRespone.success(" Sociel Link created successfully")
        );
    }

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<Sociel> sociels = socielService.getAll();
        List<SocielRequestDTO> socielDTOList = sociels.stream()
                .map(SocielMapper.INSTANCE::mapToSocielDTO)
                .toList();
        return ResponseEntity.ok(
                SuccessRespone.success(socielDTOList)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody SocielRequestDTO socielDTO) {
        Sociel sociel = socielService.update(id, socielDTO);
        return ResponseEntity.ok(
                SuccessRespone.success(SocielMapper.INSTANCE.mapToSocielDTO(sociel))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        socielService.delete(id);
        return ResponseEntity.ok(
                SuccessRespone.success("Sociel Link deleted successfully")
        );
    }
}