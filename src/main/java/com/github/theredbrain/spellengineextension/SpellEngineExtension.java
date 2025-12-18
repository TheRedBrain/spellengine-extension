package com.github.theredbrain.spellengineextension;

import com.github.theredbrain.spellengineextension.compat.ManaAttributesCompatibility;
import com.github.theredbrain.spellengineextension.compat.MergedItemsCompatibility;
import com.github.theredbrain.spellengineextension.compat.AttackRangeAttributeCompat;
import com.github.theredbrain.spellengineextension.compat.RPGInventoryCompatibility;
import com.github.theredbrain.spellengineextension.compat.RangedWeaponAPICompatibility;
import com.github.theredbrain.spellengineextension.compat.StaminaAttributesCompatibility;
import com.github.theredbrain.spellengineextension.component.type.HasConditionalSpellContainerComponent;
import com.github.theredbrain.spellengineextension.config.ServerConfig;
import com.github.theredbrain.spellengineextension.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.spellengineextension.predicate.item.SpellContainersPredicate;
import com.github.theredbrain.spellengineextension.registry.DataComponentPredicateRegistry;
import com.github.theredbrain.spellengineextension.registry.ItemComponentRegistry;
import com.github.theredbrain.spellengineextension.registry.SpellSchoolRegistry;
import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellActiveCastMixin;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.item.ItemSubPredicate;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.event.SpellEvents;
import net.spell_engine.api.spell.registry.SpellRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpellEngineExtension implements ModInitializer {
	public static final String MOD_ID = "spellengineextension";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig SERVER_CONFIG;

	public static RegistryEntry<EntityAttribute> GENERIC_MAGIC_DAMAGE;

	public static RegistryEntry<EntityAttribute> HEALTH_SPELL_COST_MULTIPLIER;
	public static RegistryEntry<EntityAttribute> MANA_SPELL_COST_MULTIPLIER;
	public static RegistryEntry<EntityAttribute> STAMINA_SPELL_COST_MULTIPLIER;

	public static RegistryEntry<EntityAttribute> EXTRA_LAUNCH_COUNT;
	public static RegistryEntry<EntityAttribute> EXTRA_LAUNCH_DELAY;
	public static RegistryEntry<EntityAttribute> EXTRA_VELOCITY;

	public static RegistryEntry<EntityAttribute> EXTRA_RICOCHET;
	public static RegistryEntry<EntityAttribute> EXTRA_RICOCHET_RANGE;
	public static RegistryEntry<EntityAttribute> EXTRA_BOUNCE;
	public static RegistryEntry<EntityAttribute> EXTRA_PIERCE;
	public static RegistryEntry<EntityAttribute> EXTRA_CHAIN_REACTION_SIZE;
	public static RegistryEntry<EntityAttribute> EXTRA_CHAIN_REACTION_TRIGGERS;

	public static ComponentType<HasConditionalSpellContainerComponent> HAS_CONDITIONAL_SPELL_CONTAINER;
	public static ComponentType<Identifier> PROXY_POOL;

	public static ItemSubPredicate.Type<SpellContainersPredicate> SPELL_CONTAINER_PREDICATE;

	public static final TagKey<Spell> CAN_BE_IN_USE_ITEM_SPELL_HOTBAR_SLOT = TagKey.of(SpellRegistry.KEY, SpellEngineExtension.identifier("can_be_in_use_item_spell_hotbar_slot"));
	public static final TagKey<Spell> ENABLES_MOVEMENT_LOCKING_DURING_CASTING = TagKey.of(SpellRegistry.KEY, SpellEngineExtension.identifier("enables_movement_locking_during_casting"));

	public static final boolean isManaAttributesLoaded = FabricLoader.getInstance().isModLoaded("manaattributes");
	public static final boolean isStaminaAttributesLoaded = FabricLoader.getInstance().isModLoaded("staminaattributes");
	public static final boolean isRangedWeaponAPILoaded = FabricLoader.getInstance().isModLoaded("ranged_weapon_api");
	public static final boolean isMergedItemsLoaded = FabricLoader.getInstance().isModLoaded("mergeditems");
	public static final boolean isAttackRangeAttributeLoaded = FabricLoader.getInstance().isModLoaded("attackrangeattribute");
	public static final boolean isRPGInventoryLoaded = FabricLoader.getInstance().isModLoaded("rpginventory");
//	public static final boolean isShoulderSurfingLoaded = FabricLoader.getInstance().isModLoaded("shouldersurfing"); // TODO Shoulder Surfing Compat

	public static SpellContainer addMergedSpellContainer(SpellContainer spellContainer, ItemStack itemStack) {
		if (isMergedItemsLoaded) {
			return MergedItemsCompatibility.addMergedSpellContainer(spellContainer, itemStack);
		} else {
			return spellContainer;
		}
	}

	public static float getCurrentMana(LivingEntity livingEntity) {
		float currentMana = 0.0F;
		if (isManaAttributesLoaded) {
			currentMana = ManaAttributesCompatibility.getCurrentMana(livingEntity);
		}
		return currentMana;
	}

	public static float getCurrentStamina(LivingEntity livingEntity) {
		float currentStamina = 0.0F;
		if (isStaminaAttributesLoaded) {
			currentStamina = StaminaAttributesCompatibility.getCurrentStamina(livingEntity);
		}
		return currentStamina;
	}

	public static float getItemUseStaminaCost(LivingEntity livingEntity) {
		float currentStamina = 0.0F;
		if (isStaminaAttributesLoaded) {
			currentStamina = StaminaAttributesCompatibility.getItemUseStaminaCost(livingEntity);
		}
		return currentStamina;
	}

	public static void addMana(LivingEntity livingEntity, float amount) {
		if (isManaAttributesLoaded) {
			ManaAttributesCompatibility.addMana(livingEntity, amount);
		}
	}

	public static void addStamina(LivingEntity livingEntity, float amount) {
		if (isStaminaAttributesLoaded) {
			StaminaAttributesCompatibility.addStamina(livingEntity, amount);
		}
	}

	public static double getAttackRange(PlayerEntity playerEntity) {
		if (isAttackRangeAttributeLoaded) {
			return AttackRangeAttributeCompat.getAttackRange(playerEntity);
		} else {
			return playerEntity.getEntityInteractionRange();
		}
	}

	public static RegistryEntry<EntityAttribute> getRangedAttackDamageAttribute() {
		if (isRangedWeaponAPILoaded) {
			return RangedWeaponAPICompatibility.getRangedAttackDamageAttribute();
		} else {
			return EntityAttributes.GENERIC_ATTACK_DAMAGE;
		}
	}

	public static RegistryEntry<EntityAttribute> getRangedAttackSpeedAttribute() {
		if (isRangedWeaponAPILoaded) {
			return RangedWeaponAPICompatibility.getRangedAttackSpeedAttribute();
		} else {
			return EntityAttributes.GENERIC_ATTACK_SPEED;
		}
	}

	public static void updateConditionalSpellContainerItems(PlayerInventory playerInventory) {
		if (isRPGInventoryLoaded) {
			RPGInventoryCompatibility.updateConditionalSpellContainerItems(playerInventory);
		}
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Spell Engine was extended!");
		SERVER_CONFIG = ConfigApiJava.registerAndLoadConfig(ServerConfig::new, RegisterType.BOTH);

		DataComponentPredicateRegistry.init();
		ItemComponentRegistry.init();
		SpellSchoolRegistry.init();

		SpellEvents.SPELL_CAST.register((args) -> {
			if (SERVER_CONFIG.enable_movement_locking_spell_casting.get() && args.spell().isIn(ENABLES_MOVEMENT_LOCKING_DURING_CASTING)) {
				((DuckPlayerEntityMixin) args.caster()).spellengineextension$setMovementLockingTicks(Math.max(0, ((DuckSpellActiveCastMixin) args.spell().value().active.cast).spellengineextension$getAfterCastingMovementLockingTicks()));
			}
		});
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}
}