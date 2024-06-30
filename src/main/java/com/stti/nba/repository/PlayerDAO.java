package com.stti.nba.repository;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.stti.nba.entity.Player;

@Repository
public class PlayerDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Player> getAllPlayers(){
        return jdbcTemplate.query("SELECT * from PLAYER", new PlayerRowMapper());
    }

    public List<Player> getPlayersInTeam(int teamId){
        return jdbcTemplate.query("SELECT * from PLAYER WHERE team_id= ?",new PlayerRowMapper(),teamId);
    }



}