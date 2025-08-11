package com.aaseya.AIS.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aaseya.AIS.Model.ControlType;
import com.aaseya.AIS.Model.Inspection_SLA;
import com.aaseya.AIS.Model.Inspection_Type;
import com.aaseya.AIS.Model.NewEntity;
import com.aaseya.AIS.Model.Skill;
import com.aaseya.AIS.Model.Users;
import com.aaseya.AIS.dao.EntityDAO;
import com.aaseya.AIS.dao.InspectionTypeDAO;
import com.aaseya.AIS.dao.SkillDAO;
import com.aaseya.AIS.dao.UsersDAO;
import com.aaseya.AIS.dto.EntityDetailsDTO;
import com.aaseya.AIS.dto.GetAllInspection_TypeDTO;
import com.aaseya.AIS.dto.InspectionTypeAdminSkillDTO;
import com.aaseya.AIS.dto.InspectionTypeDTO;
import com.aaseya.AIS.dto.InspectionTypeEntityDTO;
import com.aaseya.AIS.dto.InspectionTypeGetAdminDTO;
import com.aaseya.AIS.dto.InspectionTypeGetAdminDTO.GoalDeadlineDTO;
import com.aaseya.AIS.dto.InspectionTypeGetAdminDTO.SlaDetailDTO;
import com.aaseya.AIS.dto.InspectionTypePrimaryDetailsDTO;
import com.aaseya.AIS.dto.InspectionTypeRequestDTO;
import com.aaseya.AIS.dto.InspectionTypeSLADTO;
import com.aaseya.AIS.dto.InspectionTypeSkillAdminDTO;
import com.aaseya.AIS.dto.SkillDTO;
import com.aaseya.AIS.dto.UsersDTO;

import jakarta.transaction.Transactional;

@Service
public class InspectionTypeService {

	@Autowired
	private InspectionTypeDAO inspectionTypeDAO;

	@Autowired
	private UsersDAO usersDAO;

	@Autowired
	private SkillDAO skillDAO;

	public List<InspectionTypeDTO> getInspectionType() {
		List<InspectionTypeDTO> inspectionTypeList = new ArrayList<InspectionTypeDTO>();
		List<Inspection_Type> inspectionTypes = inspectionTypeDAO.getAllInspectionTypes();

		for (Inspection_Type inspection_type : inspectionTypes) {
			InspectionTypeDTO inspectionTypeDTO = new InspectionTypeDTO();
			inspectionTypeDTO.setEntitySize(inspection_type.getEntitySize());
			inspectionTypeDTO.setHigh(inspection_type.getHigh());
			inspectionTypeDTO.setHigh(inspection_type.getHigh());
			inspectionTypeDTO.setIns_type_id(inspection_type.getIns_type_id());
			inspectionTypeDTO.setIsActive(String.valueOf(inspection_type.isActive()));
			inspectionTypeDTO.setLow(inspection_type.getLow());
			inspectionTypeDTO.setName(inspection_type.getName());
			inspectionTypeDTO.setThreshold(inspection_type.getThreshold());
			inspectionTypeDTO.setMedium(inspection_type.getMedium());

			inspectionTypeList.add(inspectionTypeDTO);
		}
		return inspectionTypeList;
	}

	public List<String> getInspectionTypeNames() {
		return inspectionTypeDAO.getAllInspectionTypeNames();
	}

	public Inspection_Type getInspectionTypeById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Optional<Inspection_Type> getAllInspectionType() {
		// TODO Auto-generated method stub
		return null;
	}

//    @Transactional
//    public InspectionTypeSkillDTO getInspectionTypeById(long id) {
//        Inspection_Type inspectionType = inspectionTypeDAO.findById(id);
//
//        if (inspectionType != null) {
//            return convertToDTO(inspectionType);
//        } else {
//            // Handle not found case
//            return null; // or throw an exception or return an optional
//        }
//    }

	@Transactional
	public InspectionTypeDTO getInspectionTypeDetailsByName(String name) {
		Inspection_Type inspectionType = inspectionTypeDAO.getInspectionTypeByName(name);

		if (inspectionType == null) {
			return null;
		}

		Set<String> inspectionSkills = inspectionType.getSkills().stream().map(Skill::getSkill)
				.collect(Collectors.toSet());

		List<Users> usersList = usersDAO.getUsersByRole("Inspector");

		List<UsersDTO> filteredUsers = usersList.stream().filter(user -> user.getSkill().stream().map(Skill::getSkill)
				.collect(Collectors.toSet()).containsAll(inspectionSkills)).map(user -> {
					UsersDTO userDTO = new UsersDTO();
					// userDTO.setUserID(user.getUserID());
					userDTO.setUserName(user.getUserName());
					userDTO.setEmailID(user.getEmailID());
					userDTO.setRole(user.getRole());
					userDTO.setSkills(user.getSkill().stream().map(Skill::getSkill).collect(Collectors.toList()));
					return userDTO;
				}).collect(Collectors.toList());

		InspectionTypeDTO inspectionTypeDTO = new InspectionTypeDTO();
		inspectionTypeDTO.setIns_type_id(inspectionType.getIns_type_id());
		inspectionTypeDTO.setName(inspectionType.getName());
		inspectionTypeDTO.setSkills(inspectionSkills.stream().collect(Collectors.toList()));
		inspectionTypeDTO.setUsers(filteredUsers);

		return inspectionTypeDTO;
	}

	/// addInspectionTypeToEntities///
	@Autowired
	private EntityDAO entityDAO;

	@Transactional
	public void addInspectionTypeToEntities(String inspectionTypeId, List<String> entityIds) {
		// Fetch the InspectionType by ID
		Inspection_Type inspectionType = inspectionTypeDAO.getInspectionTypeById(inspectionTypeId);

		// Fetch NewEntity objects by IDs
		Set<NewEntity> newEntities = new HashSet<>();
		for (String entityId : entityIds) {
			NewEntity newEntity = entityDAO.getEntityByEntityId(entityId);
			newEntities.add(newEntity);
		}

		// Create Many-to-Many relationship
		inspectionType.setNewEntities(newEntities);
		for (NewEntity newEntity : newEntities) {
			newEntity.getInspectionTypes().add(inspectionType);
		}

		// Save the changes
		inspectionTypeDAO.save(inspectionType);

	}/// addInspectionTypeToEntities///

	public List<GetAllInspection_TypeDTO> getAllInspectionIdsAndNames() {
		return inspectionTypeDAO.findAllInspectionIdsAndNames();
	}

	public InspectionTypeAdminSkillDTO getInspectionTypeById(long inspectionTypeId) {
		return inspectionTypeDAO.getInspectionTypeById(inspectionTypeId);
	}

	public InspectionTypeGetAdminDTO getInspectionTypeDetails(long insTypeId) {
		Inspection_Type inspectionType = inspectionTypeDAO.getInspectionTypeWithSla(insTypeId);

		if (inspectionType == null) {
			throw new RuntimeException("Inspection Type not found for ID: " + insTypeId);
		}

		// Map entity to DTO
		InspectionTypeGetAdminDTO dto = new InspectionTypeGetAdminDTO();
		dto.setInspectionId(inspectionType.getIns_type_id());
		dto.setInspectionType(inspectionType.getName());
		dto.setThreshold(inspectionType.getThreshold());
		dto.setEnity_size(inspectionType.getEntitySize());

		List<InspectionTypeGetAdminDTO.SlaDetailDTO> slaDetails = inspectionType.getInspectionSLAs().stream()
				.map(sla -> {
					InspectionTypeGetAdminDTO.SlaDetailDTO slaDto = new InspectionTypeGetAdminDTO.SlaDetailDTO();
					slaDto.setEntitySize(sla.getEntitySize());

					InspectionTypeGetAdminDTO.GoalDeadlineDTO inspector = new InspectionTypeGetAdminDTO.GoalDeadlineDTO();
					inspector.setGoal(sla.getInspectorGoal());
					inspector.setDeadline(sla.getInspectorDeadline());
					slaDto.setInspector(inspector);

					InspectionTypeGetAdminDTO.GoalDeadlineDTO reviewer = new InspectionTypeGetAdminDTO.GoalDeadlineDTO();
					reviewer.setGoal(sla.getReviewerGoal());
					reviewer.setDeadline(sla.getReviewerDeadline());
					slaDto.setReviewer(reviewer);

					InspectionTypeGetAdminDTO.GoalDeadlineDTO approver = new InspectionTypeGetAdminDTO.GoalDeadlineDTO();
					approver.setGoal(sla.getApproverGoal());
					approver.setDeadline(sla.getApproverDeadline());
					slaDto.setApprover(approver);

					return slaDto;
				}).collect(Collectors.toList());

		dto.setSlaDetails(slaDetails);
		return dto;
	}

	public List<InspectionTypeSkillAdminDTO> getInspectionTypesWithSkills() {
		// Fetch all inspection types
		List<Inspection_Type> inspectionTypes = inspectionTypeDAO.getAllInspectionTypes();

		// Transform the data into DTOs
		List<InspectionTypeSkillAdminDTO> result = new ArrayList<>();
		for (Inspection_Type inspectionType : inspectionTypes) {
			// Fetch skills for the current inspection type
			List<Skill> skills = inspectionTypeDAO.getSkillsByInspectionTypeId(inspectionType.getIns_type_id());

			// Map to InspectionTypeSkillAdminDTO
			InspectionTypeSkillAdminDTO dto = new InspectionTypeSkillAdminDTO();
			dto.setInsTypeId(inspectionType.getIns_type_id());
			dto.setName(inspectionType.getName());
			dto.setIsActive(inspectionType.isActive()); 

			// Extract skill names into a list of strings
			List<String> skillNames = new ArrayList<>();
			for (Skill skill : skills) {
				skillNames.add(skill.getSkill());
			}
			dto.setSkills(skillNames); // Set the skill names

			result.add(dto); // Add the DTO to the result list
		}

		return result; // Return the final list of DTOs

	}

	public InspectionTypeDTO getInspectionTypewithEntity1(long insTypeId) {
		// Fetch Inspection_Type by ins_type_id using the DAO
		Inspection_Type inspectionType = inspectionTypeDAO.getInspectionTypeByInspectionId(insTypeId);
		InspectionTypeDTO inspectionTypeDTO = new InspectionTypeDTO();

		if (inspectionType != null) {
			inspectionTypeDTO.setIns_type_id(inspectionType.getIns_type_id());
			inspectionTypeDTO.setName(inspectionType.getName());
			inspectionTypeDTO.setThreshold(inspectionType.getThreshold());

			// Prepare entity details
			List<EntityDetailsDTO> entityDetails = new ArrayList<>();
			for (NewEntity entity : inspectionType.getNewEntities()) {
				EntityDetailsDTO entityDTO = new EntityDetailsDTO();
				entityDTO.setEntityId(entity.getEntityid());
				entityDTO.setName(entity.getName());
				entityDTO.setAddress(entity.getFloor() + ", " + entity.getFacility() + ", " + entity.getAddress());
				entityDetails.add(entityDTO);
			}
			inspectionTypeDTO.setEntityDetails(entityDetails);
		} else {
			// Handle the case where the Inspection_Type is not found
			throw new RuntimeException("Inspection Type not found for ID: " + insTypeId);
		}

		return inspectionTypeDTO;
	}

	// addNewInspectionTypeWithSkills//
	@Transactional
	public long processInspectionType(InspectionTypePrimaryDetailsDTO inspectionTypePrimaryDetailsDTO, String action) {
		Inspection_Type inspectionType;
		if ("edit".equalsIgnoreCase(action)) {
			boolean nameExists = inspectionTypeDAO.existsByNameExcludingId(inspectionTypePrimaryDetailsDTO.getName(),
					inspectionTypePrimaryDetailsDTO.getIns_type_id());
			if (nameExists) {
				throw new IllegalArgumentException("Inspection name already exists for a different record.");
			}

			// Fetch the existing Inspection Type by ID
			inspectionType = inspectionTypeDAO.findById(inspectionTypePrimaryDetailsDTO.getIns_type_id());

			if (inspectionType == null) {
				throw new IllegalArgumentException(
						"Inspection type with ID " + inspectionTypePrimaryDetailsDTO.getIns_type_id() + " not found.");
			}

			// Update fields in the Inspection Type only if they are not null or empty
			if (inspectionTypePrimaryDetailsDTO.getName() != null
					&& !inspectionTypePrimaryDetailsDTO.getName().isEmpty()) {
				inspectionType.setName(inspectionTypePrimaryDetailsDTO.getName());
			}
			if (inspectionTypePrimaryDetailsDTO.getThreshold() != null
					&& !inspectionTypePrimaryDetailsDTO.getThreshold().isEmpty()) {
				inspectionType.setThreshold(inspectionTypePrimaryDetailsDTO.getThreshold());
			}
			inspectionType.setActive(true);

			if (inspectionTypePrimaryDetailsDTO.getHigh() != null) {
				inspectionType.setHigh(inspectionTypePrimaryDetailsDTO.getHigh());
			}
			if (inspectionTypePrimaryDetailsDTO.getMedium() != null) {
				inspectionType.setMedium(inspectionTypePrimaryDetailsDTO.getMedium());
			}
			if (inspectionTypePrimaryDetailsDTO.getLow() != null) {
				inspectionType.setLow(inspectionTypePrimaryDetailsDTO.getLow());
			}
			if (inspectionTypePrimaryDetailsDTO.getEntitySize() != null
					&& !inspectionTypePrimaryDetailsDTO.getEntitySize().isEmpty()) {
				inspectionType.setEntitySize(inspectionTypePrimaryDetailsDTO.getEntitySize());
			}
			// Set the control type
	        ControlType controlType = inspectionTypeDAO.findByControlTypeId(inspectionTypePrimaryDetailsDTO.getControlTypeId());
	        if (controlType != null) {
	            inspectionType.setControlTypes(Set.of(controlType));
	        }

		} else if ("save".equalsIgnoreCase(action)) {
			// Check if the inspection name already exists
			if (inspectionTypeDAO.existsByName(inspectionTypePrimaryDetailsDTO.getName())) {
				throw new IllegalArgumentException("Inspection name already exists.");
			}

			// Create a new Inspection Type
			inspectionType = new Inspection_Type();
			inspectionType.setName(inspectionTypePrimaryDetailsDTO.getName());
			inspectionType.setThreshold(inspectionTypePrimaryDetailsDTO.getThreshold());
			inspectionType.setActive(true);
			inspectionType.setHigh(inspectionTypePrimaryDetailsDTO.getHigh());
			inspectionType.setMedium(inspectionTypePrimaryDetailsDTO.getMedium());
			inspectionType.setLow(inspectionTypePrimaryDetailsDTO.getLow());
			inspectionType.setEntitySize(inspectionTypePrimaryDetailsDTO.getEntitySize());
			
			ControlType controlType = inspectionTypeDAO.findByControlTypeId(inspectionTypePrimaryDetailsDTO.getControlTypeId());
	        if (controlType != null) {
	            inspectionType.setControlTypes(Set.of(controlType));
	        }
			inspectionTypeDAO.save(inspectionType);
		} else {
			throw new IllegalArgumentException("Invalid action: " + action);
		}

		// Handle new skills
		Set<Skill> updatedSkills = new HashSet<>();
		for (String skillName : inspectionTypePrimaryDetailsDTO.getNewSkills()) {
			if (skillName != null && !skillName.isEmpty()) {
				Skill existingSkill = skillDAO.findBySkill(skillName);
				if (existingSkill != null) {
					// Existing skill found, add it to the updated skills set
					updatedSkills.add(existingSkill);
				} else {
					// New skill, create and save
					Skill newSkill = new Skill();
					newSkill.setSkill(skillName);
					newSkill.setActive(true);
					skillDAO.save(newSkill);
					updatedSkills.add(newSkill);

				}
				System.out.println(updatedSkills);
			}

		}

		// Handle existing skills
		for (String skillId : inspectionTypePrimaryDetailsDTO.getExistingSkills()) {
			if (skillId != null && !skillId.isEmpty()) {
				Skill existingSkill = skillDAO.findById(Long.parseLong(skillId));
				existingSkill.getInspectionTypes().add(inspectionType);
				if (existingSkill != null) {
					updatedSkills.add(existingSkill);
				}
			}
		}
		System.out.println(updatedSkills);
		// Associate updated skills with the Inspection Type
		inspectionType.setSkills(updatedSkills);
		inspectionTypeDAO.save(inspectionType);

		return inspectionType.getIns_type_id();
	}

	public InspectionTypeDTO getInspectionTypewithEntity(long insTypeId) {
		// Fetch Inspection_Type by ins_type_id using the DAO
		Inspection_Type inspectionType = inspectionTypeDAO.getInspectionTypeByInspectionId(insTypeId);
		InspectionTypeDTO inspectionTypeDTO = new InspectionTypeDTO();

		if (inspectionType != null) {
			inspectionTypeDTO.setIns_type_id(inspectionType.getIns_type_id());
			inspectionTypeDTO.setName(inspectionType.getName());
			inspectionTypeDTO.setThreshold(inspectionType.getThreshold());

			// Prepare entity details
			List<EntityDetailsDTO> entityDetails = new ArrayList<>();
			for (NewEntity entity : inspectionType.getNewEntities()) {
				EntityDetailsDTO entityDTO = new EntityDetailsDTO();
				entityDTO.setEntityId(entity.getEntityid());
				entityDTO.setName(entity.getName());
				entityDTO.setAddress(entity.getFloor() + ", " + entity.getFacility() + ", " + entity.getAddress());
				entityDetails.add(entityDTO);
			}
			inspectionTypeDTO.setEntityDetails(entityDetails);
		} else {
			// Handle the case where the Inspection_Type is not found
			throw new RuntimeException("Inspection Type not found for ID: " + insTypeId);
		}
		return inspectionTypeDTO;
	}

	public InspectionTypeService(InspectionTypeDAO inspectionTypeDAO) {
		this.inspectionTypeDAO = inspectionTypeDAO;
	}

	public void updateInspectionType(InspectionTypeDTO dto) {
		// Fetch the Inspection_Type entity
		Inspection_Type inspectionType = inspectionTypeDAO.findById(dto.getIns_type_id());
		if (inspectionType == null) {
			throw new RuntimeException("Inspection_Type with ID " + dto.getIns_type_id() + " not found.");
		}

		// Update basic fields
		inspectionType.setName(dto.getName());
		inspectionType.setThreshold(dto.getThreshold());

		// Fetch the NewEntity objects based on the entity IDs provided in the DTO
		Set<NewEntity> updatedEntities = fetchEntitiesByIds(dto.getNewEntities());

		// Update the mapping without modifying entity data
		inspectionType.getNewEntities().clear();
		inspectionType.getNewEntities().addAll(updatedEntities);

		// Persist changes
		inspectionTypeDAO.update(inspectionType);
	}

	private Set<NewEntity> fetchEntitiesByIds(List<String> entityIds) {
		Set<NewEntity> entities = new HashSet<>();
		for (String entityId : entityIds) {
			if (entityId == null || entityId.trim().isEmpty()) {
				throw new IllegalArgumentException("Entity ID cannot be null or empty.");
			}
			NewEntity entity = inspectionTypeDAO.findEntityById(entityId);
			if (entity == null) {
				throw new RuntimeException("Entity with ID " + entityId + " not found.");
			}
			entities.add(entity);
		}
		return entities;
	}

	public InspectionTypeRequestDTO getInspectionTypeByIdForEdit(long inspectionTypeId) {
		InspectionTypeRequestDTO inspectionTypeRequestDTO = new InspectionTypeRequestDTO();
		Inspection_Type inspection_Type = inspectionTypeDAO.findById(inspectionTypeId);

		// Get the primary details
		InspectionTypePrimaryDetailsDTO primaryDetailsDTO = new InspectionTypePrimaryDetailsDTO();
		primaryDetailsDTO.setIns_type_id(inspectionTypeId);
		primaryDetailsDTO.setName(inspection_Type.getName());
		primaryDetailsDTO.setThreshold(inspection_Type.getThreshold());
		primaryDetailsDTO.setHigh(inspection_Type.getHigh());
		primaryDetailsDTO.setMedium(inspection_Type.getMedium());
		primaryDetailsDTO.setLow(inspection_Type.getLow());
		
		 if (inspection_Type.getControlTypes() != null && !inspection_Type.getControlTypes().isEmpty()) {
		        // Assuming ControlType is an entity and you want to get both the id and the name
		        ControlType controlType = inspection_Type.getControlTypes().iterator().next(); // Get the first control type
		        primaryDetailsDTO.setControlTypeName(controlType.getControlTypeName()); // Assuming getControlTypeName() exists in ControlType
		        primaryDetailsDTO.setControlTypeId(controlType.getControlTypeId()); // Assuming getControlTypeId() exists in ControlType
		    } else {
		    	primaryDetailsDTO.setControlTypeName("Not Available"); // Default value if no control type is found
		    	primaryDetailsDTO.setControlTypeId(-1); // Default value, adjust as needed
		    }


		List<SkillDTO> skills = new ArrayList<SkillDTO>();
		skills = inspection_Type.getSkills().stream().map(skill -> {
			SkillDTO skillDTO = new SkillDTO();
			skillDTO.setSkillId(skill.getSkillId());
			skillDTO.setSkill(skill.getSkill());

			List<String> inspectionTypeNames = new ArrayList<String>();
			Set<Inspection_Type> inspection_Types = skill.getInspectionTypes();
			inspectionTypeNames = inspection_Types.stream().map(inspectionType -> inspectionType.getName())
					.collect(Collectors.toList());
			skillDTO.setInspectionTypeNames(inspectionTypeNames);
			return skillDTO;
		}).collect(Collectors.toList());

		primaryDetailsDTO.setSkills(skills);
		inspectionTypeRequestDTO.setInspectionTypePrimaryDetails(primaryDetailsDTO);

		// Get the SLA details
		List<Inspection_SLA> inspection_SLAs = inspection_Type.getInspectionSLAs();
		inspectionTypeRequestDTO.setInspectionSLA(inspection_SLAs);

		// Get the entity mapping
		List<EntityDetailsDTO> entityDetailsDTOs = inspection_Type.getNewEntities().stream().map(entity -> {
			EntityDetailsDTO detailsDTO = new EntityDetailsDTO();
			detailsDTO.setEntityId(entity.getEntityid());
			detailsDTO.setName(entity.getName());
			detailsDTO.setAddress(entity.getFloor() + ", " + entity.getFacility() + ", " + entity.getAddress());
			return detailsDTO;
		}).collect(Collectors.toList());

		inspectionTypeRequestDTO.setEntityDetailsDTOs(entityDetailsDTOs);
		return inspectionTypeRequestDTO;
	}

	public Page<InspectionTypeSkillAdminDTO> getAllInspectionTypesWithSkills(Pageable pageable) {
		Page<Inspection_Type> paginatedInspectionTypes = inspectionTypeDAO.getAllInspectionTypes(pageable);

		// Map the paginated entities to DTOs
		return paginatedInspectionTypes.map(entity -> {
			InspectionTypeSkillAdminDTO dto = new InspectionTypeSkillAdminDTO();

			// Set the Inspection_Type fields
			dto.setInsTypeId(entity.getIns_type_id());
			dto.setName(entity.getName());
			dto.setIsActive(entity.isActive());

			// Map the related skills to a list of skill names (as strings)
			if (entity.getSkills() != null) {
				List<String> skillNames = entity.getSkills().stream().map(Skill::getSkill) // Get skill names
						.collect(Collectors.toList());
				dto.setSkills(skillNames);
			}

			return dto;
		});
	}
	public List<GetAllInspection_TypeDTO> getInspectionIdsAndNamesByControlTypeId(long controlTypeId) {
	    return inspectionTypeDAO.findInspectionIdsAndNamesByControlTypeId(controlTypeId);
	}
	
}