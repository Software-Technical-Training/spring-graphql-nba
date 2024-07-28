package com.stti.nba.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.springframework.graphql.execution.ErrorType.BAD_REQUEST;
import static org.springframework.graphql.execution.ErrorType.NOT_FOUND;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import com.stti.nba.entity.Team;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureGraphQlTester
public class TeamControllerTests {

    @Autowired
    private GraphQlTester gqlTester;

    @Test
    public void testTeams(){
        String teamsQuery = "query { teams { id name city } }";

        List<Team> teamList = gqlTester.document(teamsQuery)
            .execute()
            .path("data.teams[*]")
            .entityList(Team.class).get();

        assertTrue(teamList.size() > 0);
        assertEquals("Golden State Warriors", teamList.get(0).getName());

    }

    @Test
    public void testTeamByTeamId(){
        String teamsQuery = "query { team(teamId : \"1\") { id name city } }";

        Team team = gqlTester.document(teamsQuery)
            .execute()
            .path("data.team")
            .entity(Team.class).get();

        assertNotNull(team);
        assertEquals("Golden State Warriors", team.getName());

    }

    @Test
    public void testTeamByTeamIdDoesNotExist(){
        String teamsQuery = "query { team(teamId : \"100\") { id name city } }";

        gqlTester.document(teamsQuery)
          .execute()
          .errors()
          .expect(error -> error.getErrorType() == NOT_FOUND
            && "Team not found for id: 100".equals(error.getMessage()))
          .verify()
          .path("$.data")
          .matchesJson("{\n"
            + "        \"team\": null\n"
            + "    }");        

    }

    @Test
    public void testCreateTeam(){
        String mutationStr = "mutation {createTeam(name: \"Seattle Seahawks\", coach: \"Gary\", city: \"Seattle\") }";

        gqlTester.document(mutationStr)
            .execute()
            .path("data.createTeam").hasValue();
    }

    @Test
    public void testCreateTeamThatExists(){
        String mutationStr = "mutation {createTeam(name: \"Golden State Warriors\", coach: \"Gary\", city: \"Seattle\") }";

        gqlTester.document(mutationStr)
          .execute()
          .errors()
          .expect(error -> error.getErrorType() == BAD_REQUEST
            && "Team already exists with this name : Golden State Warriors".equals(error.getMessage()))
          .verify()
          .path("$.data")
          .matchesJson("{\n"
            + "        \"createTeam\": null\n"
            + "    }");        
    }

}
