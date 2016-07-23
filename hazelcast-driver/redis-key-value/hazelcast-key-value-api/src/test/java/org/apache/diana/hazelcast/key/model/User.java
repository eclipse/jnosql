package org.apache.diana.hazelcast.key.model;

import java.io.Serializable;
import java.util.Objects;


public class User implements Serializable {

    private final String nickName;

    public User(String nickName) {
        this.nickName = nickName;

    }

    public String getNickName() {
        return nickName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(nickName, user.nickName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nickName);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("nickName='").append(nickName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
