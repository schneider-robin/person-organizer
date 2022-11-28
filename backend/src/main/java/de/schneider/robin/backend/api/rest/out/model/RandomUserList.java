package de.schneider.robin.backend.api.rest.out.model;

import de.schneider.robin.backend.api.rest.out.model.RandomUser;
import lombok.Data;

import java.util.List;

@Data
public class RandomUserList {

    private List<RandomUser> results;

}
