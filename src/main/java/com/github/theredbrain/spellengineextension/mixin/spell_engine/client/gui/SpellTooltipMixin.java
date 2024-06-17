package com.github.theredbrain.spellengineextension.mixin.spell_engine.client.gui;

import com.github.theredbrain.manaattributes.entity.ManaUsingEntity;
import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.config.ServerConfig;
import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellCostMixin;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_power.api.SpellPower;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;

@Mixin(SpellTooltip.class)
public class SpellTooltipMixin {

    @Inject(method = "spellInfo", at = @At("TAIL"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void spellengineextension$spellInfo(Identifier spellId, PlayerEntity player, ItemStack itemStack, boolean details, CallbackInfoReturnable<List<Text>> cir, ArrayList lines, Spell spell, SpellPower.Result primaryPower, MutableText name, String description, SpellTooltip.DescriptionMutator mutator, float cooldownDuration, boolean showItemCost, net.spell_engine.config.ServerConfig config) {

        ServerConfig spellEngineExtensionConfig = SpellEngineExtension.serverConfig;

        if (spellEngineExtensionConfig.spell_cost_health_allowed && spell.cost != null) {
            float healthCost = ((DuckSpellCostMixin) spell.cost).betteradventuremode$getHealthCost();
            if (healthCost != 0.0F) {
                boolean hasEnoughHealth = !((DuckSpellCostMixin) spell.cost).betteradventuremode$checkHealthCost() || healthCost <= 0 || healthCost < player.getHealth();
                lines.add(Text.literal(" ").append(Text.translatable("spell.tooltip.health", healthCost).formatted(hasEnoughHealth ? Formatting.GREEN : Formatting.RED)));
            }
        }

        if (SpellEngineExtension.isManaAttributesLoaded && spellEngineExtensionConfig.spell_cost_mana_allowed && spell.cost != null) {
            float manaCost = ((DuckSpellCostMixin) spell.cost).betteradventuremode$getManaCost();
            if (manaCost != 0.0F) {
                boolean hasEnoughMana = !((DuckSpellCostMixin) spell.cost).betteradventuremode$checkManaCost() || manaCost <= 0 || manaCost < ((ManaUsingEntity) player).manaattributes$getMana();
                lines.add(Text.literal(" ").append(Text.translatable("spell.tooltip.mana", manaCost).formatted(hasEnoughMana ? Formatting.GREEN : Formatting.RED)));
            }
        }

        if (SpellEngineExtension.isStaminaAttributesLoaded && spellEngineExtensionConfig.spell_cost_stamina_allowed && spell.cost != null) {
            float staminaCost = ((DuckSpellCostMixin) spell.cost).betteradventuremode$getStaminaCost();
            if (staminaCost != 0.0F) {
                boolean hasEnoughStamina = !((DuckSpellCostMixin) spell.cost).betteradventuremode$checkStaminaCost() || staminaCost <= 0 || staminaCost < ((StaminaUsingEntity) player).staminaattributes$getStamina();
                lines.add(Text.literal(" ").append(Text.translatable("spell.tooltip.stamina", staminaCost).formatted(hasEnoughStamina ? Formatting.GREEN : Formatting.RED)));
            }
        }

        if (spellEngineExtensionConfig.spell_cost_effects_allowed && spell.cost != null && spell.cost.effect_id != null && !spell.cost.effect_id.isEmpty()) {
            StatusEffect effect = (StatusEffect) Registries.STATUS_EFFECT.get(new Identifier(spell.cost.effect_id));
            if (effect != null) {
                int decrementEffectAmount = ((DuckSpellCostMixin) spell.cost).betteradventuremode$getDecrementEffectAmount();
                StatusEffectInstance statusEffectInstance = player.getStatusEffect(effect);
                int currentAmplifier = -1;
                if (statusEffectInstance != null) {
                    currentAmplifier = statusEffectInstance.getAmplifier();
                }
                boolean hasRequiredEffectAndLevel = player.hasStatusEffect(effect) && (currentAmplifier + 1 >= decrementEffectAmount || decrementEffectAmount <= 0);
                lines.add(
                        Text.literal(" ")
                                .append(Text.translatable("spell.tooltip.effect.1"))
                                .append(effect.getName().copy())
                                .append(ScreenTexts.SPACE)
                                .append(decrementEffectAmount > 1 ? Text.translatable("enchantment.level." + (decrementEffectAmount - 1)).append(ScreenTexts.SPACE) : Text.empty())
                                .append(Text.translatable("spell.tooltip.effect.2"))
                                .formatted(hasRequiredEffectAndLevel ? Formatting.GREEN : Formatting.RED)
                );
            }
        }
    }
}
