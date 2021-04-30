package az.dynamics.task.controller;

import az.dynamics.task.model.request.CreateZipFileRequestDto;
import az.dynamics.task.model.response.CreateZipFileResponseDto;
import az.dynamics.task.model.response.ZippingInfoDto;
import az.dynamics.task.service.ZippingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Kanan
 */
@RestController
public class ZippingController {
    private final ZippingService zippingService;

    public ZippingController(ZippingService zippingService) {
        this.zippingService = zippingService;
    }

    @PostMapping("/zip")
    public ResponseEntity<CreateZipFileResponseDto> create(@Valid @RequestBody CreateZipFileRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(zippingService.zipping(request));
    }

    @GetMapping("/status")
    public ResponseEntity<ZippingInfoDto> get(@RequestParam Integer id) {
        return ResponseEntity.ok(zippingService.getZippingInfo(id));
    }
}
