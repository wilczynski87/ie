package dlarodziny.wolontariusze.ie.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class AttitudeTests {
    
    @Test
    void justAnExample() {
        System.out.println("This test method should be run");
    }

    /**
     * InnerAttitudeTests
     */
    @Nested
    class InnerAttitudeTestsFindByValue {
        @Test
        void findByValueTest() {
            // Given
            String testValue1 = "Zimny";

            // When
            var result = Attitude.findByValue(testValue1);
            System.out.println(result);
            // Then
            assertEquals(Attitude.ZIMNY, result);
        }

        @Test
        void findByValueTestWrong() {
            // Given
            String testValue1 = "Zimny";

            // When
            var result = Attitude.findByValue(testValue1);
            System.out.println(result);
            // Then
            assertNotEquals(Attitude.GORACY, result);
        }
        @Test
        void findByValueTestAddedChars() {
            System.out.println("Test start");
            // Given
            String testValue1 = "? Chłodny";
            // When
            var result = Attitude.findByValue(testValue1);
            
            // Then
            assertEquals(Attitude.CHLODNY, result);
        }

        
    }
    
}