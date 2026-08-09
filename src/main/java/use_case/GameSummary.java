package use_case;

import java.util.UUID;

/**
 * A DTO that stores simple game information
 */
public record GameSummary(UUID id, String timeCreated, String gameResult) {

}
