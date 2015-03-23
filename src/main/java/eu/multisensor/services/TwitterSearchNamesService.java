/**
 Copyright (c) 2014, everis
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
 **/

package eu.multisensor.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import eu.multisensor.dto.SearchNameRequest;
import eu.multisensor.dto.SearchNameResponse;
import eu.multisensor.dto.TwitterSearchNamesForMultisensor;

/**
 * Service for the search of Twitter accounts based on string input.
 * (Possible contributors on social media developed as part of WP3 (Task 3.4).)
 * @author nicolas kourtellis
 */

@Path("TwitterSearchNamesServiceMultiKeys")
public class TwitterSearchNamesService {

	String keys = "";
	
	public TwitterSearchNamesService(String keys){
		// Location of Twitter API keys
		this.keys = keys;
	}

	
	/**
	 * Method to request the search of a person via a name for potential username matches.
	 * @param inRequest name of the Twitter contributor
	 * @return Potential matches of usernames and other relevant info
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
	public SearchNameResponse SearchName(SearchNameRequest request) {

		try {
			
	        // Calling the servince to seach for the particular string
	        TwitterSearchNamesForMultisensor tcfms = new TwitterSearchNamesForMultisensor(request.query, keys);
	        // Return response
			return tcfms.response;

		} catch (Exception ex) {
			System.err.println("ERROR in producing the response for Request\t"+request.toString());
			ex.printStackTrace();
			return null;
		}
	}//SearchName

}//TwitterSearchNamesServiceMultiKeys