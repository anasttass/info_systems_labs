package ru.stud.dto;

import ru.stud.model.RecordStatus;

public record ImportHistoryResponse(Long id, RecordStatus status, String username, Integer numberOfEntities) {}
