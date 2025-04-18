public class FeedManager {

    private Users users; // users will be stored in hash table
    private Posts posts; // posts will be stored in hash table

    public FeedManager() {
        // initialize the hash tables with buckets of size 1000 each containing AVLTree with null root
        this.users = new Users(); 
        this.posts = new Posts(); 
    }

    public StringBuilder create_user(String userId) {
        StringBuilder result = new StringBuilder(); // initialize result with empty string
        if (users.getUser(userId) != null) { 
            // if user already exists
            result = new StringBuilder("Some error occurred in create_user.");
        } else {
            // if user does not exist create a new user and add it to the hash table
            User newUser = new User(userId);
            users.addUser(newUser);
            result = new StringBuilder("Created user with Id " + userId + ".");
        }
        return result;
    }

    public StringBuilder follow_user(String userId, String otherUserId) {
        StringBuilder result = new StringBuilder();
        User currentUser = users.getUser(userId); // get the current user
        User followedUser = users.getUser(otherUserId); // get the user to be followed
        if (userId.equals(otherUserId)) { 
            // since user cannot follow itself
            result = new StringBuilder("Some error occurred in follow_user.");
        } else if (currentUser == null || followedUser == null) {
            // if either of the users do not exist
            result = new StringBuilder("Some error occurred in follow_user.");
        } else if (currentUser.getFollowing().find(otherUserId) != null) {
            // if the current user is already following the other user
            result = new StringBuilder("Some error occurred in follow_user.");
        } else {
            // if the current user is not following the other user then follow the other user
            currentUser.followUser(otherUserId);
            result = new StringBuilder(currentUser.getUserId() + " followed " + followedUser.getUserId() + ".");
        }
       return result;
    }

    public StringBuilder unfollow_user(String userId, String otherUserId) {
        StringBuilder result = new StringBuilder();
        User currentUser = users.getUser(userId);
        User unFollowedUser = users.getUser(otherUserId);
        if (userId.equals(otherUserId)) {
            result = new StringBuilder("Some error occurred in unfollow_user.");
        } else if (currentUser == null || unFollowedUser == null) {
            result = new StringBuilder("Some error occurred in unfollow_user.");
        } else if (currentUser.getFollowing().find(otherUserId) == null) {
            // if the current user is not following the other user
            result = new StringBuilder("Some error occurred in unfollow_user.");
        } else {
            // if the current user is following the other user then unfollow the other user
            currentUser.unfollowUser(otherUserId);
            result = new StringBuilder(currentUser.getUserId() + " unfollowed " + unFollowedUser.getUserId() + ".");
        }
       return result;
    }

    public StringBuilder create_post(String userId, String postId, Object content) {
        StringBuilder result = new StringBuilder();
        User currentUser = users.getUser(userId);
        if (currentUser == null) {
            result = new StringBuilder("Some error occurred in create_post.");
        } else if (posts.getPost(postId) != null) { 
            // if there is already a post with the same postId
            result = new StringBuilder("Some error occurred in create_post.");
        } else {  
            // if the post does not exist then
            Post newPost = new Post(postId, userId, content); // create a new post
            currentUser.addPost(postId); // add the postId to the hash table of users
            posts.addPost(newPost); // add the userId to the hash table of posts
            result = new StringBuilder(currentUser.getUserId() + " created a post with Id " + postId + ".");
        }
        return result;
    }

    public StringBuilder see_post(String userId, String postId) {
        StringBuilder result = new StringBuilder();
        User currentUser = users.getUser(userId);
        Post currentPost = posts.getPost(postId);
        if (currentUser == null || currentPost == null) {
            result = new StringBuilder("Some error occurred in see_post.");
        } else {
            // if the current user and post exist then add the postId to the seenPosts hash table of the user
            // if the postId already exists in seenPosts it will not change anything and either way the post will be seen
            currentUser.getSeenPosts().insert(postId, null);
            result = new StringBuilder(currentUser.getUserId() + " saw " + postId + ".");
        }
        return result;
    }

    public StringBuilder see_all_posts_from_user(String viewerId, String viewedId) {
        StringBuilder result = new StringBuilder();
        User viewer = users.getUser(viewerId); // get the user who is viewing the posts
        User viewed = users.getUser(viewedId); // get the user whose posts are being viewed
        if (viewer == null || viewed == null) { 
            result = new StringBuilder("Some error occurred in see_all_posts_from_user.");
        } else {
            AVLTree<String, Void> seenPosts = viewer.getSeenPosts(); // get the AVLTree of seen posts of the viewer
            AVLTree<String, Void> viewedPosts = viewed.getPosts(); // get the AVLTree of posts of the viewed user
            Queue viewedPostsList = viewedPosts.traverse(); // get the list of posts of the viewed user by traversing the AVLTree
            while (!viewedPostsList.isEmpty()) { // while there are posts in the list
                // dequeue the post and add it to the seenPosts AVLTree of the viewer
                String postId = viewedPostsList.dequeue().toString();
                seenPosts.insert(postId, null);
            }
            // even if there are no posts to be seen the loop will run and the AVLTree will be empty
            result = new StringBuilder(viewerId + " saw all posts of " + viewedId + ".");            
        }
        return result;
    }

    public StringBuilder toggle_like(String userId, String postId) {
        StringBuilder result = new StringBuilder();
        User currentUser = users.getUser(userId);
        Post currentPost = posts.getPost(postId);
        if (currentUser == null || currentPost == null) {
            result = new StringBuilder("Some error occurred in toggle_like.");
        } else {
            if (currentUser.getLikedPosts().find(postId) != null) { 
                // if the current user has already liked the post then unlike the post
                currentUser.getLikedPosts().delete(postId); // delete the postId from the likedPosts AVLTree of the user
                currentPost.removeLike(userId); // remove the userId from the likes list of the post
                result = new StringBuilder(currentUser.getUserId() + " unliked " + postId + ".");
            } else {
                // if the current user has not liked the post then like the post
                currentUser.getLikedPosts().insert(postId, null); // insert the postId into the likedPosts AVLTree of the user
                currentUser.getSeenPosts().insert(postId, null); // insert the postId into the seenPosts AVLTree of the user
                currentPost.addLike(userId); // add the userId to the likes list of the post
                result = new StringBuilder(currentUser.getUserId() + " liked " + postId + ".");
            }
        }
        return result;
    }

    public StringBuilder generate_feed(String userId, String num) {
        StringBuilder result = new StringBuilder();
        int numPosts = Integer.parseInt(num); // convert the string to integer
        User currentUser = users.getUser(userId); // get the current user
        if (currentUser == null) {
            return result.append("Some error occurred in generate_feed.");
        }

        // initial message setup
        result.append(String.format("Feed for %s:", userId));
        
        AVLTree<String, Void> seenPosts = currentUser.getSeenPosts(); // get the AVLTree of seen posts of the user
        AVLTree<String, Void> following = currentUser.getFollowing(); // get the AVLTree of following users of the user
        Queue<String> followingList = following.traverse(); // get the list of following users by traversing the AVLTree
        Queue<Post> allPosts = new Queue<>(); // create a queue to store all posts from all following users
    
        while (!followingList.isEmpty()) { // while there are following users
            String followingId = followingList.dequeue(); // dequeue the following user
            User followingUser = users.getUser(followingId); // get the following user
            if (followingUser == null) continue; // ensure the following user is valid
            Queue<String> followingUserPostsList = followingUser.getPosts().traverse(); // get the list of posts of the following user by traversing the AVLTree
    
            while (!followingUserPostsList.isEmpty()) { // while there are posts in the list
                String postId = followingUserPostsList.dequeue(); // dequeue the post
                if (seenPosts.find(postId) == null) { // if the post has not been seen by the user
                    allPosts.enqueue(posts.getPost(postId)); // enqueue the post
                }
            }
        }
    
        MaxHeap<Post> maxHeap = new MaxHeap<Post>(allPosts.size()); // create a max heap with the size of the queue (all not seen posts)
        while (!allPosts.isEmpty()) { // while there are posts in the queue
            Post post = allPosts.dequeue(); // dequeue the post
            maxHeap.insert(post); // insert the post into the max heap (will maintain the max heap property)
        } 
    
        int count = 0; // initialize the count of posts seen to 0
        while (!maxHeap.isEmpty() && count < numPosts) { // while there are posts in the max heap and the count is less than the number of posts
            Post post = maxHeap.extractMax(); // extract the post with the maximum likes
            result.append(String.format("\nPost ID: %s, Author: %s, Likes: %d",
                                        post.getPostId(), post.getUserId(), post.getLikeCount()));
            count++;
            // those posts will not be added into the seenPosts AVLTree of the user
            // seenPosts.insert(post.getPostId(), null);
        }
        
        // if the count is less than the number of available posts then print no more posts available
        if (count < numPosts) {
            result.append(String.format("\nNo more posts available for %s.", userId));
        }
    
        return result;
    }
    
    public StringBuilder scroll_through_feed(String userId, int numPosts, int[] likes) {
        StringBuilder result = new StringBuilder();
        User currentUser = users.getUser(userId);
        if (currentUser == null) {
            return result.append("Some error occurred in scroll_through_feed.");
        }

        result.append(userId + " is scrolling through feed:");
        
        AVLTree<String, Void> seenPosts = currentUser.getSeenPosts();
        AVLTree<String, Void> following = currentUser.getFollowing();
        Queue<String> followingList = following.traverse();
        Queue<Post> allPosts = new Queue<>();
    
        while (!followingList.isEmpty()) {
            String followingId = followingList.dequeue();
            User followingUser = users.getUser(followingId);
            if (followingUser == null) continue; // Ensure the following user is valid
            Queue<String> followingUserPostsList = followingUser.getPosts().traverse();
    
            while (!followingUserPostsList.isEmpty()) {
                String postId = followingUserPostsList.dequeue();
                if (seenPosts.find(postId) == null) {
                    allPosts.enqueue(posts.getPost(postId));
                }
            }
        }
    
        MaxHeap<Post> maxHeap = new MaxHeap<Post>(allPosts.size());
        while (!allPosts.isEmpty()) {
            Post post = allPosts.dequeue();
            maxHeap.insert(post);
        } 

        // the code above is same as scroll_through_feed() till here
        int count = 0; 
        while (!maxHeap.isEmpty() && count < numPosts) { // while there are posts in the max heap and the count is less than the number of posts
            Post post = maxHeap.extractMax(); // extract the post with the maximum likes
            int currentLike = likes[count]; // get the current like value

            if (currentLike == 1) { // if the current like value is 1
                post.addLike(userId); // add the userId to the likes list of the post
                seenPosts.insert(post.getPostId(), null); // insert the postId into the seenPosts AVLTree of the user
                currentUser.getLikedPosts().insert(post.getPostId(), null); // insert the postId into the likedPosts AVLTree of the user
                result.append(String.format("\n%s saw %s while scrolling and clicked the like button.", userId, post.getPostId()));
            } else {
                // if the current like value is 0
                seenPosts.insert(post.getPostId(), null); // insert the postId into the seenPosts AVLTree of the user
                result.append(String.format("\n%s saw %s while scrolling.", userId, post.getPostId()));
            }
            count++;
        }
        // if the count is less than the number of available posts then print no more posts in feed
        if (count < numPosts) {
            result.append(String.format("\nNo more posts in feed.", userId));
        }
        return result;
    }
    
    public StringBuilder sort_posts(String userId) {
        StringBuilder result = new StringBuilder();
        User currentUser = users.getUser(userId);
        if (currentUser == null) {
            return result.append("Some error occurred in sort_posts.");
        }

        AVLTree<String, Void> currentUserPosts = currentUser.getPosts(); // get the AVLTree of posts of the user
        Queue<String> currentUserPostsList = currentUserPosts.traverse(); // get the list of posts of the user by traversing the AVLTree
        MaxHeap<Post> maxHeap = new MaxHeap<Post>(currentUserPostsList.size()); // create a max heap with the size of the list of posts

        if (currentUserPostsList.isEmpty()) { // if there are no posts from the user
            return result.append("No posts from " + userId + " .");
        } else { // if there are posts from the user intialize the message
            result.append("Sorting " + userId + "'s posts:");
        }

        while (!currentUserPostsList.isEmpty()) { // while there are posts in the list
            String postId = currentUserPostsList.dequeue(); // dequeue the post
            Post currentPost = posts.getPost(postId); // get the post from the hash table of posts
            maxHeap.insert(currentPost); // insert the post into the max heap
        }

        while (!maxHeap.isEmpty()) { // while there are posts in the max heap
            Post post = maxHeap.extractMax(); // extract the post with the maximum likes or the lexicographical order
            result.append(String.format("\n%s, Likes: %d", post.getPostId(), post.getLikeCount()));
        }

        return result;
    }
}