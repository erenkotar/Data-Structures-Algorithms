public class Users {
    private HashTable users; // hash table that contains all the posts of the system

    public Users() {
        this.users = new HashTable(10000); // starting with an arbitrary capacity
        // I chose it based on 6.3 Constraint Specifications of the project description
    }

    // add a new user
    public void addUser(User user) {
        // key is the userId, value is the user object 
        users.put(user.getUserId(), user);
    }

    // retrieve a user by their ID
    public User getUser(String userId) {
        return (User) users.get(userId);
    }

    // remove a user
    // will not be used in the project but implemented for completeness
    public void removeUser(String userId) {
        users.remove(userId);
    }
}
