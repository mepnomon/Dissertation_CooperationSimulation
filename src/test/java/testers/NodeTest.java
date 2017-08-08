package testers;

import Game_Models.Node;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author D.B. Dressler
 */
public class NodeTest {
    
    public NodeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getCoopStatus method, of class Node.
     */
    @Test
    public void testGetCoopStatus() {
        System.out.println("getCoopStatus");
        Node instance = new Node(1, false);
        boolean result = instance.getCoopStatus();
        
        assertNotNull(result);
        System.out.println("Coop Status:" + result);
    }

//    /**
//     * Test of getPoolContribution method, of class Node.
//     */
//    @Test
//    public void testGetPoolContribution() {
//        System.out.println("getPoolContribution");
//        Node instance = new Node(1);
//        int expResult;
//       
//        if(!instance.getCoopStatus()){
//            expResult = 0;
//            assertEquals(instance.getPoolContribution(),expResult);
//        } else {
//            expResult = 1;
//            assertEquals(instance.getPoolContribution(),expResult);
//        }
//    }
//
//    /**
//     * Test of getLocation method, of class Node.
//     */
//    @Test
//    public void testGetLocation() {
//        System.out.println("getLocation");
//        Node instance = new Node(0);
//        int expResult = 0;
//        int result = instance.getLocation();
//        assertEquals(expResult, result);
//    }
    
}
