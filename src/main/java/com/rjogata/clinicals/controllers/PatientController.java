	package com.rjogata.clinicals.controllers;
	
	import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rjogata.clinicals.model.ClinicalData;
import com.rjogata.clinicals.model.Patient;
import com.rjogata.clinicals.repos.PatientRepository;
import com.rjogata.clinicals.util.BMICalculator;
	
	@RestController
	@RequestMapping("/api")
	public class PatientController {
	
		private PatientRepository repos;
		Map< String, String > filters = new HashMap<>();
		
		
		@Autowired
		public PatientController(PatientRepository repository) {
			this.repos = repository;
		}
		
		@RequestMapping(value="/patients", method=RequestMethod.GET)
		public List<Patient> getPatients(){
			return repos.findAll();
		}

		@RequestMapping(value="/patients/{id}", method=RequestMethod.GET)		
		public Patient getPatient(@PathVariable("id") int id) {
			return repos.findById(id).get();
		}

		@RequestMapping(value="/patients", method=RequestMethod.POST)		
		public Patient savePatient (@RequestBody Patient patient) {
			return repos.save(patient);
		}

		@RequestMapping(value="/patients/analyze/{id}", method=RequestMethod.GET)		
		public Patient analyze(@PathVariable("id") int patientId) {
			Patient patient = repos.findById(patientId).get();
			List<ClinicalData> clinicalData = patient.getClinicalData();
			List<ClinicalData> duplicateClinicalData = new ArrayList<>(clinicalData);
			
			for(ClinicalData eachEntry: duplicateClinicalData) {
	            /* if(eachEntry.getComponentValue().equals("hw")) {   hw = height and weight  h/w */
				if(filters.containsKey(eachEntry.getComponentName())) {
					clinicalData.remove(eachEntry);
					continue;
				} else {
					filters.put(eachEntry.getComponentName(), null);
				}
				
				BMICalculator.calculateBMI(clinicalData, eachEntry);
	        }
			filters.clear();
            return patient;
		}


	}
