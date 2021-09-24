package com.safetynet.safetynetalertsapi.model.DTO;

import java.util.List;

public class StationNumberDTO {

	private List<PersonCoveredByStationNumberDTO> peopleCoveredByStationNumber;
	private int numberOfChildren;
	private int numberOfAdults;

	public StationNumberDTO() {
	}

	public List<PersonCoveredByStationNumberDTO> getPeopleCoveredByStationNumberDTO() {
		return peopleCoveredByStationNumber;
	}

	public void setPeopleCoveredByStationNumberDTO(
			List<PersonCoveredByStationNumberDTO> personCoveredByStationNumberDTO) {
		this.peopleCoveredByStationNumber = personCoveredByStationNumberDTO;
	}

	public int getNumberOfChildren() {
		return numberOfChildren;
	}

	public void setNumberOfChildren(int numberOfChildren) {
		this.numberOfChildren = numberOfChildren;
	}

	public int getNumberOfAdults() {
		return numberOfAdults;
	}

	public void setNumberOfAdults(int numberOfAdults) {
		this.numberOfAdults = numberOfAdults;
	}

}
