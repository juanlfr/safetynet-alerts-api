package com.safetynet.safetynetalertsapi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.utils.SafetyAlertsNetUtil;

public class SafetyAlertsNetUtilTest {
	private MedicalRecord medicalRecord;

	@Test
	public void ageCalculatorTest() {

		medicalRecord = new MedicalRecord();
		medicalRecord.setBirthdate("01/01/2000");

		int age = SafetyAlertsNetUtil.ageCalculator(medicalRecord);

		assertThat(age).isEqualTo(21);

	}

	@Test
	public void splitNameTest() {
		String fullName = " Juan Luis ";
		String[] nameSplitted = SafetyAlertsNetUtil.splitName(fullName);
		String[] nameSplittedToCompare = { "Juan", "Luis" };
		assertThat(nameSplitted.equals(nameSplittedToCompare));
	}
}
