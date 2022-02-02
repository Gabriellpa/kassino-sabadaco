package com.gabriel.kassino.sabadaco.application.api;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeathCheck {

    @GetMapping
    public ResponseEntity<HeathCheckDTO> heathCheck() {
        return ResponseEntity.ok(HeathCheckDTO.builder().status("UP").build());
    }

}


@Data
@Builder
class HeathCheckDTO {
    private String status;
}