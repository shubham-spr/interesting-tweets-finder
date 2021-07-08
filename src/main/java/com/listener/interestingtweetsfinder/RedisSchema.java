package com.listener.interestingtweetsfinder;

import com.listener.interestingtweetsfinder.utils.KeyHelper;

public class RedisSchema {

    // as a set, interesting [twid, twid, twid...]
    public static String getInterestingHashKey(){
        return KeyHelper.getKey ("interesting");
    }

    // as a stream, interesting:reason [twid1|twid2|twid3..] (a stream of tweet ids)
    public static String getInterestingHashKey(String reason){
        return KeyHelper.getKey ("interesting:"+reason);
    }

}
