package io.openleap.mrs.message.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ValidationService {

    private final ObjectMapper objectMapper;
    private final Map<Schema, JsonSchema> jsonSchemas;
    private static final JsonSchemaFactory SCHEMA_FACTORY =
            JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);

    public ValidationService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.jsonSchemas = loadSchemas();
    }

    private Map<Schema, JsonSchema> loadSchemas() {
        return Arrays.stream(Schema.values())
                .collect(Collectors.toUnmodifiableMap(
                        Function.identity(),
                        this::loadSchema
                ));
    }

    private JsonSchema loadSchema(Schema schema) {
        String resourcePath = "classpath:schemas/" + schema.schemaName();
        Resource resource = loadResource(resourcePath);

        try (InputStream schemaStream = resource.getInputStream()) {
            return SCHEMA_FACTORY.getSchema(schemaStream);
        } catch (IOException e) {
            throw new SchemaLoadingException(
                    "Failed to load schema: " + schema.name(), e);
        }
    }

    private Resource loadResource(String path) {
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver()
                    .getResources(path);

            if (resources.length == 0) {
                throw new SchemaLoadingException(
                        "No resources found at: " + path);
            }
            return resources[0];
        } catch (IOException e) {
            throw new SchemaLoadingException(
                    "Error loading resource: " + path, e);
        }
    }

    // Validation methods remain unchanged
    public <T> Set<ValidationMessage> validateObject(Schema schema, T object) {
        JsonNode node = objectMapper.convertValue(object, JsonNode.class);
        return validateJsonNode(schema, node);
    }

    public Set<ValidationMessage> validateJsonNode(Schema schema, JsonNode jsonNode) {
        JsonSchema jsonSchema = jsonSchemas.get(schema);
        if (jsonSchema == null) {
            throw new IllegalArgumentException("Unsupported schema type: " + schema);
        }
        return jsonSchema.validate(jsonNode);
    }

    private static class SchemaLoadingException extends RuntimeException {
        SchemaLoadingException(String message, Throwable cause) {
            super(message, cause);
        }

        SchemaLoadingException(String message) {
            super(message);
        }
    }
}
