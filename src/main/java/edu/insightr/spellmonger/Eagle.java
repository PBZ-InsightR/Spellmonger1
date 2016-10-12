package edu.insightr.spellmonger;


public class Eagle extends Creature {

    public Eagle(String name) {
        super(name + " [Eagle]", "");
        this.setHp(1);
        this.setAttack(1);
        this.setAlive(true);
    }

    public Eagle(String name, String owner) {
        super(name + " [Eagle]", owner);
        this.setHp(1);
        this.setAttack(1);
        this.setAlive(true);
    }

}
