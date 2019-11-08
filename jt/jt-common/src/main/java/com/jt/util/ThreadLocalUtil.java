package com.jt.util;

import com.jt.pojo.User;

public class ThreadLocalUtil {
    private static final ThreadLocal<User> thread = new ThreadLocal<User>();

    public static void set(User user) {
        thread.set(user);
    }

    public static User get() {
        return thread.get();
    }

    public static void remove() {
        thread.remove();
    }
}
