package jvm.jirc.client.entity.profile.event;

import jvm.jirc.client.entity.profile.Profile;

public interface MyProfileListener {

    public void onFriendAdd(final Profile profile);

    public void onFriendRemove(final Profile profile);
}
