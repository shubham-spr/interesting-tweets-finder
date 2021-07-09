package com.listener.interestingtweetsfinder;

import com.listener.interestingtweetsfinder.controller.TweetFeedController;
import com.listener.interestingtweetsfinder.model.Regex;
import com.listener.interestingtweetsfinder.repository.RedisFeedRepository;
import com.listener.interestingtweetsfinder.repository.RedisFeedRepositoryImp;
import com.listener.interestingtweetsfinder.repository.RegexRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class TweetControllerIntegrationTest {

    private MockMvc mockMvc;

    @MockBean
    private RegexRepository regexRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        regexRepository.save (new Regex (
                "regex-chinese",
                ".*[\\p{IsHan}]+.*",
                "Contains Chinese/Japanese Text",
                false)
        );
    }

    @Test
    void testGetReasons() throws Exception {
        Thread.sleep (6000);
        mockMvc.perform (get ("/api/reasons"))
                .andExpect (status ().isOk ())
                .andExpect (model ().attributeExists ("reasons"))
                .andExpect (model ().attribute("reasons",contains ("regex-chinese")))
                .andExpect (view ().name ("index"));
    }


    @Test
    void testGetRecentInterestingTweetsWithReason() throws Exception {
        mockMvc.perform (get ("/api/recent/regex-chinese?limit=10"))
                .andExpect (status ().isOk ())
                .andExpect (model ().attributeExists ("tweetsForReason"))
                .andExpect (view ().name ("index"));
    }

    @Test
    void testGetForConversation() throws Exception {
        mockMvc.perform (get ("/api/conversation/123323232"))
                .andExpect (status ().isOk ())
                .andExpect (model ().attributeExists ("tweets"))
                .andExpect (view ().name ("index"));
    }

    @Test
    void testGetAllInterestingTweets() throws Exception {
        mockMvc.perform (get ("/api/recent"))
                .andExpect (status ().isOk ())
                .andExpect (model ().attributeExists ("allInterestingTweets"))
                .andExpect (view ().name ("index"));
    }
}
