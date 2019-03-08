package com.postcomment.contract.internal.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteRequest extends BaseRequest {
    private UUID uuid;
    private boolean upvote;
}
