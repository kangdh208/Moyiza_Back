package com.example.moyiza_be.club.dto;

import com.example.moyiza_be.club.entity.ClubJoinEntry;
import com.example.moyiza_be.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ClubMemberResponse {
    private final Long userId;
    private final String userNickname;
    private final String profilePictureUrl;
    private final LocalDateTime joinedSince;

    public ClubMemberResponse(User user, LocalDateTime joinedSince) {
        this.userId = user.getId();
        this.userNickname = user.getNickname();
        this.profilePictureUrl = user.getProfileImage();
        this.joinedSince = joinedSince;
    }
}