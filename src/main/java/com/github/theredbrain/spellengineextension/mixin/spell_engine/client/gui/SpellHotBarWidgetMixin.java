package com.github.theredbrain.spellengineextension.mixin.spell_engine.client.gui;

import com.github.theredbrain.spellengineextension.SpellEngineExtensionClient;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.Vec2f;
import net.spell_engine.client.SpellEngineClient;
import net.spell_engine.client.gui.Drawable;
import net.spell_engine.client.gui.HudElement;
import net.spell_engine.client.gui.HudRenderHelper;
import net.spell_engine.client.util.Rect;
import net.spell_engine.client.util.TextureFile;
import net.spell_engine.config.HudConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HudRenderHelper.SpellHotBarWidget.class)
public class SpellHotBarWidgetMixin {
	@Shadow(remap = false)
	public static Rect lastRendered;

	@Shadow(remap = false)
	@Final
	private static TextureFile WIDGETS;

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
	 * @reason integrate toggleable spell bar background rendering
	 */
	@Overwrite
	public static void render(DrawContext context, int screenWidth, int screenHeight, HudRenderHelper.SpellHotBarWidget.ViewModel viewModel) {
		HudElement config = ((HudConfig) SpellEngineClient.hudConfig.value).hotbar;
		MinecraftClient client = MinecraftClient.getInstance();
		TextRenderer textRenderer = client.inGameHud.getTextRenderer();
		if (!viewModel.spells().isEmpty()) {
			float estimatedWidth = (float) (20 * viewModel.spells().size());
			float estimatedHeight = 22.0F;
			Vec2f origin = config.origin.getPoint(screenWidth, screenHeight).add(config.offset).add(new Vec2f(estimatedWidth * -0.5F, estimatedHeight * -0.5F));
			lastRendered = new Rect(origin, origin.add(new Vec2f(estimatedWidth, estimatedHeight)));
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			if (SpellEngineExtensionClient.CLIENT_CONFIG.enable_spell_hotbar_background_rendering.get()) {
				float barOpacity = 1.0F;
				context.setShaderColor(1.0F, 1.0F, 1.0F, barOpacity);
				context.drawTexture(WIDGETS.id(), (int) origin.x, (int) origin.y, 0.0F, 0.0F, 10, 22, WIDGETS.width(), WIDGETS.height());
				int middleElements = viewModel.spells().size() - 1;

				for (int i = 0; i < middleElements; ++i) {
					context.drawTexture(WIDGETS.id(), (int) origin.x + 10 + i * 20, (int) origin.y, 10.0F, 0.0F, 20, 22, WIDGETS.width(), WIDGETS.height());
				}

				context.drawTexture(WIDGETS.id(), (int) origin.x + 10 + middleElements * 20, (int) origin.y, 170.0F, 0.0F, 12, 22, WIDGETS.width(), WIDGETS.height());
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
					context.drawTexture(spell.iconId(), x, y, 0.0F, 0.0F, iconSize, iconSize, iconSize, iconSize);
				} else if (spell.itemStack() != null) {
					context.drawItem(spell.itemStack(), x, y);
				}

				if (spell.cooldown() > 0.0F) {
					renderCooldown(context, spell.cooldown(), x, y);
				}

				if (spell.keybinding() != null) {
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
	}
}