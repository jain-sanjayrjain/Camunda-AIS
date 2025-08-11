package com.aaseya.AIS.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aaseya.AIS.dao.CategoriesSummaryReportDAO;
import com.aaseya.AIS.dto.TopTenNegativeObservationsDTO;

@Service
public class CategoriesSummaryReportService {
	 @Autowired
	    private CategoriesSummaryReportDAO categoriesSummaryReportDAO;

	 
	 public List<TopTenNegativeObservationsDTO> getTop10NegativeObservations(Long ins_Type_Id) {
		    System.out.println("Service: Fetching top 10 negative observations for InspectionTypeId: " + ins_Type_Id);

		    List<Object[]> results;
		    
		    // Check if ins_Type_Id is provided or not
		    if (ins_Type_Id != null) {
		        results = categoriesSummaryReportDAO.getTop10CategoriesWithNegativeObservations(ins_Type_Id);
		    } else {
		        results = categoriesSummaryReportDAO.getTop10CategoriesWithNegativeObservations();
		    }

		    List<TopTenNegativeObservationsDTO> topNegativeObservations = new ArrayList<>();
		    int sno = 1;

		    for (Object[] obj : results) {
		        TopTenNegativeObservationsDTO dto = new TopTenNegativeObservationsDTO();
		        dto.setSno((long) sno++);
		        dto.setCategoryName((String) obj[0]);
		        dto.setChecklistItemName((String) obj[1]);
		        dto.setNegativeCount((Long) obj[2]);

		        

		        topNegativeObservations.add(dto);
		    }

		    return topNegativeObservations;
		}


	 
	 
	 public Map<String, Long> getTopNegativeCategories(Long ins_Type_Id) {
		    List<Object[]> result;
		    
		    if (ins_Type_Id != null) {
		        result = categoriesSummaryReportDAO.getTop5CategoriesWithNegativeObservations(ins_Type_Id);
		    } else {
		        result = categoriesSummaryReportDAO.getTop5CategoriesWithNegativeObservations();
		    }

		    Map<String, Long> topCategories = new HashMap<>();
		    for (Object[] obj : result) {
		        topCategories.put((String) obj[0], (Long) obj[1]);
		    }
		    
		    return topCategories;
		}
	}