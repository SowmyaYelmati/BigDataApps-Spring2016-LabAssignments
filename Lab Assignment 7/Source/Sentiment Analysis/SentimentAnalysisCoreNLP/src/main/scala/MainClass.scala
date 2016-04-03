import org.apache.spark.SparkConf

import org.apache.spark.streaming.twitter.TwitterUtils

import org.apache.spark.streaming.{Seconds, StreamingContext}



object MainClass {


  def main(args: Array[String]) {


    val filters = args


    System.setProperty("twitter4j.oauth.consumerKey", "UamFKOWeTXeekMhrPMx1nZQl7")

    System.setProperty("twitter4j.oauth.consumerSecret", "FH8J6Oo5xUb5w15eV0h7IWlxQeodHrNEJs8IZBIydUQ3iyjXne")

    System.setProperty("twitter4j.oauth.accessToken", "880485062-qJV1iexoBBwJhqXWazoAjoKA5Qbgm6HSyrjSbrPy")

    System.setProperty("twitter4j.oauth.accessTokenSecret", "Kq06Fbdo7Pa4aThVVt3AvfOYJyZLr7eBhxRrDiShjfJli")

    val sparkConf = new SparkConf().setAppName("STweetsApp").setMaster("local[*]")

    //Create a Streaming COntext with 2 second window

    val ssc = new StreamingContext(sparkConf, Seconds(5))

    val stream = TwitterUtils.createStream(ssc, None, filters)

    val tweetsinenglang=stream.filter(_.getLang()=="en")

    val tweets_text =tweetsinenglang.map(status => status.getText())


    tweets_text.foreachRDD(rdd => rdd.collect().foreach(text =>

    {

      println(text)

      val sentimentAnalyzer: SentimentAnalyzer = new SentimentAnalyzer

      val tweetWithSentiment: TweetWithSentiment = sentimentAnalyzer.findSentiment(text)

      System.out.println(tweetWithSentiment)

    }




    ))

    ssc.start()


    ssc.awaitTermination()



  }

}