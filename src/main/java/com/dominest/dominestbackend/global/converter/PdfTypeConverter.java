package com.dominest.dominestbackend.global.converter;

import com.dominest.dominestbackend.api.resident.util.PdfType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class PdfTypeConverter implements Converter<String, PdfType> {
    @Override
    public PdfType convert(String source) {
        return PdfType.from(source);
    }
}
