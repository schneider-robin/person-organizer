package de.schneider.robin.backend.api.rest.in.model;

import de.schneider.robin.lib.api.model.PersonPageResponse;
import de.schneider.robin.lib.api.model.PersonResponse;

import java.util.Iterator;

public class CompletePersonPageResponse extends PersonPageResponse implements Iterable<PersonResponse> {

    @Override
    public Iterator<PersonResponse> iterator() {
        return this.getContent().iterator();
    }

    public void add(
            PersonResponse item
    ) {
        this.addContentItem(item);
    }
}
