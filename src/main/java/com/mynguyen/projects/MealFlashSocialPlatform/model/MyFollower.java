package com.mynguyen.projects.MealFlashSocialPlatform.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class MyFollower {
    @EmbeddedId
    private MyFollowerId id;

    @ManyToOne
    @JoinColumn(name = "me_id", insertable = false, updatable = false)
    private User me;

    @ManyToOne
    @JoinColumn(name = "my_follower_id", insertable = false, updatable = false)
    private User myFollower;

    @Embeddable
    public static class MyFollowerId implements Serializable {
        @Column(name = "me_id", nullable = false, updatable = false)
        private Integer meId;

        @Column(name = "my_follower_id", nullable = false, updatable = false)
        private Integer myFollowerId;

        public MyFollowerId() {
        }

        public MyFollowerId(Integer meId, Integer myFollowerId) {
            this.meId = meId;
            this.myFollowerId = myFollowerId;
        }

        public Integer getMeId() {
            return meId;
        }

        public void setMeId(Integer meId) {
            this.meId = meId;
        }

        public Integer getMyFollowerId() {
            return myFollowerId;
        }

        public void setMyFollowerId(Integer myFollowerId) {
            this.myFollowerId = myFollowerId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MyFollowerId that = (MyFollowerId) o;
            return meId.equals(that.meId) && myFollowerId.equals(that.myFollowerId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(meId, myFollowerId);
        }
    }

    public MyFollower() {
    }

    public MyFollower(MyFollowerId id) {
        this.id = id;
    }

    public MyFollower(User me, User myFollower) {
        this.me = me;
        this.myFollower = myFollower;
    }

    public MyFollower(MyFollowerId id, User me, User myFollower) {
        this.id = id;
        this.me = me;
        this.myFollower = myFollower;
    }

    public User getMe() {
        return me;
    }

    public void setMe(User me) {
        this.me = me;
    }

    public User getMyFollower() {
        return myFollower;
    }

    public void setMyFollower(User myFollower) {
        this.myFollower = myFollower;
    }

    public String getFollowerUsername(){
        return this.getMyFollower().getUsername();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyFollower follower = (MyFollower) o;
        return id.equals(follower.id) && me.equals(follower.me) && myFollower.equals(follower.myFollower);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, me, myFollower);
    }

    @Override
    public String toString() {
        return "MyFollower{" +
                "me=" + me.getUsername() +
                ", myFollower=" + myFollower.getUsername() +
                '}';
    }
}
