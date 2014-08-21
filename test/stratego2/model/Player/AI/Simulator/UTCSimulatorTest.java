/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stratego2.model.Player.AI.Simulator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import stratego2.model.Color;

/**
 *
 * @author roussew
 */
public class UTCSimulatorTest {
    
    public UTCSimulatorTest() {
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
     * Test of run method, of class UTCSimulator.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        UTCSimulator instance = null;
        instance.run();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getWinner method, of class UTCSimulator.
     */
    @Test
    public void testGetWinner() {
        System.out.println("getWinner");
        UTCSimulator instance = null;
        Color expResult = null;
        Color result = instance.getWinner();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDepth method, of class UTCSimulator.
     */
    @Test
    public void testGetDepth() {
        System.out.println("getDepth");
        UTCSimulator instance = null;
        int expResult = 0;
        int result = instance.getDepth();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
