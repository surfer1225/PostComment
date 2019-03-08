package com.postcomment.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment{
    private UUID uuid;
    @SerializedName("post-uuid")
    private UUID postUuid;
    private String author;
    private String content;
    private int votes;

}
