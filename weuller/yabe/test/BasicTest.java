import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

//    @Test
//    public void aVeryImportantThingToTest() {
//        assertEquals(2, 1 + 1);
//    }
    
    @Before
    public void setup() { //metodo para deletar o banco antes de cada teste
        Fixtures.deleteDatabase();
    }
    
    @Test
    public void createAndRetrieveUser() {
        // Create a new user and save it
        new UserBO("bob@gmail.com", "secret", "Bob").save();
        
        // Retrieve the user with e-mail address bob@gmail.com
        UserBO bob = UserBO.find("byEmail", "bob@gmail.com").first();
        
        // Test 
        assertNotNull(bob);
        assertEquals("Bob", bob.getFullname());
    }
    
    @Test
    public void tryConnectAsUser() {
        // Create a new user and save it
        new UserBO("bob@gmail.com", "secret", "Bob").save();
        
        // Test 
        assertNotNull(UserBO.connect("bob@gmail.com", "secret"));
        assertNull(UserBO.connect("bob@gmail.com", "badpassword"));
        assertNull(UserBO.connect("tom@gmail.com", "secret"));
    }
    
    @Test
    public void createPost() {
        // Create a new user and save it
        UserBO bob = new UserBO("bob@gmail.com", "secret", "Bob").save();
        
        // Create a new post
        new PostBO(bob, "My first post", "Hello world").save();
        
        // Test that the post has been created
        assertEquals(1, PostBO.count());
        
        // Retrieve all posts created by Bob
        List<PostBO> bobPosts = PostBO.find("byAuthor", bob).fetch();
        
        // Tests
        assertEquals(1, bobPosts.size());
        PostBO firstPost = bobPosts.get(0);
        assertNotNull(firstPost);
        assertEquals(bob, firstPost.getAuthor());
        assertEquals("My first post", firstPost.getTitle());
        assertEquals("Hello world", firstPost.getContent());
        assertNotNull(firstPost.getPostedAt());
    }
    
    @Test
    public void postComments() {
        // Create a new user and save it
        UserBO bob = new UserBO("bob@gmail.com", "secret", "Bob").save(); // criando um usuário
     
        // Create a new post
        PostBO bobPost = new PostBO(bob, "My first post", "Hello world").save();
     
        // Post a first comment
        new CommentBO(bobPost, "Jeff", "Nice post").save();
        new CommentBO(bobPost, "Tom", "I knew that !").save();
     
        // Retrieve all comments
        List<CommentBO> bobPostComments = CommentBO.find("byPost", bobPost).fetch();
     
        // Tests
        assertEquals(2, bobPostComments.size());
     
        CommentBO firstComment = bobPostComments.get(0);
        assertNotNull(firstComment);
        assertEquals("Jeff", firstComment.getAuthor());
        assertEquals("Nice post", firstComment.getContent());
        assertNotNull(firstComment.getPostedAt());
     
        CommentBO secondComment = bobPostComments.get(1);
        assertNotNull(secondComment);
        assertEquals("Tom", secondComment.getAuthor());
        assertEquals("I knew that !", secondComment.getContent());
        assertNotNull(secondComment.getContent());
    }
    
    @Test
    public void useTheCommentsRelation() {
        // Create a new user and save it
        UserBO bob = new UserBO("bob@gmail.com", "secret", "Bob").save();
     
        // Create a new post
        PostBO bobPost = new PostBO(bob, "My first post", "Hello world").save();
     
        // Post a first comment
        bobPost.addComment("Jeff", "Nice post");
        bobPost.addComment("Tom", "I knew that !");
     
        // Count things
        assertEquals(1, UserBO.count());
        assertEquals(1, PostBO.count());
        assertEquals(2, CommentBO.count());
     
        // Retrieve Bob's post
        bobPost = PostBO.find("byAuthor", bob).first();
        assertNotNull(bobPost);
     
        // Navigate to comments
        assertEquals(2, bobPost.getComments().size());
        assertEquals("Jeff", bobPost.getComments().get(0).getAuthor());
        
        // Delete the post
        bobPost.delete();
        
        // Check that all comments have been deleted
        assertEquals(1, UserBO.count());
        assertEquals(0, PostBO.count());
        assertEquals(0, CommentBO.count());
    }
    
    @Test
    public void fullTest() {
        Fixtures.loadModels("data.yml");
     
        // Count things
        assertEquals(2, UserBO.count());
        assertEquals(3, PostBO.count());
        assertEquals(3, CommentBO.count());
     
        // Try to connect as users
        assertNotNull(UserBO.connect("bob@gmail.com", "secret"));
        assertNotNull(UserBO.connect("jeff@gmail.com", "secret"));
        assertNull(UserBO.connect("jeff@gmail.com", "badpassword"));
        assertNull(UserBO.connect("tom@gmail.com", "secret"));
     
        // Find all of Bob's posts
        List<PostBO> bobPosts = PostBO.find("author.email", "bob@gmail.com").fetch();
        assertEquals(2, bobPosts.size());
     
        // Find all comments related to Bob's posts
        List<CommentBO> bobComments = CommentBO.find("post.author.email", "bob@gmail.com").fetch();
        assertEquals(3, bobComments.size());
     
        // Find the most recent post
        PostBO frontPost = PostBO.find("order by postedAt desc").first();
        assertNotNull(frontPost);
        assertEquals("About the model layer", frontPost.getTitle());
     
        // Check that this post has two comments
        assertEquals(2, frontPost.getComments().size());
     
        // Post a new comment
        frontPost.addComment("Jim", "Hello guys");
        assertEquals(3, frontPost.getComments().size());
        assertEquals(4, CommentBO.count());
    }
    
    @Test
    public void testTags() {
        // Create a new user and save it
        UserBO bob = new UserBO("bob@gmail.com", "secret", "Bob").save();
     
        // Create a new post
        PostBO bobPost = new PostBO(bob, "My first post", "Hello world").save();
        PostBO anotherBobPost = new PostBO(bob, "Hop", "Hello world").save();
     
        // Well
        assertEquals(0, PostBO.findTaggedWith("Red").size());
     
        // Tag it now
        bobPost.tagItWith("Red").tagItWith("Blue").save();
        anotherBobPost.tagItWith("Red").tagItWith("Green").save();
     
        // Check
        assertEquals(2, PostBO.findTaggedWith("Red").size());
        assertEquals(1, PostBO.findTaggedWith("Blue").size());
        assertEquals(1, PostBO.findTaggedWith("Green").size());
        assertEquals(1, PostBO.findTaggedWith("Red", "Blue").size());
        assertEquals(1, PostBO.findTaggedWith("Red", "Green").size());
        assertEquals(0, PostBO.findTaggedWith("Red", "Green", "Blue").size());
        assertEquals(0, PostBO.findTaggedWith("Green", "Blue").size());
        
        List<Map> cloud = TagBO.getCloud();
        assertEquals(
            "[{tag=Blue, pound=1}, {tag=Green, pound=1}, {tag=Red, pound=2}]",
            cloud.toString()
        );
    }

}
