package com.ivanov_sergey.todoapp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static java.util.Objects.isNull;

@Getter
@AllArgsConstructor
public enum NotificationStatus {
    READ,
    UNREAD
}
