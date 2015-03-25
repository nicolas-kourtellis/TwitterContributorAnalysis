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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Random;

import twitter4j.IDs;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.Paging;
import eu.multisensor.dto.ContributorAnalysisResponse;

public class TwitterCrawlerForMultisensorMultiKeys {

    private Twitter twitter = null;
    public int numkeys = 0;
    public static int maxfriendsreturned = 5000;
    public static int maxfriendsrequests = 15;

    public static int maxretweetersreturned = 100;
    public static int maxretweetsrequests = 60;
    public static int maxtweetsrequests = 60;
    private BufferedReader interests;
    private BufferedReader influence;
    private Map<Integer, Integer> errorMap = new HashMap<Integer, Integer>();

    private static final int MAX_RETRIES = 5;
    // state variables
    private String currentUserHandler;
    
    private IDs currentFriends;
    private int consecutiveErrors;
    private int consecutiveErrorsRate;
    private boolean retry;
    private HashMap<String,ArrayList<Long>> interest_users = new HashMap<String,ArrayList<Long>>();
    private HashMap<String,String> user_influence = new HashMap<String,String>();

    public ContributorAnalysisResponse response = new ContributorAnalysisResponse();
    
    private int currentpage = 1;
    private ArrayList<Long> sampledfriends = new ArrayList<Long>();
    private int accuracy = 1;

    
    public TwitterCrawlerForMultisensorMultiKeys(String screenname, String keys, String interests, String influences, int accuracy) throws IOException {
    	
    	// Initialize the API keys to use.
    	numkeys = UtilsMultiKeys.getKeys(keys);
    	// Initialize the Twitter instantiation to use
    	this.twitter = UtilsMultiKeys.getNextTwitterInstance();
        this.interests = new BufferedReader(new FileReader(interests));
        this.influence = new BufferedReader(new FileReader(influences));

        // Define the username to use
        if(!screenname.equals(null))
        	currentUserHandler = screenname.split("@")[1];
        currentFriends = null;
        resetRetry();
        
        this.read_interests();
        this.read_influences();
        
        // If accuracy is set correctly, define how many keys to use.
        if(accuracy<=5 && accuracy>0)
        	this.accuracy = (int)Math.floor(accuracy*numkeys/5);

        // Keep repeating while inside the API rate boundaries
        // Or if not reached the error rate.
        try {
        	System.out.println("Crawling friends of user " + screenname);
        	boolean done;
        	do {
        		done = false;
        		while (!done && this.retry()) {
        			try {
        				done = this.crawlProfile(influences);
        			} catch (TwitterException e) {
        				this.handleTwitterException(e);
        			}
        			if(done)
        				this.retry=false;
        		}
        	} while (this.hasMorePages() && this.retry());
        	if (!done) {
        		System.out.println("Could not access friends of " + this.getCurrentUserHandler());
        	}
        } finally {
        }
    }

    // Influence scores are stored like this:
    // screenname\tinfluencescore1_influencescore2
    public void read_influences() throws IOException{
        String line = influence.readLine();
        while(line!=null){
        	String[] infls = line.split("\t");
        	String screenname = infls[0];
        	String scores = infls[1]+"_"+infls[2];
        	user_influence.put(screenname, scores);
        	line = influence.readLine();
        }
        influence.close();
    }

    // Interests are stored like this:
    // CategoryName\tCategoryFull\tNumUsers\tUserId1\tScreenName1\tUserId2\tScreenName2...
    public void read_interests() throws IOException {
        String line = interests.readLine();
        while(line!=null){
        	String[] categories = line.split("\t");
        	String c = categories[1];
//        	int num = Integer.parseInt(categories[3]);
        	for(int i=4;i<44;i+=2 ){
        		long userid = Long.parseLong(categories[i]);
//        		String username = categories[i+1];
        		
        		if(!interest_users.containsKey(c)) {
        			ArrayList<Long> temp = new ArrayList<Long>();
        			temp.add(userid);
        			interest_users.put(c,temp);
//        			user_screenname.put(userid, username);
        		} else {
        			if(!interest_users.get(c).contains(userid)){
        				interest_users.get(c).add(userid);
//            			user_screenname.put(userid, username);
        			}
        		}
        	}
        	line = interests.readLine();
        }
        interests.close();
    }

    // true = ok, false = exception
    public boolean crawlProfile(String influences) throws TwitterException, IOException {
    	
    	// Print basic profile details
    	User user = twitter.showUser(getCurrentUserHandler());
    	
    	// Load up the various fields that can be extracted from the twitter api directly
    	response.name = user.getName();
    	response.id = user.getId();
    	response.screenname = user.getScreenName();
    	response.description = user.getDescription().replaceAll("\\p{Cntrl}", " ");
    	response.location = user.getLocation();
    	response.language = user.getLang();
    	response.nFollowers = user.getFollowersCount();
    	response.nFollowings = user.getFriendsCount(); 
    	response.nTweets = user.getStatusesCount();

    	// get all his friends and store them into sampledfriends arraylist
    	getFriends(user.getId(), user.getFriendsCount());
    	System.out.println("Sampled "+sampledfriends.size()+" friends");
  
    	// get all his followers
//    	ArrayList<Long> followers = getFollowers(user.getId());
    	
    	// compute the overlap of interests with friends
    	HashMap<String,ArrayList<Long>> interest_users_ = new HashMap<String,ArrayList<Long>>();
    	int totmyinterest =0;
    	for(String c: interest_users.keySet()){
    		for(long us: interest_users.get(c)){
        		if(sampledfriends.contains(us)){
//        			System.out.println(us+"\t"+interest_users.get(c).toString());
        			if(!interest_users_.containsKey(c)) {
        				ArrayList<Long> temp = new ArrayList<Long>();
        				temp.add(us);
        				interest_users_.put(c,temp);
        				totmyinterest++;
        			} else {
        				if(!interest_users_.get(c).contains(us)){
        					interest_users_.get(c).add(us);
            				totmyinterest++;
        				}
        			}
        		}
    		}
    	}
    	
    	response.interests_percentage = new HashMap<String,Double>();
    	// Store interests of the user
    	for(String c: interest_users_.keySet()){
    		int myinterest = interest_users_.get(c).size();
    		response.interests_percentage.put(c,(myinterest*100.0/totmyinterest));
    	}
    	
    	// Store influence score
    	double influence = (user.getFollowersCount()+1.0)/(user.getFriendsCount()+1.0);
//    	writer.printf("InfluenceScore (log10((Fo+1)/(Fr+1))):\t%.3f", Math.log10(influence));
    	response.network_influence_score = Math.log10(influence);

    	// Compute the retweets influence score
    	
    	// Get the first 200 tweets to check retweeting counts.
    	Paging paging = new Paging(1, 200);
    	List<Status> statuses = twitter.getUserTimeline(user.getId(),paging);
    	System.out.println("Statuses: "+statuses.size());
    	
        Random randomGenerator=new Random();
        ArrayList<Integer> tweetseen = new ArrayList<Integer>();
    	
    	double avg=0;
    	
        int idx=1;
        int trials=0;
        while (idx <= statuses.size()){
            int randomInt = randomGenerator.nextInt(statuses.size());
            trials++;
            if(!tweetseen.contains(randomInt)){
            	tweetseen.add(randomInt);
                Status status = statuses.get(randomInt);
                
                // Exclude tweets that are retweets from others
                // (to reduce chances of including retweet numbers not coming
                // from this user)
                if(!status.getText().split(" ")[0].equals("RT")){
//                	System.out.println("Status: "+status);

                    int folwrs = user.getFollowersCount();
                    int retwts = status.getRetweetCount();
                    
                    // Divide total retweets of this tweet with possible retweets
                    // which are the followers of this user.
                    avg+=retwts*1.0/folwrs;
                    
                    idx++;
                }
            }
            // Try three times the total size of the statuses extracted.
            if(trials>3*statuses.size())
            	break;
        }

        avg=avg/idx;
//        System.out.println(idx+" "+trials+" "+avg);
        response.retweet_influence_score = avg;
        
    	// Add the particular user in the influence score table.
    	// (if it was already in the file, it will be reinserted with an updated value.)
        user_influence.put(user.getScreenName(), Math.log10(influence)+"_"+avg);
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(influences)));
        for(String s: user_influence.keySet()){
        	out.println(s+"\t"+user_influence.get(s).split("_")[0]+"\t"+user_influence.get(s).split("_")[1]);
        }
        out.close();

    	response.mention_influence_score = 0;

        return true;
    }

    // Get a sample of friends of user with id
    // (as much as the accuracy allows)
    public int getFriends(long id, int numfriends) throws TwitterException{

    	// Numpages will be either the total number of friends a user has
    	// or an upper bound based on the max number of friends retrieved per page * number of max pages
    	int numpages = (Math.round(numfriends/maxfriendsreturned) > accuracy*maxfriendsrequests) 
    			? accuracy*maxfriendsrequests
    			: Math.round(numfriends/maxfriendsreturned);
    	
    	System.out.println("There are "+numpages+" of pages of friends to crawl");
    	
        long page = -1;
        IDs friends = null;
        while (currentpage <= numpages){
            friends = twitter.getFriendsIDs(id, page);
            
            for (long friend : friends.getIDs()) {
            	sampledfriends.add(friend);
            }
            System.out.println("Covered friends "+currentpage*maxfriendsreturned);
    		page=friends.getNextCursor();
    		currentpage++;
        	if( currentpage%maxfriendsrequests == 1 && currentpage <= numpages) {
                System.out.println("Rate limits reached. Switching crawler!");
    	    	this.twitter = UtilsMultiKeys.getNextTwitterInstance();
        	}
    	}
    	return sampledfriends.size();
    }
    
    public String getCurrentUserHandler(){
    	return currentUserHandler;
    }

    // If the API rates are passed, switch twitter instantiation by changing API key.
    // If all keys are used to their max, go to sleep.
    private void handleTwitterException(TwitterException e) {
        if (e.exceededRateLimitation()) {
            logException(e);
            System.out.println("Rate limits reached. Switching crawler!");
	    	this.twitter = UtilsMultiKeys.getNextTwitterInstance();
            if (consecutiveErrorsRate++ > numkeys) {
                int secondsToSleep = e.getRateLimitStatus().getSecondsUntilReset() + 1; // 1s slack
                int millisToSleep = 1000 * secondsToSleep;
                System.out.println("[" + new Date() + "] Sleeping for " + secondsToSleep + " seconds");
                long before = System.currentTimeMillis();
                try {
                    Thread.sleep(millisToSleep);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
                long now = System.currentTimeMillis();
                System.out.println("[" + new Date() + "] Woke up! Slept for " + (now - before) / 1000 + " seconds");
            }
	    } else {
            logException(e);
            if (consecutiveErrors++ > MAX_RETRIES)
                setRetry(false); // already tried enough
        }
    }

            
    public boolean hasMorePages() {
        return (currentFriends != null && currentFriends.hasNext());
    }

    private void logException(TwitterException e) {
        // print exception
        System.err.println(e.getMessage());
        // record it for statistics
        int code = e.getStatusCode();
        if (!errorMap.containsKey(code))
            errorMap.put(code, 0);
        errorMap.put(code, errorMap.get(code) + 1);
    }

    private boolean retry() {
        return retry;
    }

    private void setRetry(boolean retry) {
        this.retry = retry;
    }

    private void resetRetry() {
        consecutiveErrors = 0; // reset retry counter
        retry = true;
    }

}
