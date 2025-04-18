public class Posts {
    private HashTable posts; // hash table that contains all the posts of the system

    public Posts() {
        this.posts = new HashTable(10000); // starting with an arbitrary capacity 
        // I chose it based on 6.3 Constraint Specifications of the project description
    }

    // add a new post
    public void addPost(Post post) {
        // key is the postId, value is the post object 
        posts.put(post.getPostId(), post);
    }

    // retrieve a post by their ID
    public Post getPost(String postId) {
        return (Post) posts.get(postId);
    }

    // remove a post 
    // will not be used in the project but implemented for completeness
    public void removePost(String postId) {
        posts.remove(postId);
    }
}

