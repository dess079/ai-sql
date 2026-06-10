package dev.sd.aisqldemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

/**
 * Parses OpenAI-style model list responses into application DTOs.
 */
public class OpenAiModelParser {

    private final String openAiDefaultModel;

    public OpenAiModelParser(final String openAiDefaultModel) {
        this.openAiDefaultModel = openAiDefaultModel;
    }

    @SuppressWarnings("unchecked")
    public List<AIModelResponse> parseOpenAiModelList(final Map<?, ?> body, final String providerKey) {
        if (body == null) {
            return List.of();
        }

        Object dataObj = body.get("data");
        if (!(dataObj instanceof List<?> data)) {
            return List.of();
        }

        List<AIModelResponse> models = new ArrayList<>();
        for (Object item : data) {
            if (!(item instanceof Map<?, ?> modelMap)) {
                continue;
            }

            Object idObj = modelMap.get("id");
            if (!(idObj instanceof String id)) {
                continue;
            }

            int contextWindow = parseContextWindow(modelMap);
            boolean recommended = id.equals(openAiDefaultModel);
            models.add(new AIModelResponse(id, id, providerKey, contextWindow, recommended));
        }

        return models;
    }

    private int parseContextWindow(final Map<?, ?> modelMap) {
        return firstPositiveInt(modelMap, "context_window", "context_window_size", "max_tokens", "token_limit");
    }

    private int firstPositiveInt(final Map<?, ?> map, final String... keys) {
        for (String key : keys) {
            Object value = map.get(key);
            if (value instanceof Number number) {
                int intValue = number.intValue();
                if (intValue > 0) {
                    return intValue;
                }
            }
            if (value instanceof String text) {
                try {
                    int intValue = NumberUtils.parseNumber(text, Integer.class);
                    if (intValue > 0) {
                        return intValue;
                    }
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        return 0;
    }
}
