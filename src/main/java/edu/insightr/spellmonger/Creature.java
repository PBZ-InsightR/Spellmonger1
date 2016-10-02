package edu.insightr.spellmonger;

/**
 * Created by Guillaume on 01/10/2016.
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Creature extends Card {

    private int hp;
    private int attack;
    private boolean alive;

    static ArrayList<Creature> allCreatures = new ArrayList<Creature>();
    static ArrayList<Creature> temp;// = allCreatures;

    void killCreature() {
        allCreatures.remove(this);
    }

    public Creature(String name, String owner, int hp){
        super(name, owner);
        this.setHp(hp);
        this.setAttack(hp);
        this.setAlive(true);
        allCreatures.add(this);
    }

    public Creature(String name, String owner){
        super(name, owner);
        this.setHp(0);
        this.setAttack(0);
        this.setAlive(true);
        allCreatures.add(this);
    }

    @Override
    public String toString(){
        return super.toString()+ "hp = "+this.hp+", attack = "+this.attack+", alive = "+this.alive;
    }

    public static int getCreaDamageForPlayer(Player player){
        int damage = 0;
        List<Creature> creatures = getPlayerCreatures(player.getName());
        for(Creature crea : creatures)
        {
            damage += crea.getAttack();
        }
        return damage;
    }

    public static void displayGroupOfCrea(List<Creature> listOfCrea){
        int i = 1;
        System.out.println("********Displaying the creatures :");
        for (Creature crea : listOfCrea) {
            System.out.println("Creature "+i+" : "+crea.getName());
            i++;
        }
        System.out.println("********END");
    }

    public static List<Creature> getPlayerCreaOnBoard(Player player){

        List<Creature> creaOnBoard = (ArrayList<Creature>) allCreatures.clone();

        if (creaOnBoard.isEmpty()){
            return null;
        }
        else {
            Iterator<Creature> i = creaOnBoard.iterator();
            while (i.hasNext()) {
                Creature crea = i.next();
                if (!((crea.getOwner() == player.getName()) && (crea.isDraw()) && (crea.getHp() > 0))) {
                    i.remove();
                    //System.out.println("remov: owner:"+crea.getOwner()+"/draw?:"+crea.isDraw()+"/hp:"+crea.getHp());
                }
                else{
                    //System.out.println("OPPONENT CREATURE ON BOARD : "+i.next().getName()); // go delete le else
                }
            }

            /*for (Iterator<Creature> iterator = oppCreaOnBoard.iterator(); iterator.hasNext(); ) {
                Creature crea = iterator.next();
                //System.out.println("all creatures : "+crea.toString());
                if (!((crea.getOwner() != player.getName()) && (!crea.isDraw()) && (crea.getHp() <= 0))) {
                    iterator.remove();
                }
                else{
                    System.out.println("THIS IS GOOD / crea on board : "+crea.toString()); // go delete le else
                }
            }*/

            return creaOnBoard;
        }
    }

    public static List<Creature> getPlayerCreatures(String playerName){
        temp = allCreatures; // use clone?
        for(int i=0; i<temp.size(); i++) {
            if (temp.get(i).getOwner() != playerName) {
                temp.remove(temp.get(i));
            }
        }
        return temp;
    }

    public void attackOpponent(Player opponent){
        opponent.setHp(opponent.getHp()- this.getAttack());
        if (opponent.getHp() <= 0){
            opponent.setAlive(false);
            System.out.println(opponent.getName()+" is dead !");
        }
    }

    public void attackCreature(Creature creature){
        creature.setHp(creature.getHp()-this.getAttack());
        this.setHp(this.getHp()-creature.getAttack());
        if (creature.getHp() <= 0){
            creature.setAlive(false);
            creature.killCreature();
        }
        if (this.getHp() <= 0){
            this.setAlive(false);
            this.killCreature();
        }
    }

    public Creature findBestTarget(int attack, int hp, Player opponent){
        Creature bestTarget = null;
        List<Creature> potentialTargets = new ArrayList<>();
        List<Creature> opponentCrea = getPlayerCreaOnBoard(opponent);

        //Player opponentPlayer = Player.getCurrentOpponent();
        //System.out.println("opponent = "+opponentPlayer.getName()+" / "+opponentPlayer);

        //we retrieve all opponent creatures on board
        if (opponentCrea == null){
            return null;
        }
        else { // enters here, good
            for (int i = 0; i < opponentCrea.size(); i++) {
                if (opponentCrea.get(i).getOwner() == opponent.getName()) {
                    potentialTargets.add(opponentCrea.get(i));
                }
            }
        }

        //we only keep the target we can kill (if they are none, we'll attack opponent)
        //System.out.print("size of potential targets : "+potentialTargets.size());
        for(int i=0; i<potentialTargets.size(); i++) {
            if (potentialTargets.get(i).getHp() > attack) {
                potentialTargets.remove(potentialTargets.get(i));
            }
        }

        //we check on targets if we can stay alive while killing them
        for(int i=0; i<potentialTargets.size(); i++) {
            if (potentialTargets.get(i).getAttack() >= hp) {
                potentialTargets.remove(potentialTargets.get(i));
            }
        }

        //then we choose the one with the highest health
        int healthiest = 0;
        for(int i=0; i<potentialTargets.size(); i++) {
            if (potentialTargets.get(i).getHp() > healthiest){
                healthiest = potentialTargets.get(i).getHp();
            }
        }
        for(Creature target : potentialTargets)
        {
            if(target.getHp() == healthiest)
                bestTarget = target;
        }

        return bestTarget;
    }

    public void attack(Player opponent){

        //Player opponent = Player.getCurrentOpponent();
        //System.out.println("opponent :"+opponent.getName());
        Creature bestCreaTarget = findBestTarget(this.getHp(), this.getAttack(), opponent);

        /*if (bestCreaTarget != null){
            System.out.println("(crea should be attacked) - trying to make creature attack :"+opponent.getName()+" or "+bestCreaTarget.getName());
        }
        else{
            System.out.println("best target is null");
        }*/

        if (bestCreaTarget ==  null){
            attackOpponent(opponent);
            System.out.println(this.getName()+" attacks "+opponent.getName()+" and deals "+this.getAttack()+" damage");
        }
        else{
            attackCreature(bestCreaTarget);
            System.out.println(this.getName()+" attacks "+bestCreaTarget.getName()+" and deals it "+this.getAttack()+" damage and receives "+bestCreaTarget.getAttack()+" damage");
        }
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

}
