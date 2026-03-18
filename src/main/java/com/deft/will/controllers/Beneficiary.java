package com.deft.will.controllers;
import com.deft.will.dtos.WillFormRequest;
import com.deft.will.dtos.WillFormResponse;
import com.deft.will.models.PdfRequest;
import com.deft.will.services.PdfService;
import com.deft.will.services.WillService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:5000"})
@Slf4j
public class Beneficiary {

    private final WillService willService;
    private final PdfService pdfService;

    public Beneficiary(WillService willService, PdfService pdfService) {
        this.willService = willService;
        this.pdfService=pdfService;
    }

    @GetMapping("/get/beneficiary/{id}")
    public Mono<ResponseEntity<WillFormResponse>> getBeneficiary(@PathVariable String id) {

        return willService.getWill(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
        @GetMapping("/get_all/beneficiary")
        public Flux<ResponseEntity<WillFormResponse>> getAllBeneficiary () {
            return willService.getAllWill().map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
        }

        @PostMapping("/add/beneficiary")
        public Mono<ResponseEntity<WillFormResponse>> addBeneficiary (@RequestBody WillFormRequest willFormRequest){
            System.out.println("test");
            return willService.createWill(willFormRequest)
                    .map(response-> ResponseEntity.status(HttpStatus.CREATED).body(response));

        }

        @DeleteMapping("/delete/beneficiary/{id}")
        public Mono<ResponseEntity<Object>> delBeneficiary (@PathVariable String id){
            return willService.delBeneficiary(id).then(Mono.just(ResponseEntity.noContent().build()));
        }
        @PutMapping("/update/beneficiary/{id}")
        public Mono<ResponseEntity<WillFormResponse>> updateBeneficiary (@PathVariable String id, @RequestBody WillFormRequest
        updateRequest){
            return willService.updateBeneficiary(id, updateRequest).map(ResponseEntity::ok)
                    .defaultIfEmpty(ResponseEntity.notFound().build());
        }

        @PostMapping("/generate/pdf")
        public Mono<String> processPdf( @Valid @RequestBody PdfRequest data){
        List<String> keys=List.of("templates/header.html", "templates/body.html", "templates/footer.html");
        return pdfService.processWillPdf(keys, data);
        }
    }

