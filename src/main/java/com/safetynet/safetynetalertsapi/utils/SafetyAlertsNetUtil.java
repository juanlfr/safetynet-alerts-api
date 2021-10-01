package com.safetynet.safetynetalertsapi.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import org.springframework.http.converter.json.MappingJacksonValue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;

public final class SafetyAlertsNetUtil {

	public static int ageCalculator(MedicalRecord medicalRecordFound) {
		String stringBirthDate = medicalRecordFound.getBirthdate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate birthDate = LocalDate.parse(stringBirthDate, formatter);
		int personsAge = Period.between(birthDate, LocalDate.now()).getYears();
		return personsAge;
	}

	public static MappingJacksonValue setFiltersToFalse(Object o) {
		FilterProvider filters = new SimpleFilterProvider().setFailOnUnknownId(false);
		MappingJacksonValue responseDTO = new MappingJacksonValue(o);
		responseDTO.setFilters(filters);
		return responseDTO;
	}

	public static String toJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
