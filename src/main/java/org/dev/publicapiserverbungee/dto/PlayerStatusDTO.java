package org.dev.publicapiserverbungee.dto;

import java.util.HashMap;
import java.util.Map;

public record PlayerStatusDTO(Map<String, Boolean> statuses) {
    // getFieldName을 쓰지 않으면 ObjectJson으로 변환하는 경우
    // field를 찾지 못하고 null로 response하게 됨
    public Map<String, Boolean> getStatuses() {
        return new HashMap<>(statuses);
    }
}