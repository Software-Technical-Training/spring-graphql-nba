package com.stti.nba.repository;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.stti.nba.entity.Team;
import com.stti.nba.errors.TeamAlreadyExistsException;
import com.stti.nba.errors.TeamNotFoundException;

@Repository
public class TeamDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    PlayerDAO playerDAO;

    @Autowired
    public void setDataSource(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Team> getAllTeams(){
        return jdbcTemplate.query("SELECT * from TEAM", new TeamRowMapper());
    }

    public Team getTeamByTeamId(int teamId){
        try {
            Team team = (Team)jdbcTemplate.queryForObject("SELECT * from TEAM where id=?", new TeamRowMapper(),teamId);
            team.setPlayers(playerDAO.getPlayersInTeam(teamId));
            return team;            
        } catch (Exception e) {
            throw new TeamNotFoundException("Team not found for id: " +  teamId);
        }
    }

    public int createTeam(String name, String coach,String city){
        try {
            return jdbcTemplate.update("INSERT into TEAM (name,coach,city) VALUES (?,?,?)", name, coach, city);            
        } catch (Exception e) {
            throw new TeamAlreadyExistsException("Team already exists with this name : " + name);
        }
    }





}
