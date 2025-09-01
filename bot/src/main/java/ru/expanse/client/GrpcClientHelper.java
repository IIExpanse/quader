package ru.expanse.client;

import io.grpc.Metadata;
import io.quarkus.grpc.GrpcClientUtils;

import java.util.Map;
import java.util.function.BiFunction;

public class GrpcClientHelper {
    private GrpcClientHelper() {
    }

    public static <C, R, O> O callWithHeaders(C client, R request, BiFunction<C, R, O> action, Map<String, String> headers) {
        return action.apply(GrpcClientUtils.attachHeaders(client, toMetadata(headers)), request);
    }

    private static Metadata toMetadata(Map<String, String> headers) {
        Metadata metadata = new Metadata();
        headers.forEach(
                (key, value) -> metadata.put(Metadata.Key.of(key, Metadata.ASCII_STRING_MARSHALLER), value)
        );
        return metadata;
    }
}
