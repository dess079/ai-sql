package dev.sd.aisqldemo;

import java.util.List;

/**
 * Provider metadata returned by the models endpoint.
 */
public record AIProviderResponse(String key, String label, boolean enabled, List<AIModelResponse> models) {
}
