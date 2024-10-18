package org.service.kafka;

import org.apache.kafka.common.serialization.Deserializer;

import java.nio.ByteBuffer;
import java.util.Map;

public class LongDeserializer implements Deserializer<Long> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public Long deserialize(String topic, byte[] data) {
        if (data == null || data.length != Long.BYTES) {
            return null;
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        return buffer.getLong();
    }

    @Override
    public void close() {
    }
}
