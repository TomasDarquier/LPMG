package com.tdarquier.init_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tdarquier.init_service.enums.Template;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {NonInitializrDependenciesServiceImpl.class})
@ExtendWith(SpringExtension.class)
class NonInitializrDependenciesServiceImplDiffblueTest {
    @Autowired
    private NonInitializrDependenciesServiceImpl nonInitializrDependenciesServiceImpl;

    /**
     * Test
     * {@link NonInitializrDependenciesServiceImpl#getDependenciesByTemplate(Template)}.
     * <p>
     * Method under test:
     * {@link NonInitializrDependenciesServiceImpl#getDependenciesByTemplate(Template)}
     */
    @Test
    @DisplayName("Test getDependenciesByTemplate(Template)")
    void testGetDependenciesByTemplate() {
        // Arrange and Act
        List<String> actualDependenciesByTemplate = nonInitializrDependenciesServiceImpl
                .getDependenciesByTemplate(Template.USER_SERVICE_V1);

        // Assert
        assertEquals(1, actualDependenciesByTemplate.size());
        assertEquals("${NON_INITIALIZR_USER_SERVICE_DEPENDENCIES}", actualDependenciesByTemplate.get(0));
    }

    /**
     * Test
     * {@link NonInitializrDependenciesServiceImpl#getDependenciesByTemplate(Template)}.
     * <p>
     * Method under test:
     * {@link NonInitializrDependenciesServiceImpl#getDependenciesByTemplate(Template)}
     */
    @Test
    @DisplayName("Test getDependenciesByTemplate(Template)")
    void testGetDependenciesByTemplate2() {
        // Arrange and Act
        List<String> actualDependenciesByTemplate = nonInitializrDependenciesServiceImpl
                .getDependenciesByTemplate(Template.CART_SERVICE_V1);

        // Assert
        assertEquals(1, actualDependenciesByTemplate.size());
        assertEquals("${NON_INITIALIZR_CART_SERVICE_DEPENDENCIES}", actualDependenciesByTemplate.get(0));
    }

    /**
     * Test
     * {@link NonInitializrDependenciesServiceImpl#getDependenciesByTemplate(Template)}.
     * <p>
     * Method under test:
     * {@link NonInitializrDependenciesServiceImpl#getDependenciesByTemplate(Template)}
     */
    @Test
    @DisplayName("Test getDependenciesByTemplate(Template)")
    void testGetDependenciesByTemplate3() {
        // Arrange and Act
        List<String> actualDependenciesByTemplate = nonInitializrDependenciesServiceImpl
                .getDependenciesByTemplate(Template.SHIPPING_SERVICE_V1);

        // Assert
        assertEquals(1, actualDependenciesByTemplate.size());
        assertEquals("${NON_INITIALIZR_SHIPPING_SERVICE_DEPENDENCIES}", actualDependenciesByTemplate.get(0));
    }

    /**
     * Test
     * {@link NonInitializrDependenciesServiceImpl#getDependenciesByTemplate(Template)}.
     * <p>
     * Method under test:
     * {@link NonInitializrDependenciesServiceImpl#getDependenciesByTemplate(Template)}
     */
    @Test
    @DisplayName("Test getDependenciesByTemplate(Template)")
    void testGetDependenciesByTemplate4() {
        // Arrange and Act
        List<String> actualDependenciesByTemplate = nonInitializrDependenciesServiceImpl
                .getDependenciesByTemplate(Template.ORDER_SERVICE_V1);

        // Assert
        assertEquals(1, actualDependenciesByTemplate.size());
        assertEquals("${NON_INITIALIZR_ORDERS_SERVICE_DEPENDENCIES}", actualDependenciesByTemplate.get(0));
    }

    /**
     * Test
     * {@link NonInitializrDependenciesServiceImpl#getDependenciesByTemplate(Template)}.
     * <p>
     * Method under test:
     * {@link NonInitializrDependenciesServiceImpl#getDependenciesByTemplate(Template)}
     */
    @Test
    @DisplayName("Test getDependenciesByTemplate(Template)")
    void testGetDependenciesByTemplate5() {
        // Arrange and Act
        List<String> actualDependenciesByTemplate = nonInitializrDependenciesServiceImpl
                .getDependenciesByTemplate(Template.NOTIFICATION_SERVICE_V1);

        // Assert
        assertEquals(1, actualDependenciesByTemplate.size());
        assertEquals("${NON_INITIALIZR_NOTIFICATION_SERVICE_DEPENDENCIES}", actualDependenciesByTemplate.get(0));
    }

    /**
     * Test
     * {@link NonInitializrDependenciesServiceImpl#getDependenciesByTemplate(Template)}.
     * <p>
     * Method under test:
     * {@link NonInitializrDependenciesServiceImpl#getDependenciesByTemplate(Template)}
     */
    @Test
    @DisplayName("Test getDependenciesByTemplate(Template)")
    void testGetDependenciesByTemplate6() {
        // Arrange and Act
        List<String> actualDependenciesByTemplate = nonInitializrDependenciesServiceImpl
                .getDependenciesByTemplate(Template.PRODUCT_SERVICE_V1);

        // Assert
        assertEquals(1, actualDependenciesByTemplate.size());
        assertEquals("${NON_INITIALIZR_PRODUCTS_SERVICE_DEPENDENCIES}", actualDependenciesByTemplate.get(0));
    }

    /**
     * Test
     * {@link NonInitializrDependenciesServiceImpl#getDependenciesByTemplate(Template)}.
     * <ul>
     *   <li>When {@code DISCOVERY_SERVICE_V1}.</li>
     *   <li>Then return Empty.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link NonInitializrDependenciesServiceImpl#getDependenciesByTemplate(Template)}
     */
    @Test
    @DisplayName("Test getDependenciesByTemplate(Template); when 'DISCOVERY_SERVICE_V1'; then return Empty")
    void testGetDependenciesByTemplate_whenDiscoveryServiceV1_thenReturnEmpty() {
        // Arrange, Act and Assert
        assertTrue(nonInitializrDependenciesServiceImpl.getDependenciesByTemplate(Template.DISCOVERY_SERVICE_V1).isEmpty());
    }

    /**
     * Test
     * {@link NonInitializrDependenciesServiceImpl#getCompletePom(String, List)}.
     * <ul>
     *   <li>Given {@code foo}.</li>
     *   <li>When {@link ArrayList#ArrayList()} add {@code foo}.</li>
     *   <li>Then throw {@link RuntimeException}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link NonInitializrDependenciesServiceImpl#getCompletePom(String, List)}
     */
    @Test
    @DisplayName("Test getCompletePom(String, List); given 'foo'; when ArrayList() add 'foo'; then throw RuntimeException")
    void testGetCompletePom_givenFoo_whenArrayListAddFoo_thenThrowRuntimeException() {
        // Arrange
        ArrayList<String> extraDependencies = new ArrayList<>();
        extraDependencies.add("foo");

        // Act and Assert
        assertThrows(RuntimeException.class,
                () -> nonInitializrDependenciesServiceImpl.getCompletePom("Pom", extraDependencies));
    }

    /**
     * Test
     * {@link NonInitializrDependenciesServiceImpl#getCompletePom(String, List)}.
     * <ul>
     *   <li>Given {@code foo}.</li>
     *   <li>When {@link ArrayList#ArrayList()} add {@code foo}.</li>
     *   <li>Then throw {@link RuntimeException}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link NonInitializrDependenciesServiceImpl#getCompletePom(String, List)}
     */
    @Test
    @DisplayName("Test getCompletePom(String, List); given 'foo'; when ArrayList() add 'foo'; then throw RuntimeException")
    void testGetCompletePom_givenFoo_whenArrayListAddFoo_thenThrowRuntimeException2() {
        // Arrange
        ArrayList<String> extraDependencies = new ArrayList<>();
        extraDependencies.add("foo");
        extraDependencies.add("foo");

        // Act and Assert
        assertThrows(RuntimeException.class,
                () -> nonInitializrDependenciesServiceImpl.getCompletePom("Pom", extraDependencies));
    }

    /**
     * Test
     * {@link NonInitializrDependenciesServiceImpl#getCompletePom(String, List)}.
     * <ul>
     *   <li>Given {@code null}.</li>
     *   <li>When {@link ArrayList#ArrayList()} add {@code null}.</li>
     *   <li>Then throw {@link RuntimeException}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link NonInitializrDependenciesServiceImpl#getCompletePom(String, List)}
     */
    @Test
    @DisplayName("Test getCompletePom(String, List); given 'null'; when ArrayList() add 'null'; then throw RuntimeException")
    void testGetCompletePom_givenNull_whenArrayListAddNull_thenThrowRuntimeException() {
        // Arrange
        ArrayList<String> extraDependencies = new ArrayList<>();
        extraDependencies.add(null);
        extraDependencies.add("foo");

        // Act and Assert
        assertThrows(RuntimeException.class,
                () -> nonInitializrDependenciesServiceImpl.getCompletePom("Pom", extraDependencies));
    }

    /**
     * Test
     * {@link NonInitializrDependenciesServiceImpl#getCompletePom(String, List)}.
     * <ul>
     *   <li>Given {@code null}.</li>
     *   <li>When {@code </dependencies>}.</li>
     *   <li>Then return {@code </dependencies>}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link NonInitializrDependenciesServiceImpl#getCompletePom(String, List)}
     */
    @Test
    @DisplayName("Test getCompletePom(String, List); given 'null'; when '</dependencies>'; then return '</dependencies>'")
    void testGetCompletePom_givenNull_whenDependencies_thenReturnDependencies() {
        // Arrange
        ArrayList<String> extraDependencies = new ArrayList<>();
        extraDependencies.add(null);

        // Act and Assert
        assertEquals("\n\n</dependencies>",
                nonInitializrDependenciesServiceImpl.getCompletePom("</dependencies>", extraDependencies));
    }

    /**
     * Test
     * {@link NonInitializrDependenciesServiceImpl#getCompletePom(String, List)}.
     * <ul>
     *   <li>When {@link ArrayList#ArrayList()}.</li>
     *   <li>Then return {@code Pom}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link NonInitializrDependenciesServiceImpl#getCompletePom(String, List)}
     */
    @Test
    @DisplayName("Test getCompletePom(String, List); when ArrayList(); then return 'Pom'")
    void testGetCompletePom_whenArrayList_thenReturnPom() {
        // Arrange, Act and Assert
        assertEquals("Pom", nonInitializrDependenciesServiceImpl.getCompletePom("Pom", new ArrayList<>()));
    }
}
