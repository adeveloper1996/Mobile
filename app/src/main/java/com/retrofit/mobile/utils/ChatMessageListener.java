package com.retrofit.mobile.utils;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;

public interface ChatMessageListener {

    void processMessage(Chat chat, Message message);
}