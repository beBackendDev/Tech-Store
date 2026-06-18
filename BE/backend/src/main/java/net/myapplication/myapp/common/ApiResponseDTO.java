package net.myapplication.myapp.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class ApiResponseDTO<T> {
    private String status;
    private String message;
    private T response;
}
