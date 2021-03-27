package com.rjogata.clinicals.util;

import java.util.List;

import com.rjogata.clinicals.model.ClinicalData;

public class BMICalculator {
	
	public static void calculateBMI(List<ClinicalData> clinicalData, ClinicalData eachEntry) {
		if(eachEntry.getComponentValue().equals("hw")) {
		    String [] heightAndWeight = eachEntry.getComponentValue().split("/");
		    
		    if( heightAndWeight != null && heightAndWeight.length > 1) {
		        	float heightInMeters = Float.parseFloat( heightAndWeight[0]) * 0.4536F;
		        	float weight = Float.parseFloat(heightAndWeight[1]);
		        	float bmi = weight/(heightInMeters * heightInMeters);
		        	ClinicalData bmiData = new ClinicalData();
		        	bmiData.setComponentName("bmi");
		        	bmiData.setComponentValue(Float.toString(bmi));
		        	clinicalData.add(bmiData);	            		
		    }
		}
	}

}
