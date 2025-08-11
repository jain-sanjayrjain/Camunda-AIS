package com.aaseya.AIS.dto;


public class EntityInspectionCasesReportResponseDTO {

	 private Long caseId;
	    private String inspectionType;
	    private String inspectionDate;
	    private String inspectionSource;
	    private String status;
		public Long getCaseId() {
			return caseId;
		}
		public void setCaseId(Long caseId) {
			this.caseId = caseId;
		}
		public String getInspectionType() {
			return inspectionType;
		}
		public void setInspectionType(String inspectionType) {
			this.inspectionType = inspectionType;
		}
		public String getInspectionDate() {
			return inspectionDate;
		}
		public void setInspectionDate(String inspectionDate) {
			this.inspectionDate = inspectionDate;
		}
		public String getInspectionSource() {
			return inspectionSource;
		}
		public void setInspectionSource(String inspectionSource) {
			this.inspectionSource = inspectionSource;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}

}
