package dev.sd.aisqldemo;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes AI provider metadata and dynamic model discovery for the AI-SQL frontend.
 */
@RestController
@RequestMapping("/api/ai-sql")
public class ModelsController {

    private final ModelsService modelsService;

    public ModelsController(final ModelsService modelsService) {
        this.modelsService = modelsService;
    }

    /**
     * Returns the list of configured AI providers and the models currently available.
     */
    @GetMapping("/dynamic-models")
    public List<AIProviderResponse> listModels() {
        return modelsService.listModels();
    }
}
