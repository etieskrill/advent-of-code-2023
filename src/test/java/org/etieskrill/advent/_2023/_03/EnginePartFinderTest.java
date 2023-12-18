package org.etieskrill.advent._2023._03;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnginePartFinderTest {

    @Test
    void testSymbol() {
        assertTrue(EnginePartFinder.hasSymbol(
                List.of(".*...", "..5..", "....."),
                1, 2, 1));
    }
  
}