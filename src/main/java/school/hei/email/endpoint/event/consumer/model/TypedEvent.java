package school.hei.email.endpoint.event.consumer.model;

import school.hei.email.PojaGenerated;
import school.hei.email.endpoint.event.model.PojaEvent;

@PojaGenerated
public record TypedEvent(String typeName, PojaEvent payload) {}
