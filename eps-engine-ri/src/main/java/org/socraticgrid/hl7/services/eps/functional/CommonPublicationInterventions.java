/* 
 * Copyright 2015 Cognitive Medical Systems, Inc (http://www.cognitivemedciine.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.socraticgrid.hl7.services.eps.functional;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.socraticgrid.hl7.services.eps.interfaces.PSPublicationInterventionIFace;

public class CommonPublicationInterventions {

	private List<PSPublicationInterventionIFace> interventions;

	/**
	 * @return the interventions
	 */
	public List<PSPublicationInterventionIFace> getInterventions() {
		return interventions;
	}

	/**
	 * @param interventions the interventions to set
	 */
	public void setInterventions(List<PSPublicationInterventionIFace> interventions) {
		this.interventions = interventions;
	}
	
	@PostConstruct
	
	protected void initialize()
	{
		if (interventions == null)
		{
			interventions = new LinkedList<PSPublicationInterventionIFace>();
		}
	}
	
}
