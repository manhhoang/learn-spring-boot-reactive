package com.file_stream_concurrency_value_object.value_object;

import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface AvailableAmount {
    Double value();
    Optional<String> description();
}
