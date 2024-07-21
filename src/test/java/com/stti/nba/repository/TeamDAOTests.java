package com.stti.nba.repository;

import static org.junit.Assert.assertEquals;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import com.stti.nba.entity.Team;
import com.stti.nba.errors.TeamNotFoundException;

// UNIT TESTING
@RunWith(MockitoJUnitRunner.class)
public class TeamDAOTests {

    @Mock
    JdbcTemplate jdbcTemplate;


    @BeforeAll
    static void setup(){

    }

    @Test 
    public void whenGetAllTeams_thenReturnList(){
        TeamDAO teamDAO = new TeamDAO();
        Team team1 = new Team();
        Team team2 = new Team();
        team1.setId(1);
        team1.setName("GSW");
        team1.setCity("SF");
 
        team2.setId(2);
        team2.setName("LAL");
        team2.setCity("LA");

        List<Team> teams = List.of(team1,team2);
        ReflectionTestUtils.setField(teamDAO, "jdbcTemplate", jdbcTemplate);
        Mockito.when(jdbcTemplate.query(ArgumentMatchers.anyString(), ArgumentMatchers.any(TeamRowMapper.class))).thenReturn(teams);
        List<Team> resultTeams = teamDAO.getAllTeams();
        assertEquals(2,resultTeams.size());
        assertEquals(1, resultTeams.get(0).getId());
    }

    @Test(expected = TeamNotFoundException.class)
    public void whenIdNotFound_ThrowsException(){
        TeamDAO teamDAO = new TeamDAO();
        ReflectionTestUtils.setField(teamDAO, "jdbcTemplate", jdbcTemplate);

        teamDAO.getTeamByTeamId(3);
    }



}
