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
