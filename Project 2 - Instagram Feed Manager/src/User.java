public class User {
    private String userId; // unique user ID
    private AVLTree<String, Void> following; // tree of user IDs the user follows
    private AVLTree<String, Void> posts; // tree of post IDs created by the user, since key is enough valeu is null
    private AVLTree<String, Void> seenPosts; // tree of post IDs the user has seen
    private AVLTree<String, Void> likedPosts; // tree of post IDs the user has liked

    public User(String userId) {
        this.userId = userId;
        this.following = new AVLTree<>();
        this.posts = new AVLTree<>();
        this.seenPosts = new AVLTree<>();
        this.likedPosts = new AVLTree<>();
    }

    public String getUserId() {
        return userId;
    }

    public AVLTree<String, Void> getFollowing() {
        return following;
    }

    public AVLTree<String, Void> getPosts() {
        return posts;
    }

    public AVLTree<String, Void> getSeenPosts() {
        return seenPosts;
    }

    public AVLTree<String, Void> getLikedPosts() {
        return likedPosts;
    }

    // follow another user
    public void followUser(String otherUserId) {
        // if both user id is same, upper level will handle it
        // if user already follows the other user, nothing will change
        following.insert(otherUserId, null);
    }

    // unfollow a user
    public void unfollowUser(String otherUserId) {
        // if both user id is same, upper level will handle it
        // if user already not follows the other user, nothing will change
        following.delete(otherUserId);
    }

    // add a post created by the user (create post?)
    public void addPost(String postId) {
        // if post already exists, upper level will handle it
        posts.insert(postId, null);
    }

    // like a post
    public void likePost(String postId) {
        // if post is not already liked
        if (likedPosts.find(postId) == null) {
            // insert he postId to the likedPosts tree
            likedPosts.insert(postId, null);
        }
    }

    // unlike a post
    public void unlikePost(String postId) {
        // if post is already not in the likedPosts tree, delete will change nothing
        likedPosts.delete(postId);
    }

    public void markPostAsSeen(String postId) {
        // if post is already seen, nothing will change
        if (seenPosts.find(postId) == null) {
            // insert the postId to the seenPosts tree if not already seen
            seenPosts.insert(postId, null);
        }
    }
}
