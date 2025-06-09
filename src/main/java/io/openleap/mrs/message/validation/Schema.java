package io.openleap.mrs.message.validation;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Schemas.
 */
public enum Schema {

    EMAIL_MESSAGE("email.schema.json"),
    TEAMS_MESSAGE("teams.schema.json"),
    SLACK_MESSAGE("slack.schema.json"),
    TELEGRAM_MESSAGE("telegram.schema.json");


    private final String schemaName;

    private static final Map<String, Schema> map;

    static {
        map = Arrays.stream(Schema.values())
                .collect(Collectors.toUnmodifiableMap(e -> e.schemaName, e -> e));
    }

    Schema(String schema) {
        this.schemaName = schema;
    }

    public static Schema valueOfSchema(String filename) {
        return map.get(filename);
    }

    public String schemaName() {
        return schemaName;
    }


}
