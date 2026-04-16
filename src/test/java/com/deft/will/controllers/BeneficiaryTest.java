package com.deft.will.controllers;
import com.deft.will.dtos.WillFormRequest;
import com.deft.will.dtos.WillFormResponse;
import com.deft.will.services.PdfService;
import com.deft.will.services.WillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(MockitoExtension.class)
class BeneficiaryTest {
    @Mock
    private WillService willService;
    private PdfService pdfService;


    private WebTestClient webTestClient;
    @BeforeEach
    void setUp() {
        Beneficiary controller = new Beneficiary(willService, pdfService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }
    @Test
    void getBeneficiary() {

        LocalDate localDate = LocalDate.parse("1999-11-12");
        WillFormResponse response= new WillFormResponse("1", "John", "Doe", "john123@gmail.com","12345" , localDate, "friend");

        Mockito.when(willService.getWill("1")).thenReturn(Mono.just(response));
        webTestClient.get()
                .uri("/api/get/beneficiary/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(WillFormResponse.class)
                .value(resp -> {
                    assertEquals("John", resp.getFirstName());
                    assertEquals("Doe", resp.getLastName()); });


        Mockito.verify(willService).getWill("1");

    }

//    @Test
    void getAllBeneficiary() {
        LocalDate localDate = LocalDate.parse("1999-11-12");
        WillFormResponse response= new WillFormResponse("1", "John", "Doe", "john123@gmail.com","12345" , localDate, "friend");

        Mockito.when(willService.getAllWill()).thenReturn(Flux.just(response));

        webTestClient.get()
                .uri("/api/get_all/beneficiary")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(WillFormResponse.class)
                .value(list -> {
                    assertEquals(1, list.size());
                    assertEquals("John", list.get(0).getFirstName());
                });
        Mockito.verify(willService).getAllWill();

    }

//    @Test
    void addBeneficiary() {
        LocalDate localDate = LocalDate.parse("1999-11-12");
        WillFormResponse response= new WillFormResponse("1", "John", "Doe", "john123@gmail.com","12345" , localDate, "friend");
        WillFormRequest req=new WillFormRequest("John", "Doe", "john123@gmail.com","12345" , localDate, "friend");
        Mockito.when(willService.createWill(req)).thenReturn(Mono.just(response));
        webTestClient.post()
                .uri("/api/add/beneficiary")
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk()
                .expectBody(WillFormResponse.class)
                .value(res -> {
                    assertEquals("John", res.getFirstName());
                    assertEquals("Doe", res.getLastName());

                });
          Mockito.verify(willService).createWill(req);
    }

//    @Test
    void delbeneficiary() {
        LocalDate localDate = LocalDate.parse("1999-11-12");
        WillFormResponse response= new WillFormResponse("1", "John", "Doe", "john123@gmail.com","12345" , localDate, "friend");
          Mockito.when(willService.delBeneficiary("1")).thenReturn(Mono.empty());
        webTestClient.delete()
                .uri("/api/delete/beneficiary/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();

        Mockito.verify(willService).delBeneficiary("1");

    }

    @Test
    void updateBeneficiary() {
        LocalDate localDate = LocalDate.parse("1999-11-12");
        WillFormResponse response= new WillFormResponse("1", "John", "Doe", "john123@gmail.com","12345" , localDate, "friend");
        WillFormRequest req=new WillFormRequest("John", "Doe", "john123@gmail.com","12345" , localDate, "friend");

        Mockito.when(willService.updateBeneficiary("1", req)).thenReturn(Mono.just(response));
        webTestClient.put()
                .uri("/api/update/beneficiary/1")
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk()
                .expectBody(WillFormResponse.class)
                .value(res -> {
                    assertEquals("John", res.getFirstName());
                    assertEquals("Doe", res.getLastName());
                    assertEquals("john123@gmail.com", res.getEmail());
                    assertEquals("12345", res.getMobile());
                    assertEquals(localDate, res.getDoB());
                    assertEquals("friend", res.getRelation());

                });
        Mockito.verify(willService).updateBeneficiary("1", req);

    }
}