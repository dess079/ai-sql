package dev.sd.aisqldemo;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Service that resolves configured AI providers and dynamically queries available models.
 */
@Service
public class ModelsService {

    private final RestTemplate restTemplate;
    private final String openAiBaseUrl;
    private final String openAiApiKey;
    private final String openAiDefaultModel;
    private final String githubModelsToken;
    private final String azureOpenAiApiKey;
    private final String azureOpenAiBaseUrl;
    private final boolean ollamaEnabled;
    private final OpenAiModelParser parser;

    public ModelsService(final Environment environment) {
        this.restTemplate = new RestTemplate();
        this.openAiBaseUrl = environment.getProperty("spring.ai.openai.base-url", "").trim();
        this.openAiApiKey = environment.getProperty("spring.ai.openai.api-key", "").trim();
        this.openAiDefaultModel = environment.getProperty("ai-sql.default-model", "").trim();
        this.githubModelsToken = environment.getProperty("spring.ai.openai.github-models.token", "").trim();
        this.azureOpenAiApiKey = environment.getProperty("spring.ai.azure.openai.api-key", "").trim();
        this.azureOpenAiBaseUrl = environment.getProperty("spring.ai.azure.openai.base-url", "").trim();
        this.ollamaEnabled = Boolean.parseBoolean(environment.getProperty("ai-sql.ollama.enabled", "false"));
        this.parser = new OpenAiModelParser(this.openAiDefaultModel);
    }

    public List<AIProviderResponse> listModels() {
        List<AIProviderResponse> providers = new ArrayList<>();

        providers.add(buildOpenAiProvider());
        providers.add(buildOllamaProvider());
        providers.add(buildAzureOpenAiProvider());
        providers.add(buildGitHubModelsProvider());

        return providers;
    }

    private AIProviderResponse buildOpenAiProvider() {
        boolean enabled = StringUtils.hasText(openAiApiKey) && !"ollama".equalsIgnoreCase(openAiApiKey);
        return new AIProviderResponse(
                "openai",
                "OpenAI",
                enabled,
                enabled ? fetchOpenAiModels(openAiBaseUrl, openAiApiKey, "openai") : Collections.emptyList());
    }

    private AIProviderResponse buildOllamaProvider() {
        boolean enabled = ollamaEnabled || "ollama".equalsIgnoreCase(openAiApiKey);
        return new AIProviderResponse(
                "ollama",
                "Ollama",
                enabled,
                enabled ? fetchOpenAiModels(openAiBaseUrl, openAiApiKey, "ollama") : Collections.emptyList());
    }

    private AIProviderResponse buildAzureOpenAiProvider() {
        boolean enabled = StringUtils.hasText(azureOpenAiApiKey) && StringUtils.hasText(azureOpenAiBaseUrl);
        return new AIProviderResponse(
                "azure-openai",
                "Azure OpenAI",
                enabled,
                Collections.emptyList());
    }

    private AIProviderResponse buildGitHubModelsProvider() {
        boolean enabled = StringUtils.hasText(githubModelsToken);
        return new AIProviderResponse(
                "github-models",
                "GitHub Models",
                enabled,
                Collections.emptyList());
    }

    private List<AIModelResponse> fetchOpenAiModels(final String baseUrl, final String apiKey, final String providerKey) {
        if (!StringUtils.hasText(baseUrl) || !StringUtils.hasText(apiKey)) {
            return Collections.emptyList();
        }

        try {
            String uriText = baseUrl.endsWith("/") ? baseUrl + "models" : baseUrl + "/models";
            URI uri = URI.create(uriText);

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
            return parser.parseOpenAiModelList(response.getBody(), providerKey);
        } catch (RestClientException ex) {
            return Collections.emptyList();
        }
    }
}
