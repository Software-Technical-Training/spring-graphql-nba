package com.stti.nba.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;

import com.stti.nba.entity.Player;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureGraphQlTester
public class PlayerControllerTests {

     @Autowired
    private GraphQlTester gqlTester;

    @Test
    public void testPlayers(){
        String query = "query { players { id name } }";

        List<Player> playerList = gqlTester.document(query)
            .execute()
            .path("data.players[*]")
            .entityList(Player.class).get();

        assertTrue(playerList.size() > 0);
        assertEquals("Steph Curry", playerList.get(0).getName());

    }    
}
