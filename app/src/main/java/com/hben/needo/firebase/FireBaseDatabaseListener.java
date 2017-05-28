package com.hben.needo.firebase;

import com.hben.needo.feed.model.FeedContent;

/**
 * Created by Ben on 12/02/2017.
 */

public interface FireBaseDatabaseListener {

    void postsFetched(FeedContent feedContent);
}
