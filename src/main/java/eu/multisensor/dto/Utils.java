package eu.multisensor.dto;

import twitter4j.Twitter;
import twitter4j.auth.OAuth2Token;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Utils {

	private static final String CONSUMER_KEY		= "OAMm1xRi0OmI6LcqitlHwq59d";
	private static final String CONSUMER_SECRET 	= "0vJ6fkfz5Z5bZvGi3t6gy7KLySOeqVYlAkVVh2Fel5M7tXpL6h";

	// Method to return Twitter instantiation based on fixed Twitter API Keys
    public static Twitter getTwitterInstance() {
    	ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setApplicationOnlyAuthEnabled(true);

    	Twitter twitter = new TwitterFactory(builder.build()).getInstance();

        // exercise & verify
        twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
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

        return twitter;
    }
}
