package com.github.theredbrain.spellengineextension.mixin.spell_engine.client.gui;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.SpellEngineExtensionClient;
import com.github.theredbrain.spellengineextension.config.ClientConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.client.SpellEngineClient;
import net.spell_engine.client.gui.Drawable;
import net.spell_engine.client.gui.HudElement;
import net.spell_engine.client.gui.HudRenderHelper;
import net.spell_engine.client.util.Rect;
import net.spell_engine.client.util.TextureFile;
import net.spell_engine.internals.SpellCooldownManager;
import net.spell_engine.internals.casting.SpellCasterClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(HudRenderHelper.SpellHotBarWidget.class)
public abstract class SpellHotBarWidgetMixin {

	@Shadow(remap = false)
	public static Rect lastRendered;

	@Shadow(remap = false)
	@Final
	private static TextureFile HOTBAR;

	@Shadow
	private static void renderCooldown(DrawContext context, float progress, int x, int y) {
		throw new AssertionError();
	}

	@Shadow
	private static void drawKeybinding(DrawContext context, TextRenderer textRenderer, HudRenderHelper.SpellHotBarWidget.KeyBindingViewModel keybinding, int x, int y, Drawable.Anchor horizontalAnchor, Drawable.Anchor verticalAnchor) {
		throw new AssertionError();
	}

	/**
	 * @author TheRedBrain
	 * @reason integrate spell hotbar rendering options
	 */
	@Overwrite
	public static void render(DrawContext context, int screenWidth, int screenHeight, HudRenderHelper.SpellHotBarWidget.ViewModel viewModel) {
		HudElement config = SpellEngineClient.hudConfig.value.hotbar;
		MinecraftClient client = MinecraftClient.getInstance();
		TextRenderer textRenderer = client.inGameHud.getTextRenderer();
		ClientConfig spellEngineExtensionClientConfig = SpellEngineExtensionClient.CLIENT_CONFIG;
		if (client.world != null && SpellEngineExtension.SERVER_CONFIG.enable_spell_hotbar_use_key_restriction.get() && spellEngineExtensionClientConfig.disable_use_key_spell_hotbar_slot_rendering.get() && SpellEngineClient.config.spellHotbarUseKey) {
			for (int ix = 0; ix < viewModel.spells().size(); ix++) {
				HudRenderHelper.SpellHotBarWidget.SpellViewModel spell = viewModel.spells().get(ix);
				if (spell.iconId() != null) {
					Optional<RegistryEntry.Reference<Spell>> optionalSpellReference = SpellRegistry.from(client.world).getEntry(Identifier.of(spell.iconId().getNamespace(), spell.iconId().getPath().replace("textures/spell/", "").replace(".png", "")));
					if (optionalSpellReference.isPresent() && optionalSpellReference.get().isIn(SpellEngineExtension.CAN_BE_IN_USE_ITEM_SPELL_HOTBAR_SLOT)) {
						viewModel.spells().remove(ix);
						ix--;
					}
				}
			}
		}
		if (viewModel.spells().isEmpty()) {
			return;
		}
		float estimatedWidth = (float) (20 * viewModel.spells().size());
		float estimatedHeight = 22.0F;
		Vec2f origin = config.origin.getPoint(screenWidth, screenHeight).add(config.offset).add(new Vec2f(estimatedWidth * -0.5F, estimatedHeight * -0.5F));
		lastRendered = new Rect(origin, origin.add(new Vec2f(estimatedWidth, estimatedHeight)));
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		if (spellEngineExtensionClientConfig.enable_spell_hotbar_background_rendering.get()) {
			float barOpacity = 1.0F;
			context.setShaderColor(1.0F, 1.0F, 1.0F, barOpacity);
			context.drawTexture(HOTBAR.id(), (int) origin.x, (int) origin.y, 0.0F, 0.0F, 10, 22, HOTBAR.width(), HOTBAR.height());
			int middleElements = viewModel.spells().size() - 1;

			for (int i = 0; i < middleElements; ++i) {
				context.drawTexture(HOTBAR.id(), (int) origin.x + 10 + i * 20, (int) origin.y, 10.0F, 0.0F, 20, 22, HOTBAR.width(), HOTBAR.height());
			}

			context.drawTexture(HOTBAR.id(), (int) origin.x + 10 + middleElements * 20, (int) origin.y, 170.0F, 0.0F, 12, 22, HOTBAR.width(), HOTBAR.height());
		}
		context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		Vec2f iconsOffset = new Vec2f(3.0F, 3.0F);
		int iconSize = 16;

		for (int ix = 0; ix < viewModel.spells().size(); ++ix) {
			HudRenderHelper.SpellHotBarWidget.SpellViewModel spell = (HudRenderHelper.SpellHotBarWidget.SpellViewModel) viewModel.spells().get(ix);
			int x = (int) (origin.x + iconsOffset.x) + 20 * ix;
			int y = (int) (origin.y + iconsOffset.y);
			RenderSystem.enableBlend();
			if (spell.iconId() != null) {
				if (spell.cooldown() > 0.0F && spellEngineExtensionClientConfig.enable_cooldown_icons.get()) {
					context.drawTexture(Identifier.of(spell.iconId().getNamespace(), spell.iconId().getPath().replace(".png", "_cooldown.png")), x, y, 0.0F, 0.0F, iconSize, iconSize, iconSize, iconSize);
				} else {
					context.drawTexture(spell.iconId(), x, y, 0.0F, 0.0F, iconSize, iconSize, iconSize, iconSize);
				}
			} else if (spell.itemStack() != null) {
				context.drawItem(spell.itemStack(), x, y);
			}

			if (spell.cooldown() > 0.0F && spellEngineExtensionClientConfig.enable_spell_hotbar_cooldown_overlay.get()) {
				renderCooldown(context, spell.cooldown(), x, y);
			}

			int remainingCooldown = 0;
			if (spell.iconId() != null) {
				ClientPlayerEntity player = client.player;
				Optional<RegistryEntry.Reference<Spell>> optionalSpellReference = SpellRegistry.from(client.world).getEntry(Identifier.of(spell.iconId().getNamespace(), spell.iconId().getPath().replace("textures/spell/", "").replace(".png", "")));
				if (optionalSpellReference.isPresent() && player != null && !player.isSpectator()) {
					SpellCasterClient caster = (SpellCasterClient) player;
					SpellCooldownManager cooldownManager = caster.getCooldownManager();
					remainingCooldown = cooldownManager.getCooldownDuration(optionalSpellReference.get());
				}
			}

			if (remainingCooldown > 0 && spellEngineExtensionClientConfig.enable_spell_hotbar_cooldown_number.get()) {
				renderCooldownNumber(context, textRenderer, remainingCooldown, x + spellEngineExtensionClientConfig.spell_cooldown_number_offset_x.get(), y + spellEngineExtensionClientConfig.spell_cooldown_number_offset_y.get(), spellEngineExtensionClientConfig.spell_cooldown_number_color.get().toInt());
			}

			if (spell.keybinding() != null && spellEngineExtensionClientConfig.enable_spell_hotkey_icons.get()) {
				int keybindingX = x + iconSize / 2;
				int keybindingY = (int) origin.y + 2;
				if (spell.modifier() != null) {
					keybindingX += 2;
					int spacing = 1;
					int modifierWidth = spell.modifier().width(textRenderer);
					int keybindingWidth = spell.keybinding().width(textRenderer);
					int totalWidth = modifierWidth + keybindingWidth;
					keybindingX -= totalWidth / 2;
					drawKeybinding(context, textRenderer, spell.modifier(), keybindingX, keybindingY, Drawable.Anchor.LEADING, Drawable.Anchor.TRAILING);
					keybindingX += modifierWidth + spacing;
					drawKeybinding(context, textRenderer, spell.keybinding(), keybindingX, keybindingY, Drawable.Anchor.LEADING, Drawable.Anchor.TRAILING);
				} else {
					drawKeybinding(context, textRenderer, spell.keybinding(), keybindingX, keybindingY, Drawable.Anchor.CENTER, Drawable.Anchor.TRAILING);
				}
			}
		}

		RenderSystem.disableBlend();
		context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Unique
	private static void renderCooldownNumber(DrawContext context, TextRenderer textRenderer, int progress, int x, int y, int color) {
		String progressString = String.valueOf((int) Math.ceil((double) Math.max(1, progress) / 20));
		int k = x - textRenderer.getWidth(progressString) / 2;

		context.drawText(textRenderer, progressString, k + 1, y, 0, false);
		context.drawText(textRenderer, progressString, k - 1, y, 0, false);
		context.drawText(textRenderer, progressString, k, y + 1, 0, false);
		context.drawText(textRenderer, progressString, k, y - 1, 0, false);
		context.drawText(textRenderer, progressString, k, y, color, false);
	}

}
