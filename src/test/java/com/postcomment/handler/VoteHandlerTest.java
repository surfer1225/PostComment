package com.postcomment.handler;

import com.postcomment.contract.internal.request.VoteRequest;
import com.postcomment.mockdb.DataCenter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class VoteHandlerTest {
    @InjectMocks
    private VoteHandler handler;

    @Mock
    private DataCenter dc;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    //tc1: dc successfully update vote number
    //tc2: dc fails to update vote number
    @Test
    public void changeVoteTest() {
        UUID uuid = UUID.randomUUID();
        VoteRequest r = new VoteRequest();
        r.setUuid(uuid);
        r.setUpvote(true);
        when(dc.changeVote(uuid, 1)).thenReturn(1);
        assertTrue(handler.execute(r).isSuccess());

        r.setUpvote(false);
        when(dc.changeVote(uuid, -1)).thenReturn(2);
        assertTrue(!handler.execute(r).isSuccess());
    }
}
