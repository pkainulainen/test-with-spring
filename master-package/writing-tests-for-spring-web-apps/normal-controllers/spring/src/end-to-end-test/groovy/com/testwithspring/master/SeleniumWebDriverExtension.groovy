package com.testwithspring.master

import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.SpecInfo

/**
 * This extension simply registers two interceptors which have the
 * following responsibilities:
 * <ol>
 *     <li>
 *         The {@link SeleniumWebDriverInitializer} creates a new {@code WebDriver}
 *         object and injects the created object into a shared instance field that is
 *         annotated with the {@link @SeleniumWebDriver} annotation.
 *     </li>
 *     <li>
 *         The {@link SeleniumWebDriverCleanUp} simply quits the used {@WebDriver} instance
 *         and frees the reserved resources.
 *     </li>
 * </ol>
 */
class SeleniumWebDriverExtension extends AbstractAnnotationDrivenExtension<SeleniumTest> {

    @Override
    void visitSpecAnnotation(SeleniumTest annotation, SpecInfo spec) {
        spec.addSharedInitializerInterceptor(new SeleniumWebDriverInitializer(annotation.driver()))
        spec.addCleanupSpecInterceptor(new SeleniumWebDriverCleanUp())
    }
}
