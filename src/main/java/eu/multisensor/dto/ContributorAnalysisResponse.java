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

package eu.multisensor.dto;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContributorAnalysisResponse {

	@JsonProperty
	public String name = "";

	@JsonProperty
	public long id = 0;

	@JsonProperty
	public String screenname = "";

	@JsonProperty
	public String description = "";

	@JsonProperty
	public String location = "";
	
	@JsonProperty
	public String language = "";
	
	@JsonProperty
	public int nFollowers = 0;
	
	@JsonProperty
	public int nFollowings = 0;

	@JsonProperty
	public int nTweets = 0;

	@JsonProperty
	public double network_influence_score = 0;

	@JsonProperty
	public double retweet_influence_score = 0;

	@JsonProperty
	public double mention_influence_score = 0;
	
	@JsonProperty
	public HashMap<String,Double> interests_percentage;

//	@JsonProperty
//	public int nRetweets = 0;
	
//	@JsonProperty
//	public List<String> brands;

}//ContributorAnalysisResponse
