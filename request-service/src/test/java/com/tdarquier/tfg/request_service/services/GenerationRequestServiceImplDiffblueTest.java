package com.tdarquier.tfg.request_service.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tdarquier.tfg.request_service.clients.UserActivitiesClient;
import com.tdarquier.tfg.request_service.clients.UserClient;
import com.tdarquier.tfg.request_service.kafka.SGRProducer;
import com.tdarquier.tfg.request_service.messages.GenerationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {GenerationRequestServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class GenerationRequestServiceImplDiffblueTest {
    @Autowired
    private GenerationRequestServiceImpl generationRequestServiceImpl;

    @MockBean
    private SGRProducer sGRProducer;

    @MockBean
    private UserActivitiesClient userActivitiesClient;

    @MockBean
    private UserClient userClient;

    /**
     * Test
     * {@link GenerationRequestServiceImpl#sendGenerationRequest(GenerationRequest, String)}.
     * <ul>
     *   <li>Given {@link UserClient} {@link UserClient#existsById(Long)} return
     * {@code false}.</li>
     *   <li>Then return {@code false}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link GenerationRequestServiceImpl#sendGenerationRequest(GenerationRequest, String)}
     */
    @Test
    @DisplayName("Test sendGenerationRequest(GenerationRequest, String); given UserClient existsById(Long) return 'false'; then return 'false'")
    void testSendGenerationRequest_givenUserClientExistsByIdReturnFalse_thenReturnFalse() {
        // Arrange
        when(userClient.existsById(Mockito.<Long>any())).thenReturn(false);

        // Act
        boolean actualSendGenerationRequestResult = generationRequestServiceImpl
                .sendGenerationRequest(new GenerationRequest(1L, "Services Json"), "Jwt");

        // Assert
        verify(userClient).existsById(eq(1L));
        assertFalse(actualSendGenerationRequestResult);
    }

    /**
     * Test
     * {@link GenerationRequestServiceImpl#sendGenerationRequest(GenerationRequest, String)}.
     * <ul>
     *   <li>Given {@link UserClient} {@link UserClient#existsById(Long)} return
     * {@code true}.</li>
     *   <li>Then return {@code true}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link GenerationRequestServiceImpl#sendGenerationRequest(GenerationRequest, String)}
     */
    @Test
    @DisplayName("Test sendGenerationRequest(GenerationRequest, String); given UserClient existsById(Long) return 'true'; then return 'true'")
    void testSendGenerationRequest_givenUserClientExistsByIdReturnTrue_thenReturnTrue() {
        // Arrange
        when(userClient.existsById(Mockito.<Long>any())).thenReturn(true);
        when(userActivitiesClient.registerCodeGenerationActivity(Mockito.<Long>any()))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));
        doNothing().when(sGRProducer).sendGenerationRequest(Mockito.<GenerationRequest>any(), Mockito.<String>any());

        // Act
        boolean actualSendGenerationRequestResult = generationRequestServiceImpl
                .sendGenerationRequest(new GenerationRequest(1L, "Services Json"), "Jwt");

        // Assert
        verify(userActivitiesClient).registerCodeGenerationActivity(eq(1L));
        verify(userClient).existsById(eq(1L));
        verify(sGRProducer).sendGenerationRequest(isA(GenerationRequest.class), eq("Jwt"));
        assertTrue(actualSendGenerationRequestResult);
    }
}
