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
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class UtilsUserAuth {

	private static String CONSUMER_KEY	= "";
	private static String CONSUMER_SECRET_KEY = "";
	private static String ACCESS_TOKEN = "";
	private static String ACCESS_TOKEN_SECRET = "";

    public static Twitter getTwitterInstance(String file) throws IOException {

    	// Configuration setup to authenticate user account for SearchNameService
		BufferedReader readkeys = new BufferedReader(new FileReader(file));
		String line = readkeys.readLine();
		while(line!=null){
			String[] input = line.split("\t");
			if(input[0].equals("ConsumerKey(APIKey)")) {
				CONSUMER_KEY=input[1];
			}
			if(input[0].equals("ConsumerSecret(APISecret)")) {
				CONSUMER_SECRET_KEY=input[1];
			}
			if(input[0].equals("AccessToken")) {
				ACCESS_TOKEN=input[1];
			}
			if(input[0].equals("AccessTokenSecret")) {
				ACCESS_TOKEN_SECRET=input[1];
			}
			if(!CONSUMER_KEY.equals("") && !CONSUMER_SECRET_KEY.equals("") && !ACCESS_TOKEN.equals("") && !ACCESS_TOKEN_SECRET.equals(""))
				break;
			line = readkeys.readLine();
		}
		readkeys.close();
    	
    	ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_SECRET_KEY)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
    	
        return twitter;
    }
}
