package rs.raf.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.services.ErrorMessageService;

@CrossOrigin
@RestController
@RequestMapping("/errors")
public class ErrorMessageController {
    private final ErrorMessageService errorMessageService;

    @Autowired
    public ErrorMessageController(ErrorMessageService errorMessageService) {
        this.errorMessageService = errorMessageService;
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ErrorMessage>> getAllErrors(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseEntity<>(this.errorMessageService.paginate(page, size), HttpStatus.OK);
    }

}
