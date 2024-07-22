package com.stti.nba.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import com.stti.nba.entity.Team;
import com.stti.nba.errors.TeamNotFoundException;

// UNIT TESTING
@ExtendWith(MockitoExtension.class)
public class TeamDAOTests {

    @Mock JdbcTemplate jdbcTemplate;
    TeamDAO teamDAO;


    @BeforeEach
    void setup(){
        teamDAO = new TeamDAO();
        ReflectionTestUtils.setField(teamDAO, "jdbcTemplate", jdbcTemplate);
        Team team1 = new Team();
        Team team2 = new Team();
        team1.setId(1);
        team1.setName("GSW");
        team1.setCity("SF");
 
        team2.setId(2);
        team2.setName("LAL");
        team2.setCity("LA");

        List<Team> teams = List.of(team1,team2);
        Mockito.when(jdbcTemplate.query(ArgumentMatchers.anyString(), ArgumentMatchers.any(TeamRowMapper.class))).thenReturn(teams);
    }

    @Test 
    public void whenGetAllTeams_thenReturnList(){
        
        List<Team> resultTeams = teamDAO.getAllTeams();
        assertEquals(2,resultTeams.size());
        assertAll("resultTeams",
            () -> {
                Team firstTeam = resultTeams.get(0);
                assertNotNull(firstTeam);
                assertAll("firstTeam",
                    () -> {
                        assertEquals("GSW", firstTeam.getName());
                        assertEquals("SF",firstTeam.getCity());
                    }
                );
            }
        );
    }

    @Test()
    public void whenIdNotFound_ThrowsException(){
        assertThrows(TeamNotFoundException.class, () -> teamDAO.getTeamByTeamId(3));
    }



}
