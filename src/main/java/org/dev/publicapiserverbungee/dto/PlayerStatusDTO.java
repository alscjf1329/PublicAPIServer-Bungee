package org.dev.publicapiserverbungee.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerStatusDTO {

    private final Map<String, Boolean> statuses;
}