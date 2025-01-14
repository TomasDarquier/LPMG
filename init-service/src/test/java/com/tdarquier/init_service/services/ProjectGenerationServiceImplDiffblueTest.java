package com.tdarquier.init_service.services;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tdarquier.init_service.clients.SpringApiClient;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ProjectGenerationServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ProjectGenerationServiceImplDiffblueTest {
    @MockBean
    private NonInitializrDependenciesServiceImpl nonInitializrDependenciesServiceImpl;

    @Autowired
    private ProjectGenerationServiceImpl projectGenerationServiceImpl;

    @MockBean
    private RdfParserServiceImpl rdfParserServiceImpl;

    @MockBean
    private SpringApiClient springApiClient;

    /**
     * Test {@link ProjectGenerationServiceImpl#generateProject(String)}.
     * <ul>
     *   <li>Then return Empty.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link ProjectGenerationServiceImpl#generateProject(String)}
     */
    @Test
    @DisplayName("Test generateProject(String); then return Empty")
    void testGenerateProject_thenReturnEmpty() {
        // Arrange
        when(rdfParserServiceImpl.parseProjectRequest(Mockito.<String>any())).thenReturn(new ArrayList<>());

        // Act
        List<String> actualGenerateProjectResult = projectGenerationServiceImpl.generateProject("Rdf");

        // Assert
        verify(rdfParserServiceImpl).parseProjectRequest(eq("Rdf"));
        assertTrue(actualGenerateProjectResult.isEmpty());
    }
}
