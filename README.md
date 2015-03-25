Copyright (c) 2015 Nicolas Kourtellis

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

Additional Disclaimer:

This code was tested on Linux and Mac-based systems and works appropriately. As mentioned above, please use at your own risk. We cannot provide any sort of guarantees that it will work on your platform and specific settings. Also, we cannot provide any support for you to make it work in case of compilation problems.

If you use this software and its relevant feeatures, please make sure to acknowledge the EU project MULTISENSOR, grant num: 610411.

# TwitterCrawler
This repository hosts a set of classes for allowing a user with legitimate Twitter application and user authentication keys to crawl the profile of particular users and compute basic statistics on network and retweeting influence.

To compile and run:

mvn package war:war
mvn jetty:run

Testing Services:

Use the "Testing.java" to see examples on how to call the two services and receive the results.

To test the TwitterContributorAnalysisServiceMultiKeys:

This service is called with the "@barackobama" as the screenname input, and various details on this account are extracted. This is an application call and, thus, the keys used can be set to multiple ones to improve somewhat the accuracy of the crawling of interests. The accuracy is by default set to ?, i.e., 60% of the keys available will be used to reach good accuracy of interests (based on friends crawled). The output (using 3 out of 5 API keys):

I found 5 keys to initialize the crawler!
Changing twitter crawler: 0
log4j:WARN No appenders could be found for logger (twitter4j.HttpClientImpl).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
Crawling friends of user @barackobama
There are 45 of pages of friends to crawl
Covered friends 5000
Covered friends 10000
Covered friends 15000
Covered friends 20000
Covered friends 25000
Covered friends 30000
Covered friends 35000
Covered friends 40000
Covered friends 45000
Covered friends 50000
Covered friends 55000
Covered friends 60000
Covered friends 65000
Covered friends 70000
Covered friends 75000
Rate limits reached. Switching crawler!
Changing twitter crawler: 1
Covered friends 80000
Covered friends 85000
Covered friends 90000
Covered friends 95000
Covered friends 100000
Covered friends 105000
Covered friends 110000
Covered friends 115000
Covered friends 120000
Covered friends 125000
Covered friends 130000
Covered friends 135000
Covered friends 140000
Covered friends 145000
Covered friends 150000
Rate limits reached. Switching crawler!
Changing twitter crawler: 2
Covered friends 155000
Covered friends 160000
Covered friends 165000
Covered friends 170000
Covered friends 175000
Covered friends 180000
Covered friends 185000
Covered friends 190000
Covered friends 195000
Covered friends 200000
Covered friends 205000
Covered friends 210000
Covered friends 215000
Covered friends 220000
Covered friends 225000
Sampled 225000 friends
Statuses: 200
NAME: Barack Obama
ID: 813286
LANG: en
DESCRIPTION: This account is run by Organizing for Action staff. Tweets from the President are signed -bo.
SCREENNAME: BarackObama
LOCATION: Washington, DC
FOLLOWERS: 56892341
FRIENDS: 643991
TWEETS: 13265
INTERESTS: {news=10.0
gaming=10.0
music=20.0
government=40.0
food-drink=10.0
family=10.0}
RET. INFLUENCE: 2.5248203651256065E-5
NET. INFLUENCE: 1.9461733397084464
MEN. INFLUENCE: 0.0

To test the TwitterSearchNamesServiceMultiKeys:

This service is called with the "Barack Obama" as the input string and the output returns the top 10 relevant Twitter accounts with this string. This is a user-authenticated call, and thus one key is enough. The output:

Crawling Twitter to discover users relevant to this query: barack obama
log4j:WARN No appenders could be found for logger (twitter4j.HttpClientImpl).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
The query returned from that 10 results. Printing the first 10 most relevant: 
i	Name	Username	ID
1	Barack Obama	BarackObama	813286
2	Barack Obama News	ObamaNews	14099695
3	Gay Barack Obama	GayObama	257094713
4	Plaid Barack Obama	Plaid_Obama	102116993
5	Barack Obama	barackobama44	27564275
6	Barack Obama	ThePresObama	414359892
7	Barack Obama	BarakObama__	181743520
8	Barack Obama	theUSpresident	156529187
9	Team Barack Obama	TeamBarackObama	141405856
10	Zombie Barack Obama	zombama	25400207

