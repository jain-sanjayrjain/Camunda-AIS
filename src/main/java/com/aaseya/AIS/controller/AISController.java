package com.aaseya.AIS.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.aaseya.AIS.Model.Checklist_Item;
import com.aaseya.AIS.Model.ControlType;
import com.aaseya.AIS.Model.InspectionCase;
import com.aaseya.AIS.Model.InspectionPlan;
import com.aaseya.AIS.Model.PreInspectionChecklist;
import com.aaseya.AIS.Model.SaveSubmitPreInspectionChecklist;
import com.aaseya.AIS.Model.Skill;
import com.aaseya.AIS.Model.SubSegment;
import com.aaseya.AIS.Model.Zone;
import com.aaseya.AIS.dao.InspectionCaseDAO;
import com.aaseya.AIS.dto.AISResponseDTO;
import com.aaseya.AIS.dto.AllUserGroupDTO;
import com.aaseya.AIS.dto.ApproverCommentsDTO;
import com.aaseya.AIS.dto.ApproverDashboardDTO;
import com.aaseya.AIS.dto.AssignApproverDTO;
import com.aaseya.AIS.dto.AssignDTO;
import com.aaseya.AIS.dto.AssignRequestDTO;
import com.aaseya.AIS.dto.CaseSummaryDTO;
import com.aaseya.AIS.dto.CheckList_ItemDTO;
import com.aaseya.AIS.dto.ChecklistCategoriesDTO;
import com.aaseya.AIS.dto.ChecklistCategoryDTO;
import com.aaseya.AIS.dto.ChecklistDTO;
import com.aaseya.AIS.dto.ControlTypeDTO;
import com.aaseya.AIS.dto.CreateInspectionPlanRequestDTO;
import com.aaseya.AIS.dto.EntitiesInspectionTypeDTO;
import com.aaseya.AIS.dto.EntityDetailsDTO;
import com.aaseya.AIS.dto.EntityInformationDTO;
import com.aaseya.AIS.dto.EntityInspectionCasesReportResponseDTO;
import com.aaseya.AIS.dto.EntityInspectionReportDTO;
import com.aaseya.AIS.dto.EntityRegistrationDTO;
import com.aaseya.AIS.dto.EntityReportDTO;
import com.aaseya.AIS.dto.EntityRequestDTO;
import com.aaseya.AIS.dto.EntityResponseDTO;
import com.aaseya.AIS.dto.GetAllInspection_TypeDTO;
import com.aaseya.AIS.dto.GetAllSkillDTO;
import com.aaseya.AIS.dto.InspectionCaseDTO;
import com.aaseya.AIS.dto.InspectionCaseDetailsDTO;
import com.aaseya.AIS.dto.InspectionCase_EntityDTO;
import com.aaseya.AIS.dto.InspectionEscalationDTO;
import com.aaseya.AIS.dto.InspectionFilters;
import com.aaseya.AIS.dto.InspectionHistoryDTO;
import com.aaseya.AIS.dto.InspectionPlanDTO;
import com.aaseya.AIS.dto.InspectionTypeAdminSkillDTO;
import com.aaseya.AIS.dto.InspectionTypeDTO;
import com.aaseya.AIS.dto.InspectionTypeGetAdminDTO;
import com.aaseya.AIS.dto.InspectionTypeIdDTO;
import com.aaseya.AIS.dto.InspectionTypePrimaryDetailsDTO;
import com.aaseya.AIS.dto.InspectionTypeRequestDTO;
import com.aaseya.AIS.dto.InspectionTypeSLADTO;
import com.aaseya.AIS.dto.InspectionTypeSkillAdminDTO;
import com.aaseya.AIS.dto.InspectorSourceStatusCountDTO;
import com.aaseya.AIS.dto.LoginRequestDTO;
import com.aaseya.AIS.dto.PreInspectionChecklistDTO;
import com.aaseya.AIS.dto.PreInspectionChecklistResponseDTO;
import com.aaseya.AIS.dto.ResponseDTO;
import com.aaseya.AIS.dto.SaveInspectionDTO;
import com.aaseya.AIS.dto.SavePreInspectionDTO;
import com.aaseya.AIS.dto.SavedPreInspectionDTO;
import com.aaseya.AIS.dto.SegmentDTO;
import com.aaseya.AIS.dto.SelectedEntityDTO;
import com.aaseya.AIS.dto.SkillDTO;
import com.aaseya.AIS.dto.SkillDetailDTO;
import com.aaseya.AIS.dto.SkillInspectionTypeDTO;
import com.aaseya.AIS.dto.SkillRequestDTO;
import com.aaseya.AIS.dto.SkillsDTO;
//import com.aaseya.AIS.dto.SkillInspectionTypeDTO;
import com.aaseya.AIS.dto.StartAISRequestDTO;
import com.aaseya.AIS.dto.StatusCountResponseDTO;
import com.aaseya.AIS.dto.SubmitInspectionDTO;
import com.aaseya.AIS.dto.TempCheckInspDTO;
import com.aaseya.AIS.dto.TemplateDTO;
import com.aaseya.AIS.dto.TemplateDetailsDTO;
import com.aaseya.AIS.dto.TemplateResponseDTO;
import com.aaseya.AIS.dto.TopTenNegativeObservationsDTO;
import com.aaseya.AIS.dto.UpdateIsActiveDTO;
import com.aaseya.AIS.dto.UserGroupDTO;
import com.aaseya.AIS.dto.UserSkillDTO;
import com.aaseya.AIS.dto.UsersDTO;
import com.aaseya.AIS.dto.UsersGroupDTO;
import com.aaseya.AIS.dto.UsersRoleDTO;
import com.aaseya.AIS.dto.ZoneDTO;
import com.aaseya.AIS.dto.ZoneUserDTO;
import com.aaseya.AIS.dto.addChecklistitemsDTO;
import com.aaseya.AIS.service.AISService;
import com.aaseya.AIS.service.CategoriesSummaryReportService;
import com.aaseya.AIS.service.ChecklistCategoryService;
import com.aaseya.AIS.service.ChecklistItemService;
import com.aaseya.AIS.service.ChecklistService;
import com.aaseya.AIS.service.ControlTypeService;
import com.aaseya.AIS.service.EntityInspectionReportService;
import com.aaseya.AIS.service.InspectionCaseService;
import com.aaseya.AIS.service.InspectionEscalationService;
import com.aaseya.AIS.service.InspectionMappingService;
import com.aaseya.AIS.service.InspectionPlanService;
import com.aaseya.AIS.service.InspectionSLAService;
import com.aaseya.AIS.service.InspectionService;
import com.aaseya.AIS.service.InspectionTypeService;
import com.aaseya.AIS.service.NewEntityService;
import com.aaseya.AIS.service.PreInspectionChecklistService;
import com.aaseya.AIS.service.SaveSubmitPreInspectionChecklistService;
import com.aaseya.AIS.service.SegmentService;
import com.aaseya.AIS.service.SkillService;
import com.aaseya.AIS.service.SummaryGeneratorService;
import com.aaseya.AIS.service.TempCheckInspService;
import com.aaseya.AIS.service.TemplateService;
import com.aaseya.AIS.service.UpdateIsActiveService;
import com.aaseya.AIS.service.UserGroupForEditByIdService;
import com.aaseya.AIS.service.UserGroupService;
import com.aaseya.AIS.service.UserSkillService;
import com.aaseya.AIS.service.UsersService;
import com.aaseya.AIS.service.ZoneService;
import com.aaseya.AIS.service.getGroupCasesForLeadService;

import io.camunda.zeebe.client.ZeebeClient;
import jakarta.persistence.EntityNotFoundException;

@CrossOrigin("*")
@RestController
public class AISController {

	@Autowired
	private ZeebeClient zeebeClient;

	@Autowired
	private ZoneService zoneService;

	@Autowired
	private InspectionMappingService inspectionMappingService;

	@Autowired
	private UserGroupForEditByIdService userGroupForEditByIdService;
	@Autowired
	private UsersService usersService;

	@Autowired
	private SegmentService segmentService;

	@Autowired
	private InspectionSLAService inspectionSLAService;

	@Autowired
	private InspectionTypeService inspectionTypeService;

	@Autowired
	private NewEntityService newEntityService;

	@Autowired
	private TemplateService templateService;

	@Autowired
	private com.aaseya.AIS.service.TaskListService tasklistservice;

	@Autowired
	private InspectionCaseService inspectionCaseService;

	@Autowired
	private AISService aisService;

	@Autowired
	private ChecklistService checklistService;

	@Autowired
	private PreInspectionChecklistService preInspectionChecklistService;

	@Autowired
	private InspectionService inspectionService;

	@Autowired
	private SaveSubmitPreInspectionChecklistService saveSubmitPreInspectionChecklistService;

	@Autowired
	private InspectionCaseDAO inspectionCaseDAO;

	@Autowired
	private SummaryGeneratorService summaryGeneratorService;

	@Autowired
	private SkillService skillsService;

	@Autowired
	private ChecklistItemService checklistItemService;

	@Autowired
	private ChecklistCategoryService checklistCategoryService;

	@Autowired
	private TempCheckInspService tempCheckInspService;

	@Autowired
	private UserSkillService userSkillService;

	@Autowired
	private UsersService userService;

	@Autowired
	private UpdateIsActiveService updateIsActiveService;

	@Autowired
	private EntityInspectionReportService entityInspectionReportService;

	@Autowired
	private CategoriesSummaryReportService categoriesSummaryReportService;

	@Autowired
	private ControlTypeService controlTypeService;

	@Autowired
	private UserGroupService userGroupService;

	@Autowired
	private getGroupCasesForLeadService getGroupCasesForLeadService;

	@Autowired
	private InspectionPlanService inspectionPlanService;

	@PostMapping("/addZone")
	public ResponseDTO addZone(@RequestBody Zone zone) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			zoneService.addZone(zone);
			responseDTO.setStatus("Success");
			responseDTO.setMessage("Zone created");
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatus("Failure");
			responseDTO.setErrorCode("500");
			responseDTO.setErrorMessage(e.getMessage());
		}
		return responseDTO;
	}

/////////////add users in usertable
	@PostMapping("/addUsers")
	public ResponseDTO addUsers(@RequestBody UsersDTO users) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			usersService.addUsers(users);
			responseDTO.setStatus("Success");
			responseDTO.setMessage("Users created");
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatus("Failure");
			responseDTO.setErrorCode("500");
			responseDTO.setErrorMessage(e.getMessage());
		}
		return responseDTO;
	}

//	@GetMapping("/getUsers") 
//	public ResponseEntity<List<Users>>getAllUsers() {        
//	List<Users> users = usersService.getAllUsers();       
//	return ResponseEntity.ok(users); 
//	}

	//// get users details
//	@GetMapping("/getUsers")
//	public List<UsersDTO> getAllUsers() {
//		return usersService.getAllUsers();
//	}

	@GetMapping("/getInspectionTypeNames")
	public List<String> getAllInspectionType() {
		return inspectionTypeService.getInspectionTypeNames();
	}

	@PostMapping("/addSubSegment")
	public ResponseDTO addSubSegment(@RequestBody SubSegment subSegment) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			segmentService.addSubSegment(subSegment);
			responseDTO.setStatus("Success");
			responseDTO.setMessage("SubSegment created");
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatus("Failure");
			responseDTO.setErrorCode("500");
			responseDTO.setErrorMessage(e.getMessage());
		}
		return responseDTO;
	}

	@GetMapping("/getEntityNames")
	public ResponseEntity<List<EntitiesInspectionTypeDTO>> getEntities(
			@PathVariable(value = "ins_type_id", required = false) Long insTypeId) {
		List<EntitiesInspectionTypeDTO> response = newEntityService.fetchEntities(insTypeId);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/addSegment")
	public ResponseDTO addSegment(@RequestBody SegmentDTO segmentDTO) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			segmentService.addSegment(segmentDTO);
			responseDTO.setStatus("Success");
			responseDTO.setMessage("Segment created");
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatus("Failure");
			responseDTO.setErrorCode("500");
			responseDTO.setErrorMessage(e.getMessage());
		}
		return responseDTO;
	}

	@GetMapping("/getMycasesForManager/{createdBy}/{dueDateFilter}")
	public List<InspectionCase_EntityDTO> getMycasesForManager(@PathVariable("createdBy") String createdBy,
			@PathVariable("dueDateFilter") String dueDateFilter) {
		return newEntityService.getMycasesForManager(createdBy, dueDateFilter);
	}

	@GetMapping("/getUnassignedCasesForManager/{createdBy}/status/new")
	public List<InspectionCase_EntityDTO> getEntitiesByCreatedByAndStatusNew(
			@PathVariable("createdBy") String createdBy) {
		return newEntityService.getEntitiesByCreatedByAndStatus(createdBy, "new");
	}

	@GetMapping("/getTemplate/{inspectionTypeName}")
	public List<TemplateDTO> getTemplateNameAndVersionByInspectionTypeName(@PathVariable String inspectionTypeName) {
		return templateService.getTemplateNameAndVersionByInspectionTypeName(inspectionTypeName);
	}

	@GetMapping("/getInspectorPool/{date}")
	public Map<String, List<InspectionCase_EntityDTO>> getInspectorPool(@PathVariable("date") LocalDate date) {
		return inspectionCaseService.getInspectorPool(date);
	}

	@GetMapping("/getMycasesForInspector/{inspectorEmailId}")
	public List<InspectionCase_EntityDTO> getMycasesForInspector(
			@PathVariable("inspectorEmailId") String inspectorEmailId) {
		return inspectionCaseService.getMycasesForInspector(inspectorEmailId);
	}

	@GetMapping("/getUnassignedcasesForInspector/{inspectorEmailId}")
	public List<InspectionCase_EntityDTO> getUnasssignedcasesForInspector(
			@PathVariable("inspectorEmailId") String inspectorEmailId) {
		return inspectionCaseService.getUnasssignedcasesForInspector(inspectorEmailId);
	}

//	@GetMapping("/getAddressByEntity/{name}")
//	public EntityInformationDTO getAllAddressByEntity(@PathVariable String name) {
//		return newEntityService.getAddressByEntity(name);
//	}

	// assigning Task to user
	@PostMapping("/assignInspection")
	public String assignInspection(@RequestBody AssignDTO assignDTO) {
	    String status = "Success";
	    try {
	        String processId = assignDTO.getInspectionId().toString();
	        String taskId = tasklistservice.getActiveTaskID(processId);

	        if (assignDTO.getInspectorId() != null && !assignDTO.getInspectorId().isEmpty()) {
	            // Assign inspector
	            tasklistservice.assignTask(assignDTO.getInspectionId().toString(), taskId, assignDTO.getInspectorId());
	            zeebeClient.newSetVariablesCommand(assignDTO.getInspectionId())
				.variables(new HashMap<String, Object>() {
					{
						put("inspectorId", assignDTO.getInspectorId());
					}
				}).send().join();

	            // Update the inspection case with inspectorId
	            InspectionCase inspectionCase = inspectionCaseService.updateInspectionCase(assignDTO.getInspectionId(),
	                    assignDTO.getInspectorId(), "pending", null);
	            inspectionCase.setCaseCreationType("individual");
	            inspectionCaseDAO.updateInspectionCase(inspectionCase);

	            //Save Inspection Mapping for inspectorId
	            inspectionMappingService.savesInspectionMapping(assignDTO.getInspectionId(), assignDTO.getInspectorId());
	        }
	        else if (assignDTO.getLeadId() != null) {
	            // Assign leadId
	            tasklistservice.assignTask(assignDTO.getInspectionId().toString(), taskId, null);
	            zeebeClient.newSetVariablesCommand(assignDTO.getInspectionId())
				.variables(new HashMap<String, Object>() {
					{
						put("leadId", assignDTO.getLeadId());

					}
				}).send().join();

	            // Update the inspection case with leadId and groupId
	            InspectionCase inspectionCase = inspectionCaseService.updateInspectionCase(assignDTO.getInspectionId(),
	                    null, "pending", null);
	            inspectionCase.setLeadId(assignDTO.getLeadId());
	            inspectionCase.setGroupId(assignDTO.getGroupId());
	            inspectionCase.setCaseCreationType("group");
	            inspectionCaseDAO.updateInspectionCase(inspectionCase);
	        }
	        else {
	            status = "Failure: Either inspectorId or both leadId and groupId must be provided.";
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        status = "Failure";
	    }
	    return status;
	}

	@PostMapping("/startAISProcess")
	public AISResponseDTO stratAISProcess(@RequestBody StartAISRequestDTO startAISRequest) {
		AISResponseDTO startAISResponseDTO = new AISResponseDTO();
		try {
			String businessKey = aisService.startAISProcess(startAISRequest);
			startAISResponseDTO.setStatus("Success");
			startAISResponseDTO.setMessage("AIS process started successful.");
			startAISResponseDTO.setBusinessKey(businessKey);
			System.out.println(startAISResponseDTO);
			// store if new entity store in database
			if (startAISRequest.isAddEntity()) {
				String entityId = newEntityService.saveEntity(startAISRequest.getNewEntity());
				startAISRequest.setEntityId(entityId);
			}
			// Strore the inspection details in database
			boolean result = inspectionCaseService.saveInspectionCase(startAISRequest, businessKey);
		} catch (Exception e) {
			e.printStackTrace();
			startAISResponseDTO.setStatus("Failure");
			startAISResponseDTO.setMessage("Process is not started =" + e.getMessage());
		}
		return startAISResponseDTO;
	}

	///// Re-inspection

	@GetMapping("/managerPool/{createdBy}/{dateFilter}")
	public List<InspectionCase_EntityDTO> getManagerpoolbyDate(@PathVariable("createdBy") String createdBy,
			@PathVariable("dateFilter") String dateFilter) {
		try {
			return inspectionCaseService.getManagerPool(createdBy, dateFilter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<InspectionCase_EntityDTO>();
	}

	// To get individual status names and its count and total count of status for
	// particular createdby//
	@GetMapping("/getStatusCountForManager/{createdBy}")
	public ResponseEntity<StatusCountResponseDTO> getStatusCountByCreatedBy(
			@PathVariable("createdBy") String createdBy) {
		try {
			StatusCountResponseDTO statusCountResponseDTO = newEntityService.getStatusCountByCreatedBy(createdBy);
			return ResponseEntity.ok(statusCountResponseDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
	}
	//////////////////////////////////////////////////////////////////////////

	// To get count and names of status, Inspector source based on createBy//
	@GetMapping("/getInspectorSourceCountForManager/{createdBy}")
	public ResponseEntity<List<InspectorSourceStatusCountDTO>> getStatusAndInspectorSourceCount(
			@PathVariable("createdBy") String createdBy) {
		try {
			List<InspectorSourceStatusCountDTO> responseList = newEntityService
					.getStatusAndInspectorSourceCount(createdBy);
			return ResponseEntity.ok(responseList);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
	}

	////////////////////////////////////////////////////////
	// To get individual status names and its count and total count of status for
	//////////////////////////////////////////////////////// particular
	//////////////////////////////////////////////////////// inspectorID//
	@GetMapping("/getStatusCountForInspector/{inspectorID}")
	public StatusCountResponseDTO getStatusCountByInspectorID(@PathVariable("inspectorID") String inspectorID) {
		return newEntityService.getStatusCountByInspectorID(inspectorID);
	}

	///////////////////////////////////////////////////////////
	// To get count and names of status, Inspector source based on inspectorID//
	@GetMapping("/getInspectorSourceCountForInspector/{inspectorID}")
	public List<InspectorSourceStatusCountDTO> getStatusAndInspectorSourceCountByInspectorID(
			@PathVariable("inspectorID") String inspectorID) {
		return newEntityService.getStatusAndInspectorSourceCountByInspectorID(inspectorID);
	}
	///////////////////////////////////////////

	@GetMapping("/getInspectionChecklist/{inspectionId}")
	public List<ChecklistDTO> getInspectionChecklist(@PathVariable("inspectionId") String inspectionId) {
		List<ChecklistDTO> checklist = new ArrayList<ChecklistDTO>();
		checklist = checklistService.getInspectionChecklist(inspectionId);
		return checklist;
	}

	@PostMapping("/saveInspection")
	public ResponseDTO saveInspection(@RequestBody SaveInspectionDTO inspectionDTO) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			System.out.println(inspectionDTO);
			long insectionId = inspectionDTO.getInspectionChecklistandAnswers().get(0).getId().getInspectionID();
			inspectionCaseService.saveInspectionAnswers(inspectionDTO);

			// Update the overall comments
			InspectionCase inspectionCase = inspectionCaseService.updateInspectionComments(insectionId,
					inspectionDTO.getInspectorComment(), inspectionDTO.getReviewerComment(),
					inspectionDTO.getApproverComment());
			// updating InspectorId and changing status to pending in Database
			inspectionCase = inspectionCaseService.updateInspectionCase(insectionId, "", "in_progress", inspectionCase);
			inspectionCaseDAO.updateInspectionCase(inspectionCase);
			responseDTO.setStatus("Success");
			responseDTO.setMessage("Inspection saved successfully!!");
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatus("Failure");
			responseDTO.setErrorMessage(e.getMessage());
		}
		return responseDTO;

	}

	@GetMapping("/getSubmittedInspection/{inspectionId}")
	public SubmitInspectionDTO getSubmittedInspection(
	        @PathVariable("inspectionId") String inspectionId,
	        @RequestParam(value = "inspectorId", required = false) String inspectorId) {
	    SubmitInspectionDTO saveInspectionDTO = new SubmitInspectionDTO();
	    try {
	        saveInspectionDTO = inspectionCaseService.getSubmittedInspection(Long.parseLong(inspectionId), inspectorId);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return saveInspectionDTO;
	}

	@PostMapping("/submitInspection/{inspectionId}")
	public ResponseDTO submitInspection(@PathVariable("inspectionId") String inspectionId,
			@RequestBody SaveInspectionDTO inspectionDTO) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			String taskId = tasklistservice.getActiveTaskID(inspectionId);
			tasklistservice.CompleteTaskByID(taskId, new HashMap<String, Object>() {
				{
					put("checklistFilled", "true");
				}
			});

			inspectionCaseService.saveInspectionAnswers(inspectionDTO);

			// Update the overall comments
			InspectionCase inspectionCase = inspectionCaseService.updateInspectionComments(Long.parseLong(inspectionId),
					inspectionDTO.getInspectorComment(), inspectionDTO.getReviewerComment(),
					inspectionDTO.getApproverComment());

			// updating InspectorId and changing status to pending in Database
			inspectionCase = inspectionCaseService.updateInspectionCase(Long.parseLong(inspectionId), "",
					"pending_review", inspectionCase);

			// Check due date and update dueDateSatisfiedByInspector
			boolean dueDateSatisfied = inspectionCaseService.isDueDateSatisfied(Long.parseLong(inspectionId));
			inspectionCase.setDueDateSatisfiedByInspector(dueDateSatisfied);
			inspectionCaseDAO.updateInspectionCase(inspectionCase);

			responseDTO.setStatus("Success");
			responseDTO.setMessage("Inspection checklist submitted.");
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatus("Failure");
			responseDTO.setErrorMessage(e.getMessage());
		}
		return responseDTO;
	}

	@GetMapping("/getMycasesForInspectorDate/{inspectorID}/{date}")
	public Map<String, List<InspectionCase_EntityDTO>> getMycasesForInspectorDate(
			@PathVariable("inspectorID") String inspectorID, 
			@PathVariable("date") LocalDate due_date) {
		try {
			return inspectionCaseService.getMyCasesForInspectorDate(inspectorID, due_date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<String, List<InspectionCase_EntityDTO>>();
	}

	/////// Get status count dashboard data for approver
	@GetMapping("/statusCountsByApproverID")
	public Map<String, Object> getStatusCountsByApproverID(@RequestParam String approverID) {
		List<ApproverDashboardDTO> statusCounts = newEntityService.getStatusCountsByApproverID(approverID);

		Map<String, Object> response = new HashMap<>();
		Map<String, Long> statusCountsMap = new HashMap<>();
		long totalCount = 0;

		for (ApproverDashboardDTO statusCount : statusCounts) {
			String status = statusCount.getStatus().toLowerCase();
			long count = statusCount.getCount();

			if (status.equals("pending_approval") || status.equals("under_approval")) {
				statusCountsMap.merge("pending", count, Long::sum);
			} else if (!status.equals("new") && !status.equals("in_progress")) {
				statusCountsMap.merge(status, count, Long::sum);
			}
		}

		for (long count : statusCountsMap.values()) {
			totalCount += count;
		}

		response.put("statusCounts", statusCountsMap);
		response.put("totalCount", totalCount);

		return response;
	}
	////// Get the pre-inspection checklist for inspection type

	@GetMapping("/getAllPreInspectionChecklists")
	public List<PreInspectionChecklist> getAllPreInspectionChecklists(@RequestParam String name) {
		return preInspectionChecklistService.getPreInspectionChecklist(name);
	}

	///////////// Get source count for approver
	@GetMapping("/approver-source-count")
	public ResponseEntity<List<InspectorSourceStatusCountDTO>> getApproverSourceCount(@RequestParam String approverID) {
		List<InspectorSourceStatusCountDTO> result = newEntityService.getApproverSourceCountForApproverID(approverID);
		return ResponseEntity.ok(result);
	}

	////// get user details with skills based on the Inspection type
	@GetMapping("/name/{name}")
	public InspectionTypeDTO getInspectionTypeDetailsByName(@PathVariable String name) {
		return inspectionTypeService.getInspectionTypeDetailsByName(name);
	}

	////
	@GetMapping("/role/{role}")
	public List<UsersDTO> getUsersByRole(@PathVariable String role) {
		return usersService.getUsersByRole(role);
	}

	@GetMapping("/getPreInspectionChecklists")
	public PreInspectionChecklistDTO getPreInspectionChecklists(@RequestParam long inspection_case_id) {
		return preInspectionChecklistService.getPreInspectionChecklistDTO(inspection_case_id);
	}

//////////////Save  the inspection for approver////////////////////

	@PostMapping("/reviewInspection")
	public ResponseEntity<String> reviewInspection(@RequestBody ApproverCommentsDTO approverCommentRequestDTO) {
		inspectionCaseService.addReviewComments(approverCommentRequestDTO);
		return ResponseEntity.ok("Success");
	}
///////////////Save the inspection for approver//////////////////////////

	// assigning Task to Approver
	// ////////////////////////////////////////////////////////////////////
	@PostMapping("/assignInspectionToApprover")
	public String assignApproverInspection(@RequestBody AssignApproverDTO assignApproverDTO) {
		String status = "Success";
		try {
			String processId = assignApproverDTO.getInspectionId().toString();
			String taskId = tasklistservice.getActiveTaskID(processId);
			tasklistservice.assignTask(assignApproverDTO.getInspectionId().toString(), taskId,
					assignApproverDTO.getApproverId());
			zeebeClient.newSetVariablesCommand(assignApproverDTO.getInspectionId())
					.variables(new HashMap<String, Object>() {
						{
							put("approverId", assignApproverDTO.getApproverId());
						}
					}).send().join();
			// updating InspectorId and changing status to pending in Database
			inspectionCaseService.updateApproverIdinInspectionCase(assignApproverDTO.getInspectionId(),
					assignApproverDTO.getApproverId(), "pending_approval");

		} catch (Exception e) {
			e.printStackTrace();
			status = "Failure";
		}
		return status;
	}

	//////////////////////////////////////////////////////////////////////////////////

	///// fetch all zone details based on the inspectionID//
	@GetMapping("/zonedetails/{inspectionId}")
	public ResponseEntity<ZoneUserDTO> getZoneUserDetails(@PathVariable long inspectionId) {
		ZoneUserDTO zoneUserDTO = inspectionService.getZoneUserDetailsByInspectionId(inspectionId);
		if (zoneUserDTO == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(zoneUserDTO);
	}

	@PostMapping("/savesubmit")
	public ResponseEntity<List<SaveSubmitPreInspectionChecklist>> saveOrUpdateChecklists(
			@RequestBody List<SaveSubmitPreInspectionChecklist> checklists) {
		List<SaveSubmitPreInspectionChecklist> savedChecklists = checklists.stream()
				.map(saveSubmitPreInspectionChecklistService::saveOrUpdateChecklist).toList();
		return new ResponseEntity<>(savedChecklists, HttpStatus.CREATED);
	}

	@GetMapping("/getMycasesForApprover/{approverID}/{dateFilter}")
	public List<InspectionCase_EntityDTO> getMycasesForApproverByDate(@PathVariable("approverID") String approverID,
			@PathVariable("dateFilter") String dateFilter) {
		return newEntityService.getMycasesForApprover(approverID, dateFilter);
	}

	// Get approver pool data
	@GetMapping("/getApproverPool/{approverID}/{dateFilter}")
	public List<InspectionCase_EntityDTO> getApproverpoolByDate(@PathVariable("approverID") String approverID,
			@PathVariable("dateFilter") String dateFilter) {
		try {
			return inspectionCaseService.getApproverpool(approverID, dateFilter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<InspectionCase_EntityDTO>();
	}

	//////////// get Available User//////////////////
	@GetMapping("/getavailableusers/{inspectionId}")
	public List<UsersDTO> getZoneUserDetailsByInspectionId(@PathVariable long inspectionId) {
		List<UsersDTO> usersDTO = inspectionCaseService.getZoneUserDetailsByInspectionId(inspectionId);
		return usersDTO;
	}
	//////////// get Available User//////////////////

	//////// SaveSubmitPreInspection//////
	@PostMapping("/saveSubmitPreInspection")
	public ResponseEntity<List<SaveSubmitPreInspectionChecklist>> saveOrUpdateChecklists(
			@RequestBody SavePreInspectionDTO dto) {

		List<SaveSubmitPreInspectionChecklist> savedChecklists = saveSubmitPreInspectionChecklistService
				.saveOrUpdateChecklists(dto);
		return new ResponseEntity<>(savedChecklists, HttpStatus.CREATED);
	}
	/////// SavedPreInspection//////////

	@GetMapping("/savedPreInspection")
	public ResponseEntity<SavedPreInspectionDTO> getSaveSubmitPreInspectionChecklist(@RequestParam long inspectionid) {
		SavedPreInspectionDTO checklists = checklistService.getChecklistsByInspectionId(inspectionid);
		return ResponseEntity.ok(checklists);
	}

/////// Get status count dashboard data for approver
	@GetMapping("/getStatusCountsForReviewer")
	public Map<String, Object> getStatusCountsForReviewer(@RequestParam String reviewerId) {
		List<ApproverDashboardDTO> statusCounts = newEntityService.getStatusCountsForReviewer(reviewerId);

		Map<String, Object> response = new HashMap<>();
		Map<String, Long> statusCountsMap = new HashMap<>();
		long totalCount = 0;

		for (ApproverDashboardDTO statusCount : statusCounts) {
			String status = statusCount.getStatus().toLowerCase();
			long count = statusCount.getCount();

			if (status.equals("pending_review") || status.equals("under_review")) {
				statusCountsMap.merge("pending", count, Long::sum);
			} else if (!status.equals("new") && !status.equals("in_progress")) {
				statusCountsMap.merge(status, count, Long::sum);
			}
		}

		for (long count : statusCountsMap.values()) {
			totalCount += count;
		}

		response.put("statusCounts", statusCountsMap);
		response.put("totalCount", totalCount);

		return response;
	}

///////////// Get source count for reviewer
	@GetMapping("/getSourceCountForReviewer")
	public ResponseEntity<List<InspectorSourceStatusCountDTO>> getSourceCountForReviewer(
			@RequestParam String approverID) {
		List<InspectorSourceStatusCountDTO> result = newEntityService.getSourceCountForReviewer(approverID);
		return ResponseEntity.ok(result);
	}

	// assigning Task to Reviewer
	// ////////////////////////////////////////////////////////////////////
	@PostMapping("/assignInspectionToReviewer")
	public String assignInspectionToReviewer(@RequestBody AssignApproverDTO assignApproverDTO) {
		String status = "Success";
		try {
			String processId = assignApproverDTO.getInspectionId().toString();
			String taskId = tasklistservice.getActiveTaskID(processId);
			tasklistservice.assignTask(assignApproverDTO.getInspectionId().toString(), taskId,
					assignApproverDTO.getApproverId());
			zeebeClient.newSetVariablesCommand(assignApproverDTO.getInspectionId())
					.variables(new HashMap<String, Object>() {
						{
							put("reviewerId", assignApproverDTO.getApproverId());
						}
					}).send().join();
			// updating InspectorId and changing status to pending in Database
			inspectionCaseService.updateReviewerIdinInspectionCase(assignApproverDTO.getInspectionId(),
					assignApproverDTO.getApproverId(), "pending_review");

		} catch (Exception e) {
			e.printStackTrace();
			status = "Failure";
		}
		return status;
	}

	@GetMapping("/getMycasesForReviewer/{reviewerId}/{dateFilter}")
	public List<InspectionCase_EntityDTO> getMycasesForReviewerByDate(@PathVariable("reviewerId") String reviewerID,
			@PathVariable("dateFilter") String dateFilter) {
		return newEntityService.getMycasesForReviewer(reviewerID, dateFilter);
	}

	// Get approver pool data
	@GetMapping("/getReviewerPool/{reviewerId}/{dateFilter}")
	public List<InspectionCase_EntityDTO> getReviewerpoolByDate(@PathVariable("reviewerId") String reviewerID,
			@PathVariable("dateFilter") String dateFilter) {
		try {
			return inspectionCaseService.getReviewerpool(reviewerID, dateFilter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<InspectionCase_EntityDTO>();
	}
//////Co-relation message////

//////Co-relation message////
	@PostMapping("/askCopilot")
	public ResponseEntity<ResponseDTO> correlateMessage(@RequestBody Map<String, String> payload) {
		String businessKey = payload.get("businessKey");
		String prompt = payload.get("prompt");

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			// Call the service to correlate the message with businessKey and prompt
			String chatGPTResponse = aisService.correlateChatGPTMessage(businessKey, prompt);

			// Set success response
			responseDTO.setStatus("SUCCESS");
			responseDTO.setMessage("Message correlated successfully with businessKey:prompt: " + prompt);
			responseDTO.setBusinessKey(businessKey);
			responseDTO.setChatGPTResponse(chatGPTResponse); // Include the ChatGPT response

			return ResponseEntity.ok(responseDTO);
		} catch (Exception e) {
			e.printStackTrace();

			// Set failure response
			responseDTO.setStatus("FAILURE");
			responseDTO.setMessage("Message correlation failed");
			responseDTO.setErrorCode("500");
			responseDTO.setErrorMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
		}
	}

	// Log the escalation in escalation table

	@Autowired
	private InspectionEscalationService inspectionEscalationService;

	@PostMapping("/saveInspectionEscalations")
	public ResponseEntity<String> saveInspectionEscalation(@RequestBody InspectionEscalationDTO requestDTO) {
		inspectionEscalationService.saveInspectionEscalation(requestDTO);
		return ResponseEntity.ok("Inspection Escalation saved successfully.");
	}

	///// PDF report///
	@PostMapping("/generatePDFreport")
	public ResponseEntity<Void> generateReport(@RequestBody Map<String, Long> requestBody) {
		Long inspectionId = requestBody.get("inspectionId"); // Ensure you're using "inspectionId"
		summaryGeneratorService.generateAndStoreReport(inspectionId);
		return ResponseEntity.status(HttpStatus.CREATED).build(); // Return 201 Created status
	}

	@GetMapping("/generate/{id}")
	public String generateSummary(@PathVariable("id") Long id) {
		// Call the service method to generate the PDF
		summaryGeneratorService.summaryGenerator(id);
		return "PDF generated successfully for Inspection ID: " + id;
	}

	private <R> R saveOrUpdateChecklist(SaveSubmitPreInspectionChecklist savesubmitpreinspectionchecklist1) {
		return null;
	}

	@PostMapping("/addSkills")
	public ResponseEntity<Skill> createSkill(@RequestBody SkillDTO skillDTO) {
		Skill savedSkill = skillsService.saveSkill(skillDTO);
		return ResponseEntity.ok(savedSkill);
	}

	@PostMapping("/addCheckListItems")
	public ResponseEntity<ResponseDTO> saveChecklistItemAndCorrectiveActions(
			@RequestBody Checklist_Item checklist_Item) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			// Call the service to save checklist items and corrective actions
			checklistService.addCheckListItems(checklist_Item);

			// Set success response
			responseDTO.setStatus("SUCCESS");
			responseDTO.setMessage("Checklist Item and Corrective Actions saved successfully!");

			return ResponseEntity.ok(responseDTO);
		} catch (Exception e) {
			e.printStackTrace();

			// Set failure response
			responseDTO.setStatus("FAILURE");
			responseDTO.setMessage("Failed to save Checklist Item and Corrective Actions.");
			responseDTO.setErrorCode("500");
			responseDTO.setErrorMessage(e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);

		}

	}

	// addNewInspectionTypeWithSkills//
	@PostMapping("/addNewInspectionTypeWithSkills")
	public ResponseEntity<AISResponseDTO> processInspectionType(
			@RequestBody InspectionTypePrimaryDetailsDTO inspectionTypePrimaryDetailsDTO,
			@RequestParam("action") String action) {
		InspectionTypeIdDTO responseDTO = new InspectionTypeIdDTO();
		try {
			// Call the service to process the Inspection Type
			long inspectionTypeId = inspectionTypeService.processInspectionType(inspectionTypePrimaryDetailsDTO,
					action);

			// Populate common success response logic
			responseDTO.setStatus("SUCCESS");
			responseDTO.setMessage("Inspection type successfully processed.");
			responseDTO.setInspectionTypeId(inspectionTypeId);
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);

		} catch (IllegalArgumentException e) {
			// Handle specific validation errors
			responseDTO = buildErrorResponse("Invalid request: " + e.getMessage());
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			// Handle general errors
			responseDTO = buildErrorResponse("Failed to process the request: " + e.getMessage());
			return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Helper method to build an error response
	private InspectionTypeIdDTO buildErrorResponse(String errorMessage) {
		InspectionTypeIdDTO errorResponse = new InspectionTypeIdDTO();
		errorResponse.setStatus("ERROR");
		errorResponse.setMessage(errorMessage);
		return errorResponse;
	}
	// addNewInspectionTypeWithSkills//

	@PostMapping("/addChecklistCategory")
	public ResponseEntity<ResponseDTO> saveChecklistCategory(@RequestBody ChecklistCategoryDTO checklistCategoryDTO) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			// Call the service to save checklist category and items
			checklistService.addChecklistCategory(checklistCategoryDTO);

			// Set success response
			responseDTO.setStatus("SUCCESS");
			responseDTO.setMessage("Checklist Category and Items saved successfully!");

			return ResponseEntity.ok(responseDTO);
		} catch (Exception e) {
			e.printStackTrace();

			// Set failure response
			responseDTO.setStatus("FAILURE");
			responseDTO.setMessage("Failed to save Checklist Category and Items.");
			responseDTO.setErrorCode("500");
			responseDTO.setErrorMessage(e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
		}

	}

	@PostMapping("/addInspectionTypeToEntities")
	public void addInspectionTypeToEntities(@RequestBody Map<String, Object> request) {
		String inspectionTypeId = (String) request.get("inspectionTypeId");
		@SuppressWarnings("unchecked")
		List<String> entityIds = (List<String>) request.get("entityIds");

		inspectionTypeService.addInspectionTypeToEntities(inspectionTypeId, entityIds);
	}

	@GetMapping("/getUsers")
	public Page<UsersDTO> getAllUserDetails(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		return usersService.getSelectedUserDetails(page, size);
	}

//	  Get Userdetails by emailID///
	@GetMapping("/UserDetails")
	public Map<String, Object> getUserDetails(@RequestParam("emailID") String emailID) {
		return usersService.getUserDetails(emailID);
	}

	@GetMapping("/getAllSkillNames")
	public List<SkillsDTO> getSkillsBySkillName() {
		return skillsService.getSkillsBySkillName();
	}

	@PostMapping("/saveZone")
	public ResponseEntity<String> saveZone(@RequestBody Zone zone) {
		try {
			zoneService.saveZone(zone);
			return ResponseEntity.ok("Zone saved successfully!");
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("An error occurred while saving the zone.");
		}
	}

	@GetMapping("/getIdAndNames")
	public ResponseEntity<List<ZoneDTO>> getAllZones() {
		List<ZoneDTO> zones = zoneService.getAllZones();
		return ResponseEntity.ok(zones);
	}

	@GetMapping("/getAllCheckListitems")
	public Page<CheckList_ItemDTO> getChecklistItems(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return checklistItemService.getChecklistItems(pageable);
	}

	@GetMapping("/getAllCheckListCategory")
	public Page<ChecklistCategoriesDTO> getAllChecklistCategories(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		return checklistCategoryService.getAllChecklistCategories(page, size);
	}

	// Get all skills with inspection type for add inspection type
	@GetMapping("/getSkills")
	public ResponseEntity<Page<SkillInspectionTypeDTO>> getSkills(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Page<SkillInspectionTypeDTO> skills = skillsService.getSkillandRelatedInspectionTypes(page, size);
		return ResponseEntity.ok(skills);
	}

	// Get category data for edit category
	@GetMapping("getChecklistCategory/{id}")
	public ResponseEntity<ChecklistCategoryDTO> getChecklistCategoryById(@PathVariable("id") long id) throws Exception {
		ChecklistCategoryDTO checklistCategoryDTO = checklistCategoryService.getChecklistCategoryById(id);
		return ResponseEntity.ok(checklistCategoryDTO);
	}

	// Edit the checklist category
	@PutMapping("/updateChecklistCategory")
	public ResponseEntity<ResponseDTO> updateChecklistCategory(@RequestBody ChecklistCategoryDTO request) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			// Call the service to save checklist category and items
			checklistCategoryService.updateChecklistCategory(request.getChecklist_cat_id(),
					request.getChecklist_category_name(), request.getCategory_threshold_local(),
					request.getCategory_weightage_local(), request.getChecklist_ids());

			// Set success response
			responseDTO.setStatus("SUCCESS");
			responseDTO.setMessage("Checklist Category and Items updated successfully!");

			return ResponseEntity.ok(responseDTO);
		} catch (Exception e) {
			e.printStackTrace();

			// Set failure response
			responseDTO.setStatus("FAILURE");
			responseDTO.setMessage("Failed to save Checklist Category and Items.");
			responseDTO.setErrorCode("500");
			responseDTO.setErrorMessage(e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
		}

	}

	@PostMapping("/addSlaWithInspectionName")
	public ResponseEntity<AISResponseDTO> createInspectionSLA(@RequestBody InspectionTypeSLADTO request) {
		String name = request.getName();
		String action = request.getAction();
		Map<String, InspectionTypeSLADTO.SLAEntityDetails> entitySizes = request.getEntitySizes();

		AISResponseDTO response = inspectionSLAService.createInspectionSLA(name, entitySizes, action);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/addChecklistitems")
	public ResponseEntity<List<addChecklistitemsDTO>> getAllChecklistItems() {
		List<addChecklistitemsDTO> checklistItems = checklistService.getAllChecklistItems();
		return ResponseEntity.ok(checklistItems);
	}

	@GetMapping("/checklistItemsBy/{checklistId}")
	public ResponseEntity<?> getChecklistItemById(@PathVariable long checklistId) {
		Checklist_Item checklistItem = checklistService.getChecklistItemById(checklistId);
		if (checklistItem != null) {
			return ResponseEntity.ok(checklistItem);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Checklist item with ID " + checklistId + " not found.");
		}

	}

	@PostMapping("/AddEditTemplate")
	public AISResponseDTO saveOrUpdateTemplate(@RequestBody TempCheckInspDTO tempCheckInspDTO) {
		AISResponseDTO responseDTO = new AISResponseDTO();

		try {
			if ("add".equalsIgnoreCase(tempCheckInspDTO.getAction())) {
				tempCheckInspService.addTemplate(tempCheckInspDTO); // Add new template
				responseDTO.setStatus("SUCCESS");
				responseDTO.setMessage("Template added successfully.");
			} else if ("edit".equalsIgnoreCase(tempCheckInspDTO.getAction())) {
				tempCheckInspService.editTemplate(tempCheckInspDTO); // Edit existing template
				responseDTO.setStatus("SUCCESS");
				responseDTO.setMessage("Template updated successfully.");
			} else {
				responseDTO.setStatus("FAILURE");
				responseDTO.setMessage("Invalid action.");
			}

		} catch (Exception e) {
			responseDTO.setStatus("ERROR");
			responseDTO.setMessage("Error occurred: " + e.getMessage());

		}

		return responseDTO;
	}

	@GetMapping("/getAllInspectionTypes")
	public List<GetAllInspection_TypeDTO> getAllInspectionIdsAndNames() {
		return inspectionTypeService.getAllInspectionIdsAndNames();
	}

	@GetMapping("/getUserSkills")
	public ResponseEntity<?> getUserSkills(@RequestParam Long userId) {
		try {
			List<UserSkillDTO> userSkills = userSkillService.getUserSkillsByUserId(userId);
			return ResponseEntity.ok(userSkills);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/getalltemplatesandinspectiontypes")
	public ResponseEntity<Page<TemplateResponseDTO>> getAllTemplatesWithInspectionTypes(
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
		Page<TemplateResponseDTO> response = templateService.getTemplatesWithInspectionTypes(page, size);
		return ResponseEntity.ok(response);
	}

	// Get all inspection types Ids and names for add entity and edit entity

	@GetMapping("/getAllUsersWithSkills")
	public Page<GetAllSkillDTO> getAllUsersWithSkills(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(required = false) String role) { // Accept role as
																										// a query
																										// parameter
		Pageable pageable = PageRequest.of(page, size);
		return userSkillService.getAllUsersWithSkills(pageable, role);
	}

	@GetMapping("/getEntityDetails/{entityId}")
	public ResponseEntity<EntityDetailsDTO> getEntityById(@PathVariable("entityId") String entityId) {
		EntityDetailsDTO entityDetails = newEntityService.getEntityDetailsById(entityId);

		if (entityDetails != null) {
			return ResponseEntity.ok(entityDetails);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/inspection-type/{id}")
	public ResponseEntity<?> getInspectionTypeWithSkills(@PathVariable long id) {
		// Call the service layer to retrieve the Inspection_Type with Skills
		InspectionTypeAdminSkillDTO inspectionType = inspectionTypeService.getInspectionTypeById(id);

		// If the inspectionType is found, return it with HTTP status 200 OK
		if (inspectionType != null) {
			return ResponseEntity.ok(inspectionType);
		} else {
			// If not found, return a 404 NOT_FOUND with an appropriate message
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inspection Type with ID " + id + " not found.");
		}
	}

	@GetMapping("/getSlaWithInspection/{id}")
	public ResponseEntity<InspectionTypeGetAdminDTO> getInspectionTypeDetailsWithSLA(@PathVariable("id") long id) {
		InspectionTypeGetAdminDTO dto = inspectionTypeService.getInspectionTypeDetails(id);
		return ResponseEntity.ok(dto);
	}

	// Get all users with id,name, role and skills for add Skill

	@GetMapping("/getAllInspectionTypesWithSkills")
	public Page<InspectionTypeSkillAdminDTO> getAllInspectionTypesWithSkills(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return inspectionTypeService.getAllInspectionTypesWithSkills(pageable);
	}

	@PostMapping("/updateUserDetails")
	public ResponseEntity<Map<String, String>> updateUserDetails(@RequestBody UsersDTO usersDTO) {
		Map<String, String> response = new HashMap<>();

		try {
			userService.updateUserDetails(usersDTO);
			response.put("message", "User details have been updated successfully.");
			response.put("status", "SUCCESS");
			return ResponseEntity.ok(response);
		} catch (RuntimeException e) {
			response.put("message", e.getMessage());
			e.printStackTrace();
			response.put("status", "FAILURE");
			return ResponseEntity.badRequest().body(response);
		}
	}

	// Edit the Existing skill or Update the skill and ADD the new skill
	@PostMapping("/AddEditSkill")
	public ResponseDTO manageSkill(@RequestBody SkillRequestDTO skillRequest) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			String result = skillsService.handleSkillAction(skillRequest);
			responseDTO.setStatus("SUCCESS");
			responseDTO.setMessage(result);
		} catch (IllegalArgumentException e) {
			responseDTO.setStatus("FAILURE");
			responseDTO.setMessage(e.getMessage());
		} catch (Exception e) {
			responseDTO.setStatus("ERROR");
			responseDTO.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return responseDTO;
	}

	//// Get the entity mappings by inspectionId for edit////
	@GetMapping("/getEntityMappingsForInspections/{insTypeId}")
	public InspectionTypeDTO getInspectionType(@PathVariable long insTypeId) {
		return inspectionTypeService.getInspectionTypewithEntity(insTypeId);
	}

	@GetMapping("/getChecklist/{checklistId}")
	public ResponseEntity<Checklist_Item> getChecklistDetails(@PathVariable long checklistId) {
		Checklist_Item checklistItem = checklistItemService.getChecklistDetails(checklistId);
		if (checklistItem == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(checklistItem);
	}

	@GetMapping("/getAlltheEntitiesDetails")
	public Page<Map<String, Object>> getAllTheEntities(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return newEntityService.getPaginatedEntitiesWithInspectionTypes(pageable);
	}

	@PostMapping("/editEntityMapping")
	public ResponseEntity<Map<String, String>> updateInspectionType(@RequestBody InspectionTypeDTO dto) {
		Map<String, String> response = new HashMap<>();
		try {
			inspectionTypeService.updateInspectionType(dto);
			response.put("message", "InspectionType updated successfully.");
			response.put("status", "SUCCESS");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("message", "Error: " + e.getMessage());
			response.put("status", "FAILURE");
			return ResponseEntity.badRequest().body(response);
		}
	}

////Add and edit a preinspection checklist///
	@PostMapping("/addEditPreInspectionChecklist")
	public ResponseEntity<ResponseDTO> addChecklist(@RequestBody PreInspectionChecklistDTO checklistDTO) {
		ResponseDTO response = new ResponseDTO();
		try {
			PreInspectionChecklist checklist = preInspectionChecklistService.saveOrUpdateChecklist(checklistDTO);
			response.setStatus("success");
			response.setMessage("Checklist created successfully.");

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response.setStatus("error");
			response.setMessage("Error creating checklist.");
			response.setErrorCode("500");
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping("/updateChecklistItem/{checklistId}")
	public ResponseEntity<Map<String, String>> updateChecklistItem(@PathVariable Long checklistId,
			@RequestBody addChecklistitemsDTO checklistItemDTO) {

		Map<String, String> response = new HashMap<>();
		try {
			Checklist_Item updatedChecklistItem = checklistItemService.updateChecklistItem(checklistId,
					checklistItemDTO);

			if (updatedChecklistItem != null) {
				response.put("message", "Checklist Item updated successfully.");
				response.put("status", "SUCCESS");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("message", "Checklist Item not found.");
				response.put("status", "FAILURE");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			response.put("message", "An error occurred: " + e.getMessage());
			response.put("status", "FAILURE");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// using templateId retrive the inpsections details and categories associated to
	// templateID
	@GetMapping("/getTemplateDetails/{template_id}")
	public TemplateDetailsDTO getTemplateDetails(@PathVariable long template_id) {
		return templateService.getTemplateDetails(template_id);
	}

	@GetMapping("/getAllPreInspections")
	public Page<PreInspectionChecklistDTO> getAllPreInspectionChecklists(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		return preInspectionChecklistService.getAllPreInspectionChecklists(page, size);
	}

	@GetMapping("/api/pre-inspection-checklist/{id}")
	public PreInspectionChecklistResponseDTO getChecklistById(@PathVariable("id") long id) {
		return preInspectionChecklistService.getChecklistById(id);
	}

	@GetMapping("/getSkillDetails/{skillId}")
	public SkillDetailDTO getSkillDetails(@PathVariable("skillId") long skillId) {
		SkillDetailDTO skillDetailDTO = new SkillDetailDTO();
		try {
			skillDetailDTO = skillsService.getSkillDetails(skillId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return skillDetailDTO;
	}

	@PostMapping("/addEditInspectionType")
	public ResponseEntity<AISResponseDTO> addInspectionTypeWithSkillsAndSLA(
			@RequestBody InspectionTypeRequestDTO inspectionTypeRequestDTO, @RequestParam("action") String action) {

		// Create a response object to store the results
		AISResponseDTO responseDTO = new AISResponseDTO();

		try {
			// Process Inspection Type Primary Details
			long inspectionTypeId = inspectionTypeService
					.processInspectionType(inspectionTypeRequestDTO.getInspectionTypePrimaryDetails(), action);

			// Add entities to the inspection type
			List<String> entityIds = inspectionTypeRequestDTO.getInspectionTypeEntity().getEntityIds();
			inspectionTypeService.addInspectionTypeToEntities(String.valueOf(inspectionTypeId), entityIds);

			// Process SLA details

			InspectionTypeSLADTO inspectionTypeSLA = inspectionTypeRequestDTO.getInspectionTypeSLA();

//	         System.out.println("InspectionTypeSLA Name: " + inspectionTypeSLA.getName());
//	         System.out.println("InspectionTypeSLA EntitySizes: " + inspectionTypeSLA.getEntitySizes());
			System.out.println("Action: " + action);
			inspectionSLAService.createInspectionSLA(
					inspectionTypeRequestDTO.getInspectionTypePrimaryDetails().getName(),
					inspectionTypeSLA.getEntitySizes(), action);

			// Populate success response
			responseDTO.setStatus("SUCCESS");
			responseDTO.setMessage("Inspection type with skills and SLA successfully processed.");
			responseDTO.setInspectionTypeId(inspectionTypeId);

			return new ResponseEntity<>(responseDTO, HttpStatus.OK);

		} catch (IllegalArgumentException e) {
			// Handle specific validation errors
			responseDTO = buildErrorResponse("Invalid request: " + e.getMessage());
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			// Handle general errors
			e.printStackTrace();
			responseDTO = buildErrorResponse("Failed to process the request: " + e.getMessage());
			return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//	 @PostMapping("/startAISProcessSAP")
//	    public ResponseEntity<AISResponseDTO> startAISProcess(@RequestBody startAISFromSAPAPIDTO fromSAPAPIDTO) {
//	        AISResponseDTO responseDTO = new AISResponseDTO();
//	 
//	        try {
//	            // Call the service method and get the business key
//	            String businessKey = aisService.processStartAIS(fromSAPAPIDTO);
//	 
//	            // Set success response details
//	            responseDTO.setStatus("Success");
//	            responseDTO.setBusinessKey(businessKey);
//	            responseDTO.setMessage("AIS process started successfully.");
//	            responseDTO.setSapNotificationID(fromSAPAPIDTO.getSapNotificationID());
//	 
//	            return ResponseEntity.ok(responseDTO);
//	        } catch (Exception e) {
//	            // Set failure response details
//	            responseDTO.setStatus("Failure");
//	            responseDTO.setBusinessKey(null);
//	            responseDTO.setMessage("Error starting AIS process: " + e.getMessage());
//	            responseDTO.setSapNotificationID(fromSAPAPIDTO.getSapNotificationID());
//	 
//	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
//	        }
//	    }

	@GetMapping("/getInspectionTypeById/{inspectionTypeId}")
	public InspectionTypeRequestDTO getInspectionTypeById(@PathVariable("inspectionTypeId") long inspectionTypeId) {
		InspectionTypeRequestDTO inspectionTypeRequestDTO = new InspectionTypeRequestDTO();
		try {
			inspectionTypeRequestDTO = inspectionTypeService.getInspectionTypeByIdForEdit(inspectionTypeId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inspectionTypeRequestDTO;
	}

	// EntityRegistration based on Entity_Type & Edit EntityRegistration details
	// based on entityId

	@PostMapping(value = "/entityRegistration", consumes = { "multipart/form-data" })
	public ResponseDTO postEntity(
			@RequestPart(value = "entityRegistrationDTO", required = true) EntityRegistrationDTO entityRegistrationDTO,
			@RequestPart(value = "files", required = false) MultipartFile[] files,
			@RequestParam(value = "deleteAttachmentIds", required = false) List<Long> deleteAttachmentIds) {

		ResponseDTO responseDTO = new ResponseDTO();

		// Attach files to DTO
		if (files != null) {
			entityRegistrationDTO.setAttachments(Arrays.asList(files));
		}

		// Attachments to delete
		if (deleteAttachmentIds != null && !deleteAttachmentIds.isEmpty()) {
			entityRegistrationDTO.setDeleteAttachmentIds(deleteAttachmentIds);
		}

		try {
			// Validate action
			String action = entityRegistrationDTO.getAction();
			if (action == null || (!action.equalsIgnoreCase("save") && !action.equalsIgnoreCase("edit"))) {
				responseDTO.setStatus("Failure");
				responseDTO.setErrorCode("400");
				responseDTO.setErrorMessage("Invalid action: " + action + ". Allowed actions are 'save' or 'edit'.");
				return responseDTO;
			}

			// Process entity registration
			String responseMessage = newEntityService.entityRegistration(entityRegistrationDTO);

			responseDTO.setStatus("Success");
			responseDTO.setMessage(responseMessage);

		} catch (RuntimeException e) {
			responseDTO.setStatus("Failure");
			responseDTO.setErrorCode("400");
			responseDTO.setErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatus("Failure");
			responseDTO.setErrorCode("500");
			responseDTO.setErrorMessage("Error during entity registration: " + e.getMessage());
		}

		return responseDTO;
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticate(@RequestBody LoginRequestDTO loginRequest) {
		try {
			// Decrypt the password using AESDecryptionUtil
			// String decryptedPassword =
			// AESDecryptionUtil.decrypt(loginRequest.getPassword());

			// Authenticate the user
			UsersDTO userDTO = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());

			return ResponseEntity.ok(userDTO);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body("Invalid input provided: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Status: " + e.getMessage());
		}
	}

	// Activate and Deactivate any record in any table

	@PostMapping("/updateIsActive")
	public ResponseDTO updateIsActive(@RequestBody UpdateIsActiveDTO request) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			// Call the service to update the status
			updateIsActiveService.updateStatus(request);

			// Set success response
			responseDTO.setStatus("Success");
			responseDTO.setMessage("Status updated successfully");
			return responseDTO;
		} catch (IllegalArgumentException e) {
			// Set failure response for bad input
			responseDTO.setStatus("Failure");
			responseDTO.setErrorCode("400");
			responseDTO.setErrorMessage(e.getMessage());
			return responseDTO;
		} catch (Exception e) {
			// Set failure response for internal server errors
			responseDTO.setStatus("Failure");
			responseDTO.setErrorCode("500");
			responseDTO.setErrorMessage("Error updating status: " + e.getMessage());
			return responseDTO;
		}
	}

	@GetMapping("/getAllInspectorNames")
	public ResponseEntity<List<UsersRoleDTO>> getUserByRole(@RequestParam String role) {
		List<UsersRoleDTO> users = userService.getUserByRole(role);
		return ResponseEntity.ok(users);
	}

	@GetMapping("/getAllCasesBasedOnFilters")
	public Page<InspectionCaseDTO> getInspectionCasesWithEntityDetails(@RequestParam(required = false) String entityid,
			@RequestParam(required = false) String inspectorID, @RequestParam(required = false) String status,
			@RequestParam(required = false) String inspector_source,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start_date,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end_date,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueStartDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueEndDate,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

		Pageable pageable = PageRequest.of(page, size);
		return inspectionCaseService.getInspectionCasesWithEntityDetails(entityid, inspectorID, status,
				inspector_source, start_date, end_date, dueStartDate, dueEndDate, pageable);
	}

	// Get entity details by entity id checking for entity type
	@GetMapping("/getEntityRegistrationDetailsbyEntityID")
	public Object getEntityDetailsbyEntityID(@RequestParam("entityId") String entityId) {
		try {
			// Fetch the result from the service
			return newEntityService.getEntityDetailsbyEntityID(entityId);
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching entity details", e);
		}
	}

	@GetMapping("/getCaseDetailsById/{inspectionId}")
	public Optional<Optional<InspectionCaseDetailsDTO>> getInspectionCaseDetailsById(
			@PathVariable("inspectionId") long inspectionId) {
		return inspectionCaseService.getInspectionCaseDetails(inspectionId);
	}

	@GetMapping("/getEntityNames/{ins_type_id}")
	public ResponseEntity<List<EntitiesInspectionTypeDTO>> getEntities1(
			@PathVariable(value = "ins_type_id", required = false) Long insTypeId) {
		List<EntitiesInspectionTypeDTO> response = newEntityService.fetchEntities(insTypeId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/getEntityReportById")
	public ResponseEntity<?> getEntityReportById(@RequestParam String entityId) {
		try {
			// Fetch the entity report by ID using the service
			EntityReportDTO reportDTO = entityInspectionReportService.getEntityReportById(entityId);

			// Log the response data for debugging
			System.out.println("Returning Entity Report DTO: " + reportDTO);

			// Return 200 OK with the reportDTO data
			return ResponseEntity.ok(reportDTO);
		} catch (EntityNotFoundException e) {
			// Return a 404 if the entity is not found
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}

	@PostMapping("/getEntityInspectionReport")
	public ResponseEntity<EntityInspectionReportDTO> getInspectionReport(@RequestBody EntityRequestDTO requestDTO) {

		// Call the service to get the report
		EntityInspectionReportDTO reportDTO = entityInspectionReportService
				.getInspectionReportByEntityAndDate(requestDTO);

		// Return the report as the response
		return ResponseEntity.ok(reportDTO);
	}

	@PostMapping("/getEntityInspectionCasesReport")
	public ResponseEntity<List<EntityInspectionCasesReportResponseDTO>> getInspectionCases(
			@RequestBody EntityRequestDTO requestDTO) {
		List<EntityInspectionCasesReportResponseDTO> cases = entityInspectionReportService
				.getInspectionCases(requestDTO);
		return ResponseEntity.ok(cases);
	}

	@GetMapping("/getTransactionsSummaryReport")
	public CaseSummaryDTO getCaseSummary(@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate) {
		return inspectionCaseService.getCaseSummary(startDate, endDate);
	}

	@GetMapping("/getTransactions")
	public List<Map<String, Object>> getCaseStats(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		return inspectionCaseService.getCaseStatsByMonth(startDate, endDate);
	}

	@PostMapping("/inspectionHistory")
	public InspectionHistoryDTO getInspectionHistory(@RequestBody InspectionFilters inspectionFilters) {
		InspectionHistoryDTO inspectionHistoryDTO = new InspectionHistoryDTO();
		try {
			inspectionHistoryDTO = inspectionCaseService.getCasesByFilters(inspectionFilters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inspectionHistoryDTO;
	}

	@PostMapping("/inspectionHistory/getCases")
	public List<InspectionCase_EntityDTO> getInspectionHistoryCases(@RequestBody InspectionFilters inspectionFilters) {
		List<InspectionCase_EntityDTO> inspectionCaseList = new ArrayList<>();
		try {
			inspectionCaseList = inspectionCaseService.getInspectionHistoryCases(inspectionFilters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inspectionCaseList;
	}

	@GetMapping("/topFive-categories")
	public ResponseEntity<Map<String, Long>> getTopNegativeCategories(
			@RequestParam(value = "ins_Type_Id", required = false) Long ins_Type_Id) {
		Map<String, Long> response = categoriesSummaryReportService.getTopNegativeCategories(ins_Type_Id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/topTen-negative-observations")
	public ResponseEntity<List<TopTenNegativeObservationsDTO>> getTopTenNegativeObservations(
			@RequestParam(value = "ins_Type_Id", required = false) Long ins_Type_Id) {

		List<TopTenNegativeObservationsDTO> response = categoriesSummaryReportService
				.getTop10NegativeObservations(ins_Type_Id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/getAllControlType")
	public Page<ControlType> getAllControlTypes(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return controlTypeService.getAllControlTypes(pageable);

	}

	@PostMapping("/AddorEditControlType")
	public ResponseEntity<AISResponseDTO> addControlType(@RequestBody ControlTypeDTO controlTypeDTO) {
		AISResponseDTO response = new AISResponseDTO();

		try {
			if ("add".equalsIgnoreCase(controlTypeDTO.getAction())) {
				ControlType existingControlType = controlTypeService
						.findByControlTypeName(controlTypeDTO.getControlTypeName());

				if (existingControlType != null) {
					response.setStatus("Failure");
					response.setMessage(
							"Control Type with the name '" + controlTypeDTO.getControlTypeName() + "' already exists.");
				} else {
					ControlType savedControlType = controlTypeService.saveControlType(controlTypeDTO);
					response.setStatus("Success");
					response.setMessage(
							"Control Type '" + savedControlType.getControlTypeName() + "' added successfully.");
				}
			} else if ("edit".equalsIgnoreCase(controlTypeDTO.getAction())) {
				if (controlTypeDTO.getControlTypeId() == null) {
					response.setStatus("Failure");
					response.setMessage("Control Type ID must be provided for editing.");
				} else {
					ControlType updatedControlType = controlTypeService.updateControlType(controlTypeDTO);
					if (updatedControlType != null) {
						response.setStatus("Success");
						response.setMessage(
								"Control Type '" + updatedControlType.getControlTypeName() + "' updated successfully.");
					} else {
						response.setStatus("Failure");
						response.setMessage(
								"Control Type with ID '" + controlTypeDTO.getControlTypeId() + "' not found.");
					}
				}
			} else {
				response.setStatus("Failure");
				response.setMessage("Invalid action. Please specify 'add' or 'edit'.");
			}
		} catch (IllegalArgumentException e) {
			response.setStatus("Failure");
			response.setMessage(e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/AddEditUserGroup")
	public ResponseEntity<Map<String, String>> processUserGroup(@RequestBody UserGroupDTO userGroupDTO) {
	    Map<String, String> response = new HashMap<>();
	    try {
	        String message = userGroupService.processUserGroup(userGroupDTO);
	        response.put("status", "success");
	        response.put("message", message);
	        return ResponseEntity.ok(response);
	    } catch (RuntimeException e) {
	        response.put("status", "failure");
	        response.put("message", e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

	@GetMapping("/getAllUserGroups")
	public ResponseEntity<Page<AllUserGroupDTO>> getAllUserGroups(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size);
		Page<AllUserGroupDTO> userGroups = userGroupService.getAllUserGroups(pageable);
		return ResponseEntity.ok(userGroups);
	}

	@GetMapping("/getUsersByGroupId/{groupId}")
	public List<UserGroupDTO> getUsersByGroupId(@PathVariable Long groupId) {
		return userGroupService.getUsersByGroupId(groupId);
	}

	@GetMapping("/getControlTypeWithInspectionDetails/{controlTypeId}")
	public ResponseEntity<Map<String, Object>> getControlTypeDetails(@PathVariable long controlTypeId) {
		return ResponseEntity.ok(controlTypeService.getControlTypeWithInspectionDetails(controlTypeId));
	}

	@GetMapping("/getUserGroupForEditById/{groupId}")
	public ResponseEntity<UsersGroupDTO> getUserGroupDetails(@PathVariable Long groupId) {
		UsersGroupDTO dto = userGroupForEditByIdService.getUserGroupDetails(groupId);
		if (dto == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(dto);
	}

	// Get all the UserGroups with UserGroupID, UserGroupName, isActive and get the
	// count of Users linked to that UserGroup

	@GetMapping("/getUserGroupsForAssign")
	public List<UserGroupDTO> getUserGroupSummary() {
		return userGroupService.getUserGroupSummary();
	}

	// Get All ControlTypes Without Pagination
	@GetMapping("/getAllControlTypesForCase")
	public List<ControlType> getAllControlTypes() {
		return controlTypeService.getControlTypes();
	}

	@GetMapping("/getGroupcasesForLeadInspector/{leadId}")
	public List<InspectionCase_EntityDTO> getGroupCaseForLeadInspector(@PathVariable("leadId") Long leadId) {
		return getGroupCasesForLeadService.getGroupCaseForLeadInspector(leadId);
	}

	// get All InspectionPlan details
	@GetMapping("/getAllInspectionPlanDetails")
	public Page<InspectionPlanDTO> getAllInspectionPlans(@RequestParam(defaultValue = "0", required = false) int page,
			@RequestParam(defaultValue = "10", required = false) int size) {

		Pageable pageable = PageRequest.of(page, size);
		return inspectionPlanService.getAllInspectionPlans(pageable);
	}

	@PostMapping("assignCategory/{inspectionID}/{action}")
	public ResponseEntity<String> handleInspectionMapping(@PathVariable Long inspectionID, @PathVariable String action,
			@RequestBody List<AssignRequestDTO> requestDTOList) {

		// Validate action (must be "SAVE" or "SUBMIT")
		if (!action.equalsIgnoreCase("SAVE") && !action.equalsIgnoreCase("SUBMIT")) {
			return ResponseEntity.badRequest().body("Invalid action! Use 'SAVE' or 'SUBMIT'.");
		}

		// Call service to process the list of requests
		String response = inspectionMappingService.assignInspection(inspectionID, action, requestDTOList);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/getAllInspectionTypes/{controlTypeId}")
	public List<GetAllInspection_TypeDTO> getInspectionIdsAndNames(@PathVariable long controlTypeId) {
		return inspectionTypeService.getInspectionIdsAndNamesByControlTypeId(controlTypeId);
	}

	@PostMapping("/createInspectionPlan")
    public ResponseEntity<Map<String, Object>> createInspectionPlan(
            @RequestBody CreateInspectionPlanRequestDTO requestDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            if ("edit".equalsIgnoreCase(requestDTO.getAction())) {
                // Handle edit action
                response = inspectionPlanService.editInspectionPlan(requestDTO);
                return ResponseEntity.ok(response);
            } else {
                // Handle create action (default)
                if (inspectionPlanService.existsByInspectionPlanName(requestDTO.getInspectionPlanName())) {
                    response.put("status", "error");
                    response.put("message", "Inspection Plan with the same name already exists: " 
                                            + requestDTO.getInspectionPlanName());
                    return ResponseEntity.badRequest().body(response);
                }

                InspectionPlan inspectionPlan = new InspectionPlan();
                inspectionPlan.setInspectionPlanName(requestDTO.getInspectionPlanName());
                inspectionPlan.setReasonForInspectionPlan(requestDTO.getReasonForInspectionPlan());
                inspectionPlan.setDescription(requestDTO.getDescription());
                inspectionPlan.setInspectorType(requestDTO.getInspectorType());

                List<SelectedEntityDTO> selectedEntities = requestDTO.getSelectedEntities();
                LocalDate dateOfInspection = requestDTO.getDateOfInspection();
                String createdBy = requestDTO.getCreatedBy();

                response = inspectionPlanService.createInspectionPlan(
                        inspectionPlan, selectedEntities, dateOfInspection, createdBy);

                return ResponseEntity.ok(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

	// Get the assigned inspector with checklist
	@GetMapping("/getAssignedInspectorChecklist/{inspectionID}")
	public ResponseEntity<Object> getChecklistByInspectionID(@PathVariable Long inspectionID,
			@RequestParam(required = false) String inspectorID) {
		Object response = checklistService.getChecklistByInspectionID(inspectionID, inspectorID);
		return ResponseEntity.ok(response);
	}

	// Get the Checklists based on inspectorId
	@GetMapping("/getChecklistForInspector/{inspectorID}")
	public ResponseEntity<List<ChecklistDTO>> getChecklistByInspectorID(@PathVariable String inspectorID) {
		List<ChecklistDTO> response = checklistService.getChecklistByInspectorID(inspectorID);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/getAddressByEntity/{entityId}")
    public ResponseEntity<EntityResponseDTO> getAllEntityById(@PathVariable String entityId) {
        EntityResponseDTO response = newEntityService.getEntityAllDetailsById(entityId);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
	@GetMapping("/getInspectionplan/{inspectionPlanId}")
    public ResponseEntity<CreateInspectionPlanRequestDTO> getInspectionPlanWithCasesById(@PathVariable String inspectionPlanId) {
        CreateInspectionPlanRequestDTO responseDTO = inspectionPlanService.getInspectionPlanWithCasesById(inspectionPlanId);
        if (responseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(responseDTO);
    }

}
