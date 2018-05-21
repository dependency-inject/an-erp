package com.springmvc.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.FactoryBean;

public class CustomObjectMapperFactoryBean implements FactoryBean<ObjectMapper> {

    SerializerFeature[] features;
    public CustomObjectMapperFactoryBean(SerializerFeature[] features) {
        this.features = features;
    }

    public ObjectMapper getObject() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializerFactory(objectMapper.getSerializerFactory()
                .withSerializerModifier(new JacksonSerializerModifier(features)))
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    public Class<?> getObjectType() {
        return ObjectMapper.class;
    }

    public boolean isSingleton() {
        return false;
    }
}
