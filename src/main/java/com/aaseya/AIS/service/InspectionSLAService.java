package com.aaseya.AIS.service;

import com.aaseya.AIS.dao.InspectionTypeDAO;
import com.aaseya.AIS.dao.InspectionSLADAO;
import com.aaseya.AIS.dto.AISResponseDTO;
import com.aaseya.AIS.dto.InspectionTypeSLADTO;
import com.aaseya.AIS.dto.InspectionTypeSLADTO.SLAEntityDetails;
import com.aaseya.AIS.Model.Inspection_SLA;
import com.aaseya.AIS.Model.Inspection_Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InspectionSLAService {

	@Autowired
	private InspectionTypeDAO inspectionTypeDAO;

	@Autowired
	private InspectionSLADAO inspectionSLADAO;

	public AISResponseDTO createInspectionSLA(String inspectionTypeName,
			Map<String, InspectionTypeSLADTO.SLAEntityDetails> entitySizes, String action) {
		AISResponseDTO response = new AISResponseDTO();

// Fetch the inspection type by name
		Inspection_Type inspectionType = inspectionTypeDAO.getInspectionTypeByName(inspectionTypeName);

		if (inspectionType == null) {
			response.setStatus("FAILURE");
			response.setMessage("Inspection Type not found.");
			return response;
		}

		if ("save".equalsIgnoreCase(action)) {
// Save action logic
			entitySizes.forEach((entitySize, slaDetails) -> {
				boolean exists = inspectionSLADAO.existsByInspectionTypeAndEntitySize(inspectionType, entitySize);
				if (!exists) {
					Inspection_SLA inspectionSLA = createNewInspectionSLA(inspectionType, entitySize, slaDetails);
					inspectionSLADAO.saveInspectionSLA(inspectionSLA);
				}
			});
			response.setStatus("SUCCESS");
			response.setMessage("Inspection SLA saved successfully.");
		} else if ("edit".equalsIgnoreCase(action)) {
// Edit action logic
			entitySizes.forEach((entitySize, slaDetails) -> {
				Inspection_SLA existingSLA = inspectionSLADAO.findByInspectionTypeAndEntitySize(inspectionType,
						entitySize);
				if (existingSLA != null) {
					updateInspectionSLA(existingSLA, slaDetails);
					inspectionSLADAO.saveInspectionSLA(existingSLA);
				}
			});
			response.setStatus("SUCCESS");
			response.setMessage("Inspection SLA updated successfully.");
		} else {
			response.setStatus("FAILURE");
			response.setMessage("Invalid action. Use 'save' or 'edit'.");
		}

		return response;
	}

	private Inspection_SLA createNewInspectionSLA(Inspection_Type inspectionType, String entitySize,
			SLAEntityDetails slaDetails) {
		Inspection_SLA inspectionSLA = new Inspection_SLA();
		inspectionSLA.setInspectionType(inspectionType);
		inspectionSLA.setEntitySize(entitySize);
		updateInspectionSLA(inspectionSLA, slaDetails);
		return inspectionSLA;
	}

	private void updateInspectionSLA(Inspection_SLA inspectionSLA, SLAEntityDetails slaDetails) {
		inspectionSLA.setInspectorGoal(slaDetails.getInspectorGoal());
		inspectionSLA.setInspectorDeadline(slaDetails.getInspectorDeadline());
		inspectionSLA.setReviewerGoal(slaDetails.getReviewerGoal());
		inspectionSLA.setReviewerDeadline(slaDetails.getReviewerDeadline());
		inspectionSLA.setApproverGoal(slaDetails.getApproverGoal());
		inspectionSLA.setApproverDeadline(slaDetails.getApproverDeadline());
	}
}
