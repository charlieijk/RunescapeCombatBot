package rs.kreme.ksbot.api.game;

import java.util.Collections;
import rs.kreme.ksbot.api.queries.NPCQuery;
import rs.kreme.ksbot.api.wrappers.KSNPC;

/**
 * Lightweight combat facade used solely so the example tasks can compile.
 * The values returned are configurable through setters for unit tests.
 */
public class Combat {
    private boolean inCombat;
    private int healthPercent = 100;
    private int currentHealth = 99;
    private int specEnergy = 0;
    private boolean specEnabled;
    private AttackStyle attackStyle = AttackStyle.ACCURATE;

    public boolean inCombat() {
        return inCombat;
    }

    public void setInCombat(boolean inCombat) {
        this.inCombat = inCombat;
    }

    public int getHealthPercent() {
        return healthPercent;
    }

    public void setHealthPercent(int healthPercent) {
        this.healthPercent = healthPercent;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public boolean isAttacking(KSNPC npc) {
        return inCombat && npc != null;
    }

    public NPCQuery getAttackableNPC(String... targetNames) {
        return new NPCQuery(Collections.emptyList());
    }

    public int getSpecEnergy() {
        return specEnergy;
    }

    public void setSpecEnergy(int specEnergy) {
        this.specEnergy = specEnergy;
    }

    public boolean isSpecEnabled() {
        return specEnabled;
    }

    public void enableSpecial() {
        this.specEnabled = true;
    }

    public AttackStyle getAttackStyle() {
        return attackStyle;
    }

    public void setAttackStyle(AttackStyle attackStyle) {
        this.attackStyle = attackStyle;
    }

    public enum AttackStyle {
        ACCURATE,
        AGGRESSIVE,
        CONTROLLED,
        DEFENSIVE,
        RANGING,
        CASTING
    }
}
