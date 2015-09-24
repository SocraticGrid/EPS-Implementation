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
package org.socraticgrid.hl7.services.eps.internal.validatationstep.publication;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.socraticgrid.hl7.services.eps.exceptions.IncompleteDataException;
import org.socraticgrid.hl7.services.eps.exceptions.InvalidDataException;
import org.socraticgrid.hl7.services.eps.exceptions.MediaFormatNotExceptedException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchTopicException;
import org.socraticgrid.hl7.services.eps.internal.interfaces.PublicationValidationStepIFace;
import org.socraticgrid.hl7.services.eps.model.Message;

public class TopicNaming implements PublicationValidationStepIFace {

	private boolean autoCorrect = true;
	
	@Override
	public boolean valdiateMessage(String topic, Message message)
			throws NoSuchTopicException, IncompleteDataException,
			InvalidDataException, MediaFormatNotExceptedException {

		List<String> topics = message.getTopics();
		LinkedList<String> autoCorrectList = new LinkedList<String>();
		
		Iterator<String> itr = topics.iterator();
		while (itr.hasNext())
		{
			String localTopic = itr.next();
			if (localTopic.startsWith("/")== false)
			{
				if (autoCorrect)
				{
					itr.remove();
					autoCorrectList.add("/"+localTopic);
				}
				else
				{
					InvalidDataException exp = new InvalidDataException("Invalid Topic Name, Missing root prefex (/) for topic "+localTopic);
					throw exp;
				}
			}
		}
		if (autoCorrect && autoCorrectList.isEmpty()==false)
		{
			topics.addAll(autoCorrectList);
		}
		return true;
	}

	/**
	 * @return the autoCorrect
	 */
	public boolean isAutoCorrect() {
		return autoCorrect;
	}

	/**
	 * @param autoCorrect the autoCorrect to set
	 */
	public void setAutoCorrect(boolean autoCorrect) {
		this.autoCorrect = autoCorrect;
	}

}
