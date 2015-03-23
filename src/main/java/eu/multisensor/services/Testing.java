package eu.multisensor.services;

import twitter4j.User;
import eu.multisensor.dto.ContributorAnalysisRequest;
import eu.multisensor.dto.ContributorAnalysisResponse;
import eu.multisensor.dto.SearchNameRequest;
import eu.multisensor.dto.SearchNameResponse;

public class Testing {

	public static void main(String[] args) {

		int test = 2;

		if(test==1){
			
			// Initialization of variables in constructor
			String keys = "/Users/kourtell/Desktop/MULTISENSOR/trunk/wp3/ms-svc-contributorAnalysis/keys";
			String interests = "/Users/kourtell/Desktop/MULTISENSOR/trunk/wp3/ms-svc-contributorAnalysis/interests_users";
			String influences = "/Users/kourtell/Desktop/MULTISENSOR/trunk/wp3/ms-svc-contributorAnalysis/influence.txt";
			int accuracy = 2;
			
			TwitterContributorAnalysisServiceMultiKeys tcas = new TwitterContributorAnalysisServiceMultiKeys(keys,interests,influences,accuracy);
			
			// Test for the Twitter crawler
			ContributorAnalysisRequest request = new ContributorAnalysisRequest();
			request.screenname = "@barackobama";
			ContributorAnalysisResponse response = tcas.performAnalysis(request);
			
			System.out.println("NAME: "+response.name);
			System.out.println("ID: "+response.id);
			System.out.println("LANG: "+response.language);
			System.out.println("DESCRIPTION: "+response.description);
			System.out.println("SCREENNAME: "+response.screenname);
			System.out.println("LOCATION: "+response.location);
			System.out.println("FOLLOWERS: "+response.nFollowers);
			System.out.println("FRIENDS: "+response.nFollowings);
			System.out.println("TWEETS: "+response.nTweets);
			System.out.println("INTERESTS: "+response.interests_percentage.toString().replace(", ", "\n"));
			System.out.println("RET. INFLUENCE: "+response.retweet_influence_score);
			System.out.println("NET. INFLUENCE: "+response.network_influence_score);
			System.out.println("MEN. INFLUENCE: "+response.mention_influence_score);
		}
		
		if(test==2){
			//Test for the Twitter search of usernames
			String keys = "/Users/kourtell/Desktop/MULTISENSOR/trunk/wp3/ms-svc-contributorAnalysis/keys";
			
			TwitterSearchNamesService tcas = new TwitterSearchNamesService(keys);
			SearchNameRequest searchrequest = new SearchNameRequest();
			searchrequest.query = "barack obama";
			SearchNameResponse searchresponse = tcas.SearchName(searchrequest);

			int max=10;
			int i=1;
			if(searchresponse.users.size()>max)
				System.out.println("The query returned from that 10 results. Printing the first 10 most relevant: ");
			System.out.println("i\tName\tUsername\tID");
			for(User u: searchresponse.users){
				if(i<=max)
					System.out.println(i+++"\t"+u.getName()+"\t"+u.getScreenName()+"\t"+u.getId());
				else
					break;
			}
		}
		
	}

}
