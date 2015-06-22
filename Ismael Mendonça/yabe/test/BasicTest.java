import java.util.List;

import models.CommentBO;
import models.PostBO;
import models.TagBO;
import models.UserBO;

import org.hibernate.mapping.Map;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class BasicTest extends UnitTest {
	
	@Before
	public void setup( ) {
		
		Fixtures.deleteDatabase( );
	}
	
	@Test
	public void createAndRetrieveUser( ) {
		
		new UserBO("bob@gmail.com", "secret", "Bob").save( );
		
		UserBO bob = UserBO.find("byEmail", "bob@gmail.com").first( );
		
		assertNotNull(bob);
		assertEquals("Bob", bob.fullname);
	}
	
	@Test
	public void tryConnectAsUser( ) {
		
		new UserBO("bob@gmail.com", "secret", "Bob").save( );
		
		assertNotNull(UserBO.connect("bob@gmail.com", "secret"));
		assertNull(UserBO.connect("bob@gmail.com", "badpassword"));
		assertNull(UserBO.connect("tom@gmail.com", "secret"));
	}
	
	@Test
	public void createPost( ) {
		
		UserBO bob = new UserBO("bob@gmail.com", "secret", "Bob").save( );
		
		new PostBO(bob, "My first post", "Hello world").save( );
		
		assertEquals(1, PostBO.count( ));
		
		List<PostBO> bobPosts = PostBO.find("byAuthor", bob).fetch( );
		
		assertEquals(1, bobPosts.size( ));
		PostBO firstPost = bobPosts.get(0);
		assertNotNull(firstPost);
		assertEquals(bob, firstPost.author);
		assertEquals("My first post", firstPost.title);
		assertEquals("Hello world", firstPost.content);
		assertNotNull(firstPost.postedAt);
	}
	
	@Test
	public void postComments( ) {
		
		UserBO bob = new UserBO("bob@gmail.com", "secret", "Bob").save( );
		
		PostBO bobPost = new PostBO(bob, "My first post", "Hello world").save( );
		
		new CommentBO(bobPost, "Jeff", "Nice post").save( );
		new CommentBO(bobPost, "Tom", "I knew that !").save( );
		
		List<CommentBO> bobPostComments = CommentBO.find("byPost", bobPost).fetch( );
		
		assertEquals(2, bobPostComments.size( ));
		
		CommentBO firstComment = bobPostComments.get(0);
		assertNotNull(firstComment);
		assertEquals("Jeff", firstComment.author);
		assertEquals("Nice post", firstComment.content);
		assertNotNull(firstComment.postedAt);
		
		CommentBO secondComment = bobPostComments.get(1);
		assertNotNull(secondComment);
		assertEquals("Tom", secondComment.author);
		assertEquals("I knew that !", secondComment.content);
		assertNotNull(secondComment.postedAt);
	}
	
	@Test
	public void useTheCommentsRelation( ) {
		
		UserBO bob = new UserBO("bob@gmail.com", "secret", "Bob").save( );
		
		PostBO bobPost = new PostBO(bob, "My first post", "Hello world").save( );
		
		bobPost.addComment("Jeff", "Nice post");
		bobPost.addComment("Tom", "I knew that !");
		
		assertEquals(1, UserBO.count( ));
		assertEquals(1, PostBO.count( ));
		assertEquals(2, CommentBO.count( ));
		
		bobPost = PostBO.find("byAuthor", bob).first( );
		assertNotNull(bobPost);
		
		assertEquals(2, bobPost.comments.size( ));
		assertEquals("Jeff", bobPost.comments.get(0).author);
		
		bobPost.delete();
		
		assertEquals(1, UserBO.count( ));
		assertEquals(0, PostBO.count( ));
		assertEquals(0, CommentBO.count( ));
	}
	
	@Test
	public void fullTest( ) {
		Fixtures.loadModels("data.yml");
		
		assertEquals(2, UserBO.count( ));
		assertEquals(3, PostBO.count( ));
		assertEquals(3, CommentBO.count( ));
		
		assertNotNull(UserBO.connect("bob@gmail.com", "secret"));
		assertNotNull(UserBO.connect("jeff@gmail.com", "secret"));
		assertNull(UserBO.connect("jeff@gmail.com", "badpassword"));
		assertNull(UserBO.connect("tom@gmail.com", "secret"));
		
		List<PostBO> bobPosts = PostBO.find("author.email", "bob@gmail.com").fetch( );
		assertEquals(2, bobPosts.size( ));
		
		List<CommentBO> bobComments = CommentBO.find("post.author.email", "bob@gmail.com").fetch( );
		assertEquals(3, bobComments.size( ));
		
		PostBO frontPost = PostBO.find("order by postedAt desc").first( );
		assertNotNull(frontPost);
		assertEquals("About the model layer", frontPost.title);
		
		assertEquals(2, frontPost.comments.size( ));
		
		frontPost.addComment("Jim", "Hello guys");
		assertEquals(3, frontPost.comments.size( ));
		assertEquals(4, CommentBO.count( ));
	}
	
	@Test
	public void testTags() {
		
		UserBO bob = new UserBO("bob@gmail.com", "secret", "Bob").save();
		
		PostBO bobPost = new PostBO(bob, "My first post", "Hello world").save();
		PostBO anotherBobPost = new PostBO(bob, "Hop", "Hello world").save();
		
		assertEquals(0, PostBO.findTaggedWith("Red").size());
		
		bobPost.tagItWith("Red").tagItWith("Blue").save();
		anotherBobPost.tagItWith("Red").tagItWith("Green").save();
		
		assertEquals(2, PostBO.findTaggedWith("Red").size());
		assertEquals(1, PostBO.findTaggedWith("Blue").size());
		assertEquals(1, PostBO.findTaggedWith("Green").size());
		assertEquals(1, PostBO.findTaggedWith("Red", "Blue").size());
		assertEquals(1, PostBO.findTaggedWith("Red", "Green").size());
		assertEquals(0, PostBO.findTaggedWith("Red", "Green", "Blue").size());
		assertEquals(0, PostBO.findTaggedWith("Green", "Blue").size());
		
		List<Map> cloud = TagBO.getCloud();
		assertEquals("[{tag=Blue, pound=1}, {tag=Green, pound=1}, {tag=Red, pound=2}]", cloud.toString());
	}
	
	
	
	
}
