package rs.kreme.ksbot.api.scripts.combatbot;

import java.util.List;
import rs.kreme.ksbot.api.game.Combat;
import rs.kreme.ksbot.api.wrappers.KSGroundItem;
import rs.kreme.ksbot.api.wrappers.KSItem;
import rs.kreme.ksbot.api.wrappers.KSNPC;

/**
 * Simple command-line runner that executes the combat bot against the stubbed API.
 * This is purely for demonstration/testing and does not interact with the real game.
 */
public final class CombatBotRunner {

    private CombatBotRunner() {
    }

    public static void main(String[] args) {
        CombatBot bot = new CombatBot();
        configureDemoEnvironment(bot);

        if (!bot.onStart()) {
            System.out.println("Unable to start Combat Bot");
            return;
        }

        // Stage 1: low health forces the food task
        bot.runLoop(1);
        bot.ctx.combat.setHealthPercent(80);
        bot.ctx.combat.setCurrentHealth(80);

        // Stage 2: enable prayer + combat to showcase buffs/special attacks
        bot.setUsePrayer(true);
        bot.ctx.combat.setInCombat(true);
        bot.runLoop(1);

        // Stage 3: return to idle combat and let the bot attack NPCs
        bot.setUsePrayer(false);
        bot.ctx.combat.setInCombat(false);
        bot.ctx.groundItems.setItems(List.of()); // keep focus on combat
        bot.runLoop(2);

        // Stage 4: drop loot and let the looting task handle it
        bot.ctx.groundItems.setItems(List.of(
                new KSGroundItem("Rune scimitar"),
                new KSGroundItem("Dragon bones")
        ));
        bot.runLoop(1);

        bot.onStop();
    }

    private static void configureDemoEnvironment(CombatBot bot) {
        bot.setUsePrayer(false);
        bot.setPrayerNames(new String[]{"Protect from Melee", "Piety"});
        bot.setCheckEquipment(false); // keep the demo focused on combat
        bot.setUseAntiBan(false);

        bot.ctx.combat.setInCombat(false);
        bot.ctx.combat.setHealthPercent(40);
        bot.ctx.combat.setCurrentHealth(35);
        bot.ctx.combat.setSpecEnergy(100);
        bot.ctx.combat.setAttackStyle(Combat.AttackStyle.ACCURATE);
        bot.ctx.combat.setAttackableNpcs(List.of(
                new KSNPC("Goblin", 5),
                new KSNPC("Cow", 3)
        ));

        bot.ctx.inventory.setItems(List.of(
                new KSItem("Lobster"),
                new KSItem("Lobster"),
                new KSItem("Swordfish"),
                new KSItem("Magic potion")
        ));

        bot.ctx.equipment.setItems(List.of(
                new KSItem("Barrows chestplate degraded")
        ));
        bot.ctx.groundItems.setItems(List.of());
    }
}
