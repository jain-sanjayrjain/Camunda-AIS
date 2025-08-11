package com.aaseya.AIS.dao;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.aaseya.AIS.Model.Checklist_Category;
import com.aaseya.AIS.Model.Checklist_Item;
import com.aaseya.AIS.Model.InspectionChecklistandAnswers;
import com.aaseya.AIS.Model.Inspection_Type;
import com.aaseya.AIS.Model.Template;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

@Repository
public class CategoriesSummaryReportDAO {

	@PersistenceContext
	private EntityManager entityManager;

	public List<Object[]> getTop5CategoriesWithNegativeObservations() {
	    // Create CriteriaBuilder and CriteriaQuery for fetching categories
	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

	    // Root for Checklist_Category
	    Root<Checklist_Category> categoryRoot = query.from(Checklist_Category.class);

	    // Create subquery to count negative observations (e.g., "No" answers)
	    Subquery<Long> negativeCountSubquery = query.subquery(Long.class);
	    Root<InspectionChecklistandAnswers> answersRoot = negativeCountSubquery
	            .from(InspectionChecklistandAnswers.class);

	    // Match InspectionChecklistandAnswers with Checklist_Category using categoryID from embedded ID
	    Predicate categoryMatchPredicate = cb.equal(answersRoot.get("id").get("categoryID"), categoryRoot.get("id"));

	    // Condition: Only count "No" (negative) selected answers
	    Predicate negativeAnswerPredicate = cb.or(cb.equal(answersRoot.get("selected_answer"), "No"),
	            cb.equal(answersRoot.get("selected_answer"), "disagree"),
	            cb.equal(answersRoot.get("selected_answer"), "Non-Complaint"),
	            cb.equal(answersRoot.get("selected_answer"), "Fail"));

	    // Subquery: Count negative observations per category
	    negativeCountSubquery.select(cb.count(answersRoot))
	            .where(cb.and(negativeAnswerPredicate, categoryMatchPredicate));

	    // Main query: Select category name + negative answer count
	    query.multiselect(categoryRoot.get("checklist_category_name"), // Category name
	            negativeCountSubquery.getSelection()) // Count of negative answers
	            .where(cb.greaterThan(negativeCountSubquery.getSelection(), cb.literal(0L))) // Ensure at least 1 negative observation
	            .orderBy(cb.desc(negativeCountSubquery.getSelection())); // Sort by the count of negative answers

	    // Limit to top 5 results
	    TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
	    typedQuery.setMaxResults(5);

	    // Execute the query and return the result
	    return typedQuery.getResultList();
	}
	public List<Object[]> getTop10CategoriesWithNegativeObservations() {
	    System.out.println("DAO: Fetching top 10 categories with negative observations...");

	    // Create CriteriaBuilder
	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

	    // Root for Checklist_Category
	    Root<Checklist_Category> categoryRoot = query.from(Checklist_Category.class);

	    // Join Checklist_Category with Checklist_Item
	    Join<Checklist_Category, Checklist_Item> checklistItemJoin = categoryRoot.join("checklist_items", JoinType.LEFT);

	    // Subquery to count negative observations
	    Subquery<Long> negativeCountSubquery = query.subquery(Long.class);
	    Root<InspectionChecklistandAnswers> answersRoot = negativeCountSubquery
	            .from(InspectionChecklistandAnswers.class);

	    // Join conditions: Matching category and checklist item
	    Predicate categoryMatchPredicate = cb.equal(answersRoot.get("id").get("categoryID"),
	            categoryRoot.get("checklist_cat_id"));
	    Predicate checklistMatchPredicate = cb.equal(answersRoot.get("id").get("checklistID"),
	            checklistItemJoin.get("checklist_id"));

	    // Negative answer conditions
	    Predicate negativeAnswerPredicate = cb.or(cb.equal(answersRoot.get("selected_answer"), "No"),
	            cb.equal(answersRoot.get("selected_answer"), "disagree"),
	            cb.equal(answersRoot.get("selected_answer"), "Non-Complaint"),
	            cb.equal(answersRoot.get("selected_answer"), "Fail"));

	    // Subquery: Count negative answers
	    negativeCountSubquery.select(cb.count(answersRoot))
	            .where(cb.and(negativeAnswerPredicate, categoryMatchPredicate, checklistMatchPredicate));

	    // Main query: Select category name, checklist item name, and negative answer count
	    query.multiselect(categoryRoot.get("checklist_category_name"), checklistItemJoin.get("checklist_name"),
	            negativeCountSubquery.getSelection()).where(cb.greaterThan(negativeCountSubquery.getSelection(), cb.literal(0L))) // Ensure at least 1 negative observation
	            .orderBy(cb.desc(negativeCountSubquery.getSelection())); // Sort by the count of negative answers

	    // Limit to top 10 results
	    TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
	    typedQuery.setMaxResults(10);

	    // Execute the query
	    List<Object[]> results = typedQuery.getResultList();

	    System.out.println("DAO: Query executed successfully. Results size: " + results.size());

	    for (Object[] obj : results) {
	        System.out.println("DAO Result: Category = " + obj[0] + ", Checklist Item = " + obj[1]
	                + ", Negative Count = " + obj[2]);
	    }

	    return results;
	}


	public List<Object[]> getTop5CategoriesWithNegativeObservations(Long ins_Type_Id) {
	    // Fetch the InspectionType associated with the given ins_Type_Id
	    Inspection_Type inspection_Type = entityManager.find(Inspection_Type.class, ins_Type_Id);
	    if (inspection_Type == null) {
	        return Collections.emptyList(); // Return an empty list if the inspection type is not found
	    }

	    // Get the list of templates associated with this inspection type
	    Set<Template> templates = inspection_Type.getTemplates();
	    if (templates == null || templates.isEmpty()) {
	        return Collections.emptyList(); // Return an empty list if no templates are found
	    }

	    // Store all checklist categories associated with these templates
	    Set<Long> categoryIds = new HashSet<>();

	    // Iterate through each template and collect the checklist categories
	    for (Template template : templates) {
	        List<Checklist_Category> checklistCategories = template.getChecklist_categorys(); // Get checklist categories for each template
	        if (checklistCategories != null && !checklistCategories.isEmpty()) {
	            for (Checklist_Category category : checklistCategories) {
	                categoryIds.add(category.getChecklist_cat_id()); // Add the checklist category ID to the list
	            }
	        }
	    }

	    if (categoryIds.isEmpty()) {
	        return Collections.emptyList(); // Return an empty list if no checklist categories are found
	    }

	    // Create CriteriaBuilder and CriteriaQuery for fetching categories
	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<Object[]> mainQuery = cb.createQuery(Object[].class);

	    // Root for Checklist_Category
	    Root<Checklist_Category> categoryRoot = mainQuery.from(Checklist_Category.class);

	    // Subquery: Count negative observations per category
	    Subquery<Long> negativeCountSubquery = mainQuery.subquery(Long.class);
	    Root<InspectionChecklistandAnswers> answersRoot = negativeCountSubquery.from(InspectionChecklistandAnswers.class);

	    // Join condition for matching category ID
	    Predicate categoryMatchPredicate1 = cb.equal(answersRoot.get("id").get("categoryID"), categoryRoot.get("checklist_cat_id"));

	    // Condition: Only count negative selected answers (e.g., "No", "disagree", "Fail")
	    Predicate negativeAnswerPredicate = cb.or(
	        cb.equal(answersRoot.get("selected_answer"), "No"),
	        cb.equal(answersRoot.get("selected_answer"), "disagree"),
	        cb.equal(answersRoot.get("selected_answer"), "Non-Complaint"),
	        cb.equal(answersRoot.get("selected_answer"), "Fail")
	    );

	    // Subquery: Select the count of negative answers
	    negativeCountSubquery.select(cb.count(answersRoot))
	            .where(cb.and(negativeAnswerPredicate, categoryMatchPredicate1));

	    // Main query: Select category name and the count of negative answers
	    mainQuery.multiselect(
	        categoryRoot.get("checklist_category_name"), // Category name
	        negativeCountSubquery.getSelection() // Count of negative answers
	    )
	    .where(cb.and(categoryRoot.get("checklist_cat_id").in(categoryIds),cb.greaterThan(negativeCountSubquery.getSelection(), 0L))) // Match categories with the given IDs
	   
	    .orderBy(cb.desc(negativeCountSubquery.getSelection())); // Sort by negative count in descending order

	    // Limit to top 5 results
	    TypedQuery<Object[]> typedQuery = entityManager.createQuery(mainQuery);
	    typedQuery.setMaxResults(5);

	    // Execute the query and return the result
	    return typedQuery.getResultList();
	}

	public List<Object[]> getTop10CategoriesWithNegativeObservations(Long ins_Type_Id) {
	    // Fetch the InspectionType associated with the given insTypeId
	    Inspection_Type inspection_Type = entityManager.find(Inspection_Type.class, ins_Type_Id);
	    if (inspection_Type == null) {
	        return Collections.emptyList();
	    }

	    // Get the list of templates associated with this inspection type
	    Set<Template> templates = inspection_Type.getTemplates();
	    if (templates == null || templates.isEmpty()) {
	        return Collections.emptyList();
	    }

	    // Store all checklist categories associated with these templates
	    Set<Long> categoryIds = new HashSet<>();

	    // Collect checklist category IDs from each template
	    for (Template template : templates) {
	        List<Checklist_Category> checklistCategories = template.getChecklist_categorys();
	        if (checklistCategories != null && !checklistCategories.isEmpty()) {
	            for (Checklist_Category category : checklistCategories) {
	                categoryIds.add(category.getChecklist_cat_id());
	            }
	        }
	    }

	    if (categoryIds.isEmpty()) {
	        return Collections.emptyList();
	    }

	    // Create CriteriaBuilder and CriteriaQuery for fetching categories
	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<Object[]> mainQuery = cb.createQuery(Object[].class);

	    // Root for Checklist_Category
	    Root<Checklist_Category> categoryRoot = mainQuery.from(Checklist_Category.class);
	    
	    // Join Checklist_Category with Checklist_Item
	    Join<Checklist_Category, Checklist_Item> checklistItemJoin = categoryRoot.join("checklist_items", JoinType.LEFT);

	    // Match only checklist categories related to the templates
	    Predicate categoryMatchPredicate = categoryRoot.get("checklist_cat_id").in(categoryIds);

	    // Subquery: Count negative observations per category
	    Subquery<Long> negativeCountSubquery = mainQuery.subquery(Long.class);
	    Root<InspectionChecklistandAnswers> answersRoot = negativeCountSubquery.from(InspectionChecklistandAnswers.class);

	    // Join conditions: Matching category and checklist item
	    Predicate categoryMatchPredicate1 = cb.equal(answersRoot.get("id").get("categoryID"),
	            categoryRoot.get("checklist_cat_id"));
	    Predicate checklistMatchPredicate = cb.equal(answersRoot.get("id").get("checklistID"),
	            checklistItemJoin.get("checklist_id"));

	    // Negative answer conditions
	    Predicate negativeAnswerPredicate = cb.or(
	        cb.equal(answersRoot.get("selected_answer"), "No"),
	        cb.equal(answersRoot.get("selected_answer"), "disagree"),
	        cb.equal(answersRoot.get("selected_answer"), "Non-Complaint"),
	        cb.equal(answersRoot.get("selected_answer"), "Fail")
	    );

	    // Subquery: Count negative answers
	    negativeCountSubquery.select(cb.count(answersRoot))
	            .where(cb.and(negativeAnswerPredicate, categoryMatchPredicate1, checklistMatchPredicate));

	    // Alias for the subquery result
	    Expression<Long> negativeCount = negativeCountSubquery.getSelection();

	    // Main query: Select category name + negative answer count, filtering out zero counts
	    mainQuery.multiselect(
	        categoryRoot.get("checklist_category_name"), // Category name
	        checklistItemJoin.get("checklist_name"),
	        negativeCountSubquery.getSelection() // Count of negative answers
	    )
	    .where(cb.and(categoryMatchPredicate, cb.greaterThan( negativeCountSubquery.getSelection(), cb.literal(0L)))) // ✅ Fix applied here
	    .orderBy(cb.desc(negativeCountSubquery.getSelection())); 

	    // Limit to top 10 results
	    TypedQuery<Object[]> typedQuery = entityManager.createQuery(mainQuery);
	    typedQuery.setMaxResults(10);

	    // Execute the query and return the result
	    return typedQuery.getResultList();
	}
}