package com.ivanov_sergey.todoapp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static java.util.Objects.isNull;

@Getter
@AllArgsConstructor
public enum FriendRequestStatus {
    REQUESTED("requested"),
    APPROVED("approved"),
    UNFRIENDED ("unfriended"),
    DECLINED("declined");
    private final String value;

    public static FriendRequestStatus getFriendRequestStatusByValue(String value){
        if(isNull(value) || value.isEmpty()){
            return null;
        }

        FriendRequestStatus[] friendRequestStatuses = FriendRequestStatus.values();
        for (FriendRequestStatus taskStatus : friendRequestStatuses) {
            if (taskStatus.value.equals(value)){
                return taskStatus;
            }
        }
        return null;
    }

    public static String getValueByFriendRequestStatus(FriendRequestStatus friendRequestStatus){
        return friendRequestStatus.getValue();
    }

    public static List<FriendRequestStatus> getAll(){
        return List.of(values());
    }
}
