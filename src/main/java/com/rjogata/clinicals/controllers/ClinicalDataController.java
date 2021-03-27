package com.rjogata.clinicals.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rjogata.clinicals.dto.ClinicalDataRequest;
import com.rjogata.clinicals.model.ClinicalData;
import com.rjogata.clinicals.model.Patient;
import com.rjogata.clinicals.repos.ClinicalDataRepository;
import com.rjogata.clinicals.repos.PatientRepository;
import com.rjogata.clinicals.util.BMICalculator;

@RestController
@RequestMapping("/api")
public class ClinicalDataController {
	
	private ClinicalDataRepository clinicalDataRepository;
	
	private PatientRepository patienteRepository;
	
	ClinicalDataController(ClinicalDataRepository clinicalDataRepositoryParam, 
			               PatientRepository patienteRepositoryParam){
		this.clinicalDataRepository = clinicalDataRepositoryParam;
		this.patienteRepository = patienteRepositoryParam;
	}

	@RequestMapping(value = "/clinicals", method = RequestMethod.POST)
	public ClinicalData saveClinicalData(@RequestBody ClinicalDataRequest request) {
		Patient patient = patienteRepository.findById(request.getPatientId()).get();
		ClinicalData clinicalData = new ClinicalData();
		clinicalData.setComponentName(request.getComponentName());
		clinicalData.setComponentValue(request.getComponentValue());
		clinicalData.setPatient(patient);
		return clinicalDataRepository.save(clinicalData);
	}

	@RequestMapping(value = "/clinicals/{patientId}/{componentName}", method = RequestMethod.GET)
	public List<ClinicalData> getClinicalData(@PathVariable("patientId") int patientId, @PathVariable("componentName") String componentName){
		
		if(componentName.equals("bmi")) {
			componentName = "hm";
		}
		
		List<ClinicalData> clinicalData = clinicalDataRepository
				.findByPatientIdAndComponentNameOrderByMeasureDateTime(patientId, componentName);
		List<ClinicalData> duplicateClinicalData = new ArrayList<>(clinicalData);
		
		for(ClinicalData eachEntry: duplicateClinicalData) {
			BMICalculator.calculateBMI(clinicalData, eachEntry);
		}
		return clinicalData;
	}
}
