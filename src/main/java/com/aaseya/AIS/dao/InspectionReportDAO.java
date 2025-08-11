package com.aaseya.AIS.dao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.aaseya.AIS.Model.InspectionCase;
import com.aaseya.AIS.Model.InspectionReport;
import com.aaseya.AIS.Model.NewEntity;
import com.aaseya.AIS.dto.EntityInspectionCasesReportResponseDTO;
import com.aaseya.AIS.dto.EntityInspectionReportDTO;
import com.aaseya.AIS.dto.EntityRequestDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class InspectionReportDAO {
    
    @PersistenceContext
    private EntityManager entityManager;

    public void saveInspectionReport(InspectionReport inspectionReport) {
        entityManager.persist(inspectionReport);
    }

    public List<InspectionReport> findByInspectionId(Long inspectionId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<InspectionReport> cq = cb.createQuery(InspectionReport.class);
        Root<InspectionReport> root = cq.from(InspectionReport.class);
        cq.where(cb.equal(root.get("inspectionId"), inspectionId));
        return entityManager.createQuery(cq).getResultList();
    }
    
    
    public NewEntity getEntityReportById(String entityId) {
	    // Create CriteriaBuilder instance
	    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

	    // Create CriteriaQuery instance for NewEntity
	    CriteriaQuery<NewEntity> criteriaQuery = criteriaBuilder.createQuery(NewEntity.class);

	    // Define root (from NewEntity)
	    Root<NewEntity> root = criteriaQuery.from(NewEntity.class);

	    // Define where clause: where entityid = :entityId
	    criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("entityid"), entityId));

	    try {
	        // Execute query and return result
	        NewEntity result = entityManager.createQuery(criteriaQuery).getSingleResult();
	        System.out.println("Found Entity: " + result); // Log the result
	        return result;
	    } catch (NoResultException e) {
	        System.out.println("No entity found for ID: " + entityId); // Log the error
	        throw new EntityNotFoundException("Entity with ID " + entityId + " not found");
	    }
	}

    
    public EntityInspectionReportDTO getInspectionReportByEntityAndDate(EntityRequestDTO requestDTO) {

	    // Create CriteriaBuilder instance
	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();

	    // Create CriteriaQuery instance for Object[] (we want to select multiple fields)
	    CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

	    // Retrieve the entity using the entityId
	    NewEntity newEntity = entityManager.find(NewEntity.class, requestDTO.getEntityId());
	    if (newEntity == null) {
	        System.out.println("Entity not found");
	        return new EntityInspectionReportDTO();
	    }

	    // Define root for InspectionCase
	    Root<InspectionCase> root = query.from(InspectionCase.class);

	    // Create predicates
	    Predicate entityPredicate = cb.equal(root.get("entity"), newEntity);

	    Predicate datePredicate = cb.conjunction();  // Default to a true condition
	    if (requestDTO.getStartdate() != null && requestDTO.getEnddate() != null) {
	        LocalDate start = LocalDate.parse(requestDTO.getStartdate());
	        LocalDate end = LocalDate.parse(requestDTO.getEnddate());

	        // Convert the string field "dateOfInspection" to a date for filtering
	        Expression<LocalDate> dateExpr = cb.function("TO_DATE", LocalDate.class, root.get("dateOfInspection"), cb.literal("YYYY-MM-DD"));
	        datePredicate = cb.between(dateExpr, start, end);
	    }

	    // Apply filters and group by inspection type
	    query.multiselect(root.get("inspectionType"), cb.count(root))
	         .where(cb.and(entityPredicate, datePredicate))
	         .groupBy(root.get("inspectionType"));

	    List<Object[]> resultList = entityManager.createQuery(query).getResultList();

	    // Prepare the response DTO
	    EntityInspectionReportDTO reportDTO = new EntityInspectionReportDTO();

	    // Create the map to store the cases by inspection type
	    Map<String, Long> casesByInspectionType = new HashMap<>();
//	    Map<String, Long> casesBySource = new HashMap<>();
//	    Map<String, Long> casesByStatus = new HashMap<>();

	    // Assuming resultList contains the results of the query where index 0 is inspectionType and index 1 is count
	    for (Object[] result : resultList) {
	        // Get inspectionType (result[0]) and count (result[1])
	        String inspectionType = (String) result[0];
	        Long count = ((Number) result[1]).longValue(); // Convert count to Long
	        

	        // Add to the map for inspectionType
	        casesByInspectionType.put(inspectionType, count);
	        System.out.println("Inspection Type: " + inspectionType + ", Count: " + count);

	       
	    }

	    // Set the maps to the DTO
	    reportDTO.setCasesByInspectionType(casesByInspectionType);
	

	    // Define the second query for InspectionCase
	    CriteriaBuilder cb1 = entityManager.getCriteriaBuilder();
	    CriteriaQuery<InspectionCase> query1 = cb1.createQuery(InspectionCase.class);
	    Root<InspectionCase> root1 = query1.from(InspectionCase.class);

	    // Create predicates
	    Predicate entityPredicate1 = cb1.equal(root1.get("entity").get("id"), requestDTO.getEntityId()); // Assuming entity has an ID field
	    Predicate datePredicate1 = cb1.conjunction();  // Default to a true condition

	    if (requestDTO.getStartdate() != null && requestDTO.getEnddate() != null) {
	        LocalDate start = LocalDate.parse(requestDTO.getStartdate());
	        LocalDate end = LocalDate.parse(requestDTO.getEnddate());

	        Expression<LocalDate> dateExpr = cb1.function("TO_DATE", LocalDate.class, root1.get("dateOfInspection"), cb1.literal("YYYY-MM-DD"));
	        datePredicate1 = cb1.between(dateExpr, start, end);
	    }

	    query1.select(root1)
	        .where(cb1.and(entityPredicate1, datePredicate1));
      List<InspectionCase> inspectionCases = entityManager.createQuery(query1).getResultList();
      
      Map<String, Long> casesByStatus = inspectionCases.stream()
    		  .filter(inspectionCase -> inspectionCase.getStatus() != null)
    		    .collect(Collectors.groupingBy(
    		        InspectionCase::getStatus, // Group by status
    		        Collectors.counting() // Count occurrences
    		    ));
      
      Map<String, Long> casesBySource = inspectionCases.stream()
    		  .filter(inspectionCase -> inspectionCase.getInspector_source() != null)
    		    .collect(Collectors.groupingBy(
    		       InspectionCase::getInspector_source             , // Group by source
    		        Collectors.counting() // Count occurrences
    		    ));

    		// Set status and source data in DTO
    		reportDTO.setCasesByStatus(casesByStatus);
    		reportDTO.setCasesBySource(casesBySource);
	    // Return the InspectionCase list
	   // return entityManager.createQuery(query1).getResultList();
    		
	   return reportDTO; 
	}




	







	public List<EntityInspectionCasesReportResponseDTO> getInspectionCases(EntityRequestDTO requestDTO) {
	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<InspectionCase> query = cb.createQuery(InspectionCase.class);
	    Root<InspectionCase> root = query.from(InspectionCase.class);

	    // Create predicates
	    Predicate entityPredicate = cb.equal(root.get("entity").get("id"), requestDTO.getEntityId()); // Assuming entity has an ID field
	    Predicate datePredicate = cb.conjunction();  // Default to a true condition

	    if (requestDTO.getStartdate() != null && requestDTO.getEnddate() != null) {
	        LocalDate start = LocalDate.parse(requestDTO.getStartdate());
	        LocalDate end = LocalDate.parse(requestDTO.getEnddate());

	        Expression<LocalDate> dateExpr = cb.function("TO_DATE", LocalDate.class, root.get("dateOfInspection"), cb.literal("YYYY-MM-DD"));
	        datePredicate = cb.between(dateExpr, start, end);
	    }

	    query.select(root)
	         .where(cb.and(entityPredicate, datePredicate));

	    List<InspectionCase> inspectionCases = entityManager.createQuery(query).getResultList();

	    // Convert to DTO List
	    List<EntityInspectionCasesReportResponseDTO> responseDTOs = inspectionCases.stream()
	        .map(ic -> {
	            EntityInspectionCasesReportResponseDTO dto = new EntityInspectionCasesReportResponseDTO();
	            dto.setCaseId(ic.getInspectionID());
	            dto.setInspectionType(ic.getInspectionType());
	            dto.setInspectionDate(ic.getDateOfInspection().toString()); // Convert LocalDate to String
	            dto.setInspectionSource(ic.getInspector_source());
	            dto.setStatus(ic.getStatus());
	            return dto;
	        })
	        .collect(Collectors.toList());

	    return responseDTOs;
	}
}

