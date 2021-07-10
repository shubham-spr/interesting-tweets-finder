package com.listener.interestingtweetsfinder;

import com.listener.interestingtweetsfinder.utils.KeyHelper;

/**
 * RedisSchema provides key building support according to the schema used.
 * <br>
 * Redis data-structures used -
 * <br>
 * > a set "app:interesting" {tweet_id1,tweet_id2,tweet_id3,tweet_id4}
 * <br>
 *  > a stream "app:interesting:regex-id" [{"id":"tweet_id","text":"tweet","conversationId":"conversation_id"}...]
 *  for each regex-id
 */
public class RedisSchema {

    public static String getInterestingHashKey(){
        return KeyHelper.getKey ("interesting");
    }

    public static String getInterestingHashKey(String reason){
        return KeyHelper.getKey ("interesting:"+reason);
    }

}
