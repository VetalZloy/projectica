package com.vetalzloy.projectica.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * This class transforms LocalDateTime to Timestamp and vise versa. 
 * It's necessary, because databases can handle only Timestamps, 
 * but Timestamp is not very convenient class, 
 * and LocalDateTime more modern and powerful. 
 * Class is mostly used by model package classes.
 * @author VetalZloy
 *
 */
@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
	
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime locDateTime) {
    	return (locDateTime == null ? null : Timestamp.valueOf(locDateTime));
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
    	return (sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime());

    }
}