package com.springmvc.jackson;

public enum SerializerFeature {

    WriteNullListAsEmpty,
    WriteNullStringAsEmpty,
    WriteNullNumberAsZero,
    WriteNullBooleanAsFalse;

    public final int mask;

    SerializerFeature() {
        mask = (1 << ordinal());
    }
}
