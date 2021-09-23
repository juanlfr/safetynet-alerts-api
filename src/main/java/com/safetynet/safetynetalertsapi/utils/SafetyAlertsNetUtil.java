package com.safetynet.safetynetalertsapi.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import com.safetynet.safetynetalertsapi.model.MedicalRecord;

public final class SafetyAlertsNetUtil {

	public static int ageCalculator(MedicalRecord medicalRecordFound) {
		String stringBirthDate = medicalRecordFound.getBirthdate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate birthDate = LocalDate.parse(stringBirthDate, formatter);
		int personsAge = Period.between(birthDate, LocalDate.now()).getYears();
		return personsAge;
	}

}
