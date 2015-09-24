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
package org.socraticgrid.hl7.services.eps.internal;

import java.util.StringTokenizer;

import org.socraticgrid.hl7.services.eps.internal.interfaces.TopicIFace;
import org.socraticgrid.hl7.services.eps.internal.interfaces.TopicLocatorIFace;

public class StaticTopicTree implements TopicLocatorIFace {

	private TopicIFace rootTopic;
	
	@Override
	public TopicIFace locateTopic(String path) {
		
		TopicIFace cur = rootTopic;
		//Handle Root case
		if (path.compareTo("/")!=0)
		{
			//Loop down each element
			StringTokenizer tok = new StringTokenizer(path,"/");
			while(tok.hasMoreTokens())
			{
				String name = tok.nextToken();
				cur = cur.getSubTopic(name);
				if (cur == null)
				{
					break;
				}
			}
		}
		return cur;
	}

	/**
	 * @return the rootTopic
	 */
	public TopicIFace getRootTopic() {
		return rootTopic;
	}

	/**
	 * @param rootTopic the rootTopic to set
	 */
	public void setRootTopic(TopicIFace rootTopic) {
		this.rootTopic = rootTopic;
	}

}
