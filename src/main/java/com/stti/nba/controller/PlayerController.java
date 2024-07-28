package com.stti.nba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.stti.nba.entity.Player;
import com.stti.nba.repository.PlayerDAO;

@Controller
public class PlayerController {

    @Autowired
    PlayerDAO playerDAO;

    @QueryMapping
    public List<Player> players(){
        return playerDAO.getAllPlayers();
    }
}
