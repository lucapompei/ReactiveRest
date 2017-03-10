package lp.reactive.reactiverest.api;

import lp.reactive.reactiverest.service.CoordinatorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class exposes the API to access to some of coordinator functions and
 * allow object to register and unregister from/to event bus
 *
 * @author lucapompei
 */
public class CoordinatorAPI {

    /**
     * Logger
     */
    private static final Logger LOGGER = LogManager.getFormatterLogger(CoordinatorAPI.class);

    /**
     * The coordinator service used to manage the event bus
     */
    private static CoordinatorService coordinatorService;

    /**
     * Enable access the coordinator service to allow usage of coordinator
     * functions, initializing it if necessary
     *
     * @return the {@link CoordinatorService}
     */
    public static CoordinatorService getCoordinator() {
        // lazy initialization
        if (coordinatorService == null) {
            LOGGER.debug("Coordinator lazy initialization");
            coordinatorService = new CoordinatorService();
        }
        return coordinatorService;
    }

}
