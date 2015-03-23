package eu.multisensor.dto;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterSearchNamesForMultisensor {

    public static int maxusersreturned = 1000;
    public static int maxsearchrequests = 180;
    private static final int MAX_RETRIES = 5;
    
    // state variables
    private Twitter twitter = null;
    private Map<Integer, Integer> errorMap = new HashMap<Integer, Integer>();
    private int consecutiveErrors;
    private boolean retry;

    public SearchNameResponse response = new SearchNameResponse();
    
    public TwitterSearchNamesForMultisensor(String query, String keys) throws IOException {
    	this.twitter = UtilsUserAuth.getTwitterInstance(keys);
        
    	// Search if query is not null or empty
        if(!query.equals(null) || !query.equals("")) {
            resetRetry();

            System.out.println("Crawling Twitter to discover users relevant to this query: "+query);
			boolean done = false;
			while (!done && this.retry()) {
				try {
					// Search Twitter for users matching query
					response.users = twitter.searchUsers(query, -1);
					done = true;
				} catch (TwitterException e) {
					this.handleTwitterException(e);
				}
				if(done)
					this.retry=false;
			}
			if (!done) {
				System.out.println("Could not discover users on Twitter for this query ");
			}
        }
    }

    private void handleTwitterException(TwitterException e) {
        if (e.exceededRateLimitation()) {
            logException(e);
            System.out.println("Rate limits reached. crawler!");
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
	    } else {
            logException(e);
            if (consecutiveErrors++ > MAX_RETRIES)
                setRetry(false); // already tried enough
        }
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
