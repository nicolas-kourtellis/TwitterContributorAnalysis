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
import java.io.FileReader;
import java.io.IOException;

import twitter4j.Twitter;
import twitter4j.auth.OAuth2Token;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class UtilsMultiKeys {

	private static final String[] CONSUMER_KEYS = new String[10];
	private static final String[] CONSUMER_SECRETS = new String[10];
	
	static int nextkey = 0;
	static int numkeys = 0;

	// Method for reading Twitter API Keys from file passed in as argument
	// Return number of keys successfully read in.
	public static int getKeys(String file) throws IOException{
		BufferedReader readkeys = new BufferedReader(new FileReader(file));
		String line = readkeys.readLine();
		numkeys = 0;
		while(line!=null){
			String[] input = line.split("\t");
			if(input[0].equals("ConsumerKey(APIKey)")) {
				CONSUMER_KEYS[numkeys]=input[1];
			}
			if(input[0].equals("ConsumerSecret(APISecret)")) {
				CONSUMER_SECRETS[numkeys]=input[1];
				numkeys++;
			}
			line = readkeys.readLine();
//			System.out.println("Keys: "+numkeys);
		}
		readkeys.close();
		
		System.out.println("I found "+(numkeys)+" keys to initialize the crawler!");
		
		if(numkeys>0) return (numkeys+1);
		else return numkeys;
	}
	
	// Method for switching in a cyclical fashion to the next available Twitter API Key
	// Return Twitter instantiation.
    public static Twitter getNextTwitterInstance() {
    	
    	if(numkeys>0){
			System.out.println("Changing twitter crawler: "+nextkey);
        	ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setApplicationOnlyAuthEnabled(true);
        	Twitter twitter = new TwitterFactory(builder.build()).getInstance();
            // exercise & verify
            twitter.setOAuthConsumer(CONSUMER_KEYS[nextkey], CONSUMER_SECRETS[nextkey]);
            OAuth2Token token = null;
    		try {
    			token = twitter.getOAuth2Token();
    		}
    		catch (Exception e) {
    			System.out.println("Could not get OAuth2 token");
    			e.printStackTrace();
    			System.exit(0);
    		}
    		
    	    twitter.setOAuth2Token(token);
    	    
    	    nextkey=(nextkey+1)%numkeys;
    	    
            return twitter;
    	} else
            return null;

    }
}
