package com.github.theredbrain.spellengineextension;

import com.github.theredbrain.manaattributes.entity.ManaUsingEntity;
import com.github.theredbrain.spellengineextension.compat.MergedItemsCompatibility;
import com.github.theredbrain.spellengineextension.compat.RPGInventoryCompatibility;
import com.github.theredbrain.spellengineextension.component.type.HasConditionalSpellContainerComponent;
import com.github.theredbrain.spellengineextension.config.ServerConfig;
import com.github.theredbrain.spellengineextension.registry.ItemComponentRegistry;
import com.github.theredbrain.spellengineextension.registry.SpellSchoolRegistry;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.container.SpellContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpellEngineExtension implements ModInitializer {
	public static final String MOD_ID = "spellengineextension";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig SERVER_CONFIG = ConfigApiJava.registerAndLoadConfig(ServerConfig::new, RegisterType.BOTH);

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

	public static final boolean isManaAttributesLoaded = FabricLoader.getInstance().isModLoaded("manaattributes");
	public static final boolean isStaminaAttributesLoaded = FabricLoader.getInstance().isModLoaded("staminaattributes");
	public static final boolean isRangedWeaponAPILoaded = FabricLoader.getInstance().isModLoaded("ranged_weapon_api");
	public static final boolean isMergedItemsLoaded = FabricLoader.getInstance().isModLoaded("mergeditems");
	public static final boolean isRPGInventoryLoaded = FabricLoader.getInstance().isModLoaded("rpginventory");

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
			currentMana = ((ManaUsingEntity) livingEntity).manaattributes$getMana();
		}
		return currentMana;
	}

	public static float getCurrentStamina(LivingEntity livingEntity) {
		float currentStamina = 0.0F;
		if (isStaminaAttributesLoaded) {
			currentStamina = ((StaminaUsingEntity) livingEntity).staminaattributes$getStamina();
		}
		return currentStamina;
	}

	public static void addMana(LivingEntity livingEntity, float amount) {
		if (isManaAttributesLoaded) {
			((ManaUsingEntity) livingEntity).manaattributes$addMana(amount);
		}
	}

	public static void addStamina(LivingEntity livingEntity, float amount) {
		if (isStaminaAttributesLoaded) {
			((StaminaUsingEntity) livingEntity).staminaattributes$addStamina(amount);
		}
	}

	public static RegistryEntry<EntityAttribute> getRangedAttackDamageAttribute() {
		if (isRangedWeaponAPILoaded) {
			return EntityAttributes_RangedWeapon.DAMAGE.entry;
		} else {
			return EntityAttributes.GENERIC_ATTACK_DAMAGE;
		}
	}

	public static RegistryEntry<EntityAttribute> getRangedAttackSpeedAttribute() {
		if (isRangedWeaponAPILoaded) {
			return EntityAttributes_RangedWeapon.HASTE.entry;
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

		ItemComponentRegistry.init();
		SpellSchoolRegistry.init();
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}
}