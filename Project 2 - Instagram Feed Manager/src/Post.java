public class Post implements Comparable<Post> {
    private String postId; // unique ID of the post
    private String userId; // id of the user who created the post
    private Object content; // content of the post which can be any object
    private LinkedList<String> likes; // list of user Ids who have liked this post

    public Post(String postId, String userId, Object content) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.likes = new LinkedList<>();
    }

    public String getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public Object getContent() {
        return content;
    }

    public LinkedList<String> getLikes() {
        return likes;
    }

    // return the number of likes
    public int getLikeCount() {
        // size of the list = number of likes
        return likes.size();
    }

    public void addLike(String userId) {
        // if the user has not liked the post already, add the user
        if (!likes.contains(userId)) {
            likes.addFirst(userId);
        }
    }

    public void removeLike(String userId) {
        // remove the user from the list of likes, if the userId not found return false so do nothing
        likes.remove(userId);
    }

    // define how one post compares to another
    @Override
    public int compareTo(Post other) {
        // first compare based on like count
        int likeComparison = Integer.compare(this.getLikeCount(), other.getLikeCount());
        if (likeComparison != 0) {
            return likeComparison;
        }

        // if like counts are the same, compare postIds lexicographically
        return this.postId.compareTo(other.getPostId());
    }
}