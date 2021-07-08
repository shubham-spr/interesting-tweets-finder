package com.listener.interestingtweetsfinder;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Pattern;

public class PatternMatchingTest {

    private final List<String> patterns = Lists.newArrayList (
            "RT @TheSpearsRoom: Miley Cyrus showing Britney some love during her Party In The USA Performance! " +
                    "\uD83D\uDE2D❤️ #FreeBritney #July4th  https://t.co/8…",
            "@Thato_Pru @BeefHasSpoken Thato I didn't receive a call from u, o founetse mang? \uD83E\uDD14",
            "RT @tkxmq_: ใครไม่อยากติดเนตรายเดือนแพงๆ หรือติดสัญญาไรงี้แนะนำให้ซื้อ router กับซิมรายปีของค่ายทรศ.",
            "RT @sprinklr: ใครไม่อยากติดเนตรายเดือนแพงๆ หรือติดสัญญาไรงี้แนะนำให้ซื้อ router กับซิมรายปีของค่ายทรศ.",
            "RT #sprinklrlife: ใครไม่อยากติดเนตรายเดือนแพงๆ หรือติดสัญญาไรงี้แนะนำให้ซื้อ router " +
                    "กับซิมรายปีของค่ายทรศ.",
            "煮込み料理であくとりをする度に、愛の貧乏脱出大作戦（わかる？）で\\n「あくとりすぎて旨みまで捨てる」まずいラーメン屋を思い出すんよ\\n今日はカレー\uD83C\uDF5B",
            "RT @tospophoto: 【プロ野球】\\n７月８日　神宮球場\\n#東京ヤクルトスワローズ×阪神"
    );

    @Test
    public void patternMatchTestForRTStart(){
        Pattern pattern = Pattern.compile ("RT .*");
        assert ( pattern.matcher (patterns.get (0)).matches ()) ;
        assert ( !pattern.matcher (patterns.get (1)).matches ()) ;
        assert ( pattern.matcher (patterns.get (2)).matches ()) ;
    }

    @Test
    public void patternMatchTestForSprinklr(){
        Pattern pattern = Pattern.compile (".*(#sprinklr|@Sprinklr|#sprinklrlife).*",Pattern.CASE_INSENSITIVE);
        assert ( !pattern.matcher (patterns.get (0)).matches ()) ;
        assert ( pattern.matcher (patterns.get (3)).matches ()) ;
        assert ( pattern.matcher (patterns.get (4)).matches ()) ;
    }

    @Test
    public void testChineseCharacters(){
        Pattern pattern = Pattern.compile (".*[\\p{IsHan}]+.*");
        assert ( !pattern.matcher (patterns.get (3)).matches ()) ;
        assert ( !pattern.matcher (patterns.get (4)).matches ()) ;
        assert ( pattern.matcher (patterns.get (5)).matches ()) ;
        assert ( pattern.matcher (patterns.get (6)).matches ()) ;
    }
}
