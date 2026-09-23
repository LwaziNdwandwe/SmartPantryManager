package com.lwazi.smartpantrymanager;

public class Recipe {

    private int id;
    private String name;
    private String instructions;

    //Constructor
    public Recipe(String name, String instructions){
        this.name = name;
        this.instructions = instructions;
    }

    //Getters and Setters
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
            this.name = name;
    }

    public String getInstructions(){
        return instructions;
    }

    public void setInstructions(String instructions){
        this.instructions = instructions;
    }
}
