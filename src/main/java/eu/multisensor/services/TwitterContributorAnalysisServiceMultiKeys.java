/**
 Copyright (c) 2015 Nicolas Kourtellis
 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 disclaimer in the documentation and/or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 Additional Disclaimer:

 This code was tested on Linux and Mac-based systems and works appropriately. As mentioned above, please use at your own risk. We cannot provide any sort of guarantees that it will work on your platform $
 If you use this software and its relevant feeatures, please make sure to acknowledge the EU project MULTISENSOR, grant num: 610411.

 **/

package eu.multisensor.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import eu.multisensor.dto.ContributorAnalysisRequest;
import eu.multisensor.dto.ContributorAnalysisResponse;
import eu.multisensor.dto.TwitterCrawlerForMultisensorMultiKeys;

/**
 * Service for the analysis of contributors on social media developed as part of WP3 (Task 3.4).
 * @author nicola barbieri
 * @author nicolas kourtellis
 */

@Path("TwitterContributorAnalysisService")
public class TwitterContributorAnalysisServiceMultiKeys {

	String keys = "";
	String interests = "";
	String influences = "";
	int accuracy = 3;
	
	public TwitterContributorAnalysisServiceMultiKeys(String keys, String interests, String influences, int accuracy){
		// Location of Twitter API keys
		this.keys = keys;
        // Location of Twitter interests associated with accounts. Precrawled.
		this.interests = interests;
		// Location of file of influences for already crawled accounts
        this.influences = influences;
        // Number from 1 to 5 to control accuracy of friend crawling, i.e., interest estimation;
        this.accuracy = accuracy;

	}
	
	/**
	 * Method to request the analysis of the contributor
	 * @param inRequest id Twitter or the contributor
	 * @return influence score and interest profile of the contributor
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
	public ContributorAnalysisResponse performAnalysis(ContributorAnalysisRequest request) {

		try {
			TwitterCrawlerForMultisensorMultiKeys tcfms = new TwitterCrawlerForMultisensorMultiKeys(request.screenname, keys, interests, influences, accuracy);
			
			return tcfms.response;

		} catch (Exception ex) {
			System.err.println("ERROR in producing the response for Request\t"+request.toString());
			ex.printStackTrace();
			return null;
		}
	}//performAnalysis

}//TwitterContributorAnalysisService
