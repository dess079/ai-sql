package dev.sd.aisqldemo;

/**
 * Model metadata returned by the models endpoint.
 */
public record AIModelResponse(String id, String label, String provider, int contextWindow, boolean recommended) {
}
