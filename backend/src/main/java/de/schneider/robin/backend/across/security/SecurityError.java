package de.schneider.robin.backend.across.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SecurityError {

    String path;
    String errorCode;
    String errorMessage;

}