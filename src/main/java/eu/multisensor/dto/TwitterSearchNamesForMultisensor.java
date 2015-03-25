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
