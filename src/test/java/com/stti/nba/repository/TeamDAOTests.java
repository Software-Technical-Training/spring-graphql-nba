package com.stti.nba.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
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

import com.stti.nba.entity.Player;
import com.stti.nba.entity.Team;
import com.stti.nba.errors.TeamAlreadyExistsException;
import com.stti.nba.errors.TeamNotFoundException;

// UNIT TESTING
@ExtendWith(MockitoExtension.class)
public class TeamDAOTests {

    @Mock JdbcTemplate jdbcTemplate;
    @Mock PlayerDAO playerDAO;
    TeamDAO teamDAO;
    Team team1 = new Team();
    Team team2 = new Team();
    List<Team> teams;


    @BeforeEach
    void setup(){
        teamDAO = new TeamDAO();
        ReflectionTestUtils.setField(teamDAO, "jdbcTemplate", jdbcTemplate);
        ReflectionTestUtils.setField(teamDAO, "playerDAO", playerDAO);
        team1.setId(1);
        team1.setName("GSW");
        team1.setCity("SF");
 
        team2.setId(2);
        team2.setName("LAL");
        team2.setCity("LA");

        teams = List.of(team1,team2);
    }

    @Test 
    public void whenGetAllTeams_thenReturnList(){
        Mockito.when(jdbcTemplate.query(ArgumentMatchers.anyString(), ArgumentMatchers.any(TeamRowMapper.class))).thenReturn(teams);

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
    public void whenTeamIdPassed_thenReturnTeam(){
        Mockito.when(jdbcTemplate.queryForObject(anyString(), any(TeamRowMapper.class), ArgumentMatchers.anyInt())).thenReturn(team1);
        Mockito.when(playerDAO.getPlayersInTeam(ArgumentMatchers.anyInt())).thenReturn(new ArrayList<Player>());

        Team team = teamDAO.getTeamByTeamId(1);
        assertEquals(team1.getName(), team.getName());
    }

    @Test()
    public void whenTeamIdNotFound_ThrowsException(){
        Mockito.when(jdbcTemplate.queryForObject(anyString(), any(TeamRowMapper.class), 
            ArgumentMatchers.anyInt())).thenThrow(new RuntimeException());

        assertThrows(TeamNotFoundException.class, () -> teamDAO.getTeamByTeamId(3));
    }

    @Test
    public void whenCreateTeam_thenReturnSuccess(){
        Mockito.when(jdbcTemplate.update(anyString(), anyString(),anyString(),anyString())).thenReturn(1);
        assertEquals(teamDAO.createTeam("MIA", "", ""), 1);
    }

    @Test
    public void whenCreateTeamWithExistingName_thenThrowsException(){
        Mockito.when(jdbcTemplate.update(anyString(), anyString(),anyString(),anyString())).thenThrow(new RuntimeException());
        assertThrows(TeamAlreadyExistsException.class, () -> teamDAO.createTeam("GSW", "null", "null"));
    }



}
