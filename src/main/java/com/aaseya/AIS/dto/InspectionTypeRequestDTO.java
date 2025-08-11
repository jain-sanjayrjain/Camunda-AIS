package com.aaseya.AIS.dto;

import java.util.List;

import com.aaseya.AIS.Model.Inspection_SLA;

public class InspectionTypeRequestDTO {

	private String action; // "save" or "edit"
	// private String name;
	private InspectionTypePrimaryDetailsDTO inspectionTypePrimaryDetails;
	private InspectionTypeSLADTO inspectionTypeSLA;
	private InspectionTypeEntityDTO inspectionTypeEntity;

	private List<Inspection_SLA> inspectionSLA;
	private List<EntityDetailsDTO> entityDetailsDTOs;

	public InspectionTypePrimaryDetailsDTO getInspectionTypePrimaryDetails() {
		return inspectionTypePrimaryDetails;
	}

	public void setInspectionTypePrimaryDetails(InspectionTypePrimaryDetailsDTO inspectionTypePrimaryDetails) {
		this.inspectionTypePrimaryDetails = inspectionTypePrimaryDetails;
	}

	public InspectionTypeSLADTO getInspectionTypeSLA() {
		return inspectionTypeSLA;
	}

	public void setInspectionTypeSLA(InspectionTypeSLADTO inspectionTypeSLA) {
		this.inspectionTypeSLA = inspectionTypeSLA;
	}

	public InspectionTypeEntityDTO getInspectionTypeEntity() {
		return inspectionTypeEntity;
	}

	public void setInspectionTypeEntity(InspectionTypeEntityDTO inspectionTypeEntity) {
		this.inspectionTypeEntity = inspectionTypeEntity;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<Inspection_SLA> getInspectionSLA() {
		return inspectionSLA;
	}

	public void setInspectionSLA(List<Inspection_SLA> inspectionSLA) {
		this.inspectionSLA = inspectionSLA;
	}

	public List<EntityDetailsDTO> getEntityDetailsDTOs() {
		return entityDetailsDTOs;
	}

	public void setEntityDetailsDTOs(List<EntityDetailsDTO> entityDetailsDTOs) {
		this.entityDetailsDTOs = entityDetailsDTOs;
	}

}
