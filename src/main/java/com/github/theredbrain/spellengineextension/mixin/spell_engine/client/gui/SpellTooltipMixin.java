package com.github.theredbrain.spellengineextension.mixin.spell_engine.client.gui;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.SpellEngineExtensionClient;
import com.github.theredbrain.spellengineextension.component.type.HasConditionalSpellContainerComponent;
import com.github.theredbrain.spellengineextension.config.ClientConfig;
import com.github.theredbrain.spellengineextension.config.ServerConfig;
import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellCostMixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.client.gui.SpellTooltip;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(SpellTooltip.class)
public abstract class SpellTooltipMixin {

    @Shadow
    private static MutableText indentation(int level) {
        throw new AssertionError();
    }

    @WrapOperation(method = "addSpellLines", at = @At(value = "INVOKE", target = "Lnet/spell_engine/client/gui/SpellTooltip;getSpellInfo(Lnet/minecraft/item/ItemStack;Lnet/spell_engine/api/spell/container/SpellContainer;Lnet/minecraft/entity/player/PlayerEntity;ZZ)Lnet/spell_engine/client/gui/SpellTooltip$SpellInfo;"))
    private static SpellTooltip.SpellInfo spellengineextension$wrap_getSpellInfo(ItemStack itemStack, SpellContainer container, PlayerEntity player, boolean forceHideHeader, boolean allowDetailsHint, Operation<SpellTooltip.SpellInfo> original) {
        ClientConfig clientConfig = SpellEngineExtensionClient.CLIENT_CONFIG;
        HasConditionalSpellContainerComponent hasConditionalSpellContainerComponent = itemStack.get(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER);
        if (clientConfig.always_hide_details_hint.get() || (clientConfig.hide_details_hint_for_invalid_conditional_spell_container.get() && hasConditionalSpellContainerComponent != null && !hasConditionalSpellContainerComponent.is_valid())) {
            return SpellTooltip.getSpellInfo(itemStack, container, player, forceHideHeader, false);
        } else {
            return original.call(itemStack, container, player, forceHideHeader, allowDetailsHint);
        }
    }

    @Inject(method = "addSpellDetails", at = @At("TAIL"))
    private static void spellengineextension$addSpellDetails(RegistryEntry<Spell> spellEntry, PlayerEntity player, ItemStack itemStack, int indentLevel, ArrayList<Text> lines, CallbackInfo ci) {

        ServerConfig spellEngineExtensionConfig = SpellEngineExtension.SERVER_CONFIG;

        // called spell1 to avoid potential problems with spell field in original method
        Spell spell1 = spellEntry.value();

        if (spellEngineExtensionConfig.spell_cost_health_allowed.get() && spell1.cost != null) {
            float healthCost = ((DuckSpellCostMixin) spell1.cost).spellengineextension$getHealthCost();
            if (healthCost != 0.0F) {
                boolean hasEnoughHealth = !((DuckSpellCostMixin) spell1.cost).spellengineextension$checkHealthCost() || healthCost <= 0 || healthCost < player.getHealth();
                lines.add(indentation(indentLevel).append(Text.translatable("spell.tooltip.health", healthCost).formatted(hasEnoughHealth ? Formatting.GREEN : Formatting.RED)));
            }
        }

        if (SpellEngineExtension.isManaAttributesLoaded && spellEngineExtensionConfig.spell_cost_mana_allowed.get() && spell1.cost != null) {
            float manaCost = ((DuckSpellCostMixin) spell1.cost).spellengineextension$getManaCost();
            if (manaCost != 0.0F) {
                float currentMana = SpellEngineExtension.getCurrentMana(player);
                boolean hasEnoughMana = !((DuckSpellCostMixin) spell1.cost).spellengineextension$checkMana() || (manaCost > 0 && (manaCost < currentMana || !((DuckSpellCostMixin) spell1.cost).spellengineextension$checkManaCost()));
                lines.add(indentation(indentLevel).append(Text.translatable("spell.tooltip.mana", manaCost).formatted(hasEnoughMana ? Formatting.GREEN : Formatting.RED)));
            }
        }

        if (SpellEngineExtension.isStaminaAttributesLoaded && spellEngineExtensionConfig.spell_cost_stamina_allowed.get() && spell1.cost != null) {
            float staminaCost = ((DuckSpellCostMixin) spell1.cost).spellengineextension$getStaminaCost();
            if (staminaCost != 0.0F) {
                float currentStamina = SpellEngineExtension.getCurrentStamina(player);
                boolean hasEnoughStamina = !((DuckSpellCostMixin) spell1.cost).spellengineextension$checkStamina() || (staminaCost > 0 && (staminaCost < currentStamina || !((DuckSpellCostMixin) spell1.cost).spellengineextension$checkStaminaCost()));
                lines.add(indentation(indentLevel).append(Text.translatable("spell.tooltip.stamina", staminaCost).formatted(hasEnoughStamina ? Formatting.GREEN : Formatting.RED)));
            }
        }

        if (spellEngineExtensionConfig.spell_cost_effects_allowed.get() && spell1.cost != null && spell1.cost.effect_id != null && !spell1.cost.effect_id.isEmpty()) {
            Optional<RegistryEntry.Reference<StatusEffect>> optionalStatusEffectReference = Registries.STATUS_EFFECT.getEntry(Identifier.tryParse(spell1.cost.effect_id));
            if (optionalStatusEffectReference.isPresent()) {
                RegistryEntry.Reference<StatusEffect> statusEffectReference = optionalStatusEffectReference.get();
                int decrementEffectAmount = ((DuckSpellCostMixin) spell1.cost).spellengineextension$getDecrementEffectAmount();
                StatusEffectInstance statusEffectInstance = player.getStatusEffect(statusEffectReference);
                int currentAmplifier = -1;
                if (statusEffectInstance != null) {
                    currentAmplifier = statusEffectInstance.getAmplifier();
                }
                boolean checkEffectCost = ((DuckSpellCostMixin) spell1.cost).spellengineextension$checkEffectCost();
                boolean hasRequiredEffectAndLevel = !checkEffectCost || (player.hasStatusEffect(statusEffectReference) && (currentAmplifier + 1 >= decrementEffectAmount || decrementEffectAmount <= 0));
                lines.add(
                        indentation(indentLevel)
                                .append(checkEffectCost ? Text.translatable("spell.tooltip.effect.1") : Text.translatable("spell.tooltip.effect.2"))
                                .append(statusEffectReference.value().getName().copy())
                                .append(ScreenTexts.SPACE)
                                .append(decrementEffectAmount > 1 ? Text.translatable("enchantment.level." + (decrementEffectAmount - 1)).append(ScreenTexts.SPACE) : Text.empty())
                                .append(Text.translatable("spell.tooltip.effect.3"))
                                .formatted(hasRequiredEffectAndLevel ? Formatting.GREEN : Formatting.RED)
                );
            }
        }
    }
}
