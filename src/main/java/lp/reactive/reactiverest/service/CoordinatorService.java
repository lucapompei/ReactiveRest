package lp.reactive.reactiverest.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.eventbus.EventBus;

import lp.reactive.reactiverest.model.EventResponse;

/**
 * This service represents the reactive coordinator the handles the event bus
 *
 * @author lucapompei
 */
public class CoordinatorService {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LogManager.getFormatterLogger(CoordinatorService.class);

	/**
	 * The Google implementation event bus, usable to catch and dispatch events on
	 * bus
	 */
	private EventBus eventBus;

	/**
	 * This method provides access to {@link EventBus}, initializing it if
	 * necessary
	 *
	 * @return a {@link EventBus}
	 */
	public EventBus getEventBus() {
		// lazy initialization
		if (eventBus == null) {
			LOGGER.debug("Event bus lazy initialization");
			eventBus = new EventBus();
		}
		return eventBus;
	}

	/**
	 * Register a generic {@link Object} subscribers to receive events
	 *
	 * @param object,
	 *            to object to be registered to receive events
	 */
	public void register(Object object) {
		if (object == null) {
			LOGGER.error("Unable to register null object");
			return;
		}
		LOGGER.debug("Registering an object to event bus subscribers");
		getEventBus().register(object);
	}

	/**
	 * Register multiple generic {@link Object} subscribers to receive events
	 *
	 * @param objects,
	 *            the list of object to be registered to receive events
	 */
	public void register(List<Object> objects) {
		if (objects == null) {
			LOGGER.error("Unable to register null objects");
			return;
		}
		LOGGER.debug("Registering multiple objects to event bus subscribers");
		objects.forEach(o -> getEventBus().register(o));
	}

	/**
	 * Unregister a generic {@link Object} subscribers to receive events
	 *
	 * @param object,
	 *            the object to be unregistered to receive events
	 */
	public void unregister(Object object) {
		if (object == null) {
			LOGGER.error("Unable to unregister null object");
			return;
		}
		LOGGER.debug("Unregistering an object from event bus subscribers");
		getEventBus().unregister(object);
	}

	/**
	 * Unregister multiple generic {@link Object} subscribers to receive events
	 *
	 * @param objects,
	 *            the list of objects to be unregistered to receive events
	 */
	public void unregister(List<Object> objects) {
		if (objects == null) {
			LOGGER.error("Unable to unregister null objects");
			return;
		}
		LOGGER.debug("Unregistering multiple objects from event bus subscribers");
		objects.forEach(o -> getEventBus().unregister(o));
	}

	/**
	 * Post a generic {@link EventResponse} on event bus
	 *
	 * @param event,
	 *            the event to be posted on event bus
	 */
	public void post(EventResponse event) {
		if (event == null) {
			LOGGER.error("Event to post cannot be null");
			return;
		}
		LOGGER.debug("Posting a new event on event bus");
		getEventBus().post(event);
	}

	/**
	 * Check if the received event response correspond to the expected event
	 *
	 * @param receivedEvent,
	 *            the received event to check
	 * @param expectedIdentifier,
	 *            the identifier of the expected event
	 * @return a boolean indicating if the received event response correspond to the
	 *         expected event or not
	 */
	public boolean isEventForMe(EventResponse receivedEvent, String expectedIdentifier) {
		if (receivedEvent == null) {
			LOGGER.error("Received event is null");
			return false;
		}
		return receivedEvent.getIdentifier().equalsIgnoreCase(expectedIdentifier);
	}

}
