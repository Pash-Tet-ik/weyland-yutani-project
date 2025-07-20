package com.weyland.yutani.core.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommandDto {

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 1000, message = "Description must be up to 1000 characters")
    private String description;

    @NotNull(message = "Priority must be provided")
    private CommandPriority priority;

    @NotBlank(message = "Author cannot be blank")
    @Size(max = 100, message = "Author must be up to 100 characters")
    private String author;

    @NotBlank(message = "Time cannot be blank")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})$", message = "Time must be in ISO-8601 format")
    private String time;
}
