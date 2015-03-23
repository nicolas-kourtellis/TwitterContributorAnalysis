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
