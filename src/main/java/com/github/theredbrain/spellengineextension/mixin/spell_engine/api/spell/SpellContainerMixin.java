package com.github.theredbrain.spellengineextension.mixin.spell_engine.api.spell;
//
//import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellContainerMixin;
//import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.spell_engine.api.spell.Spell;
//import net.spell_engine.api.spell.SpellContainer;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Function;
//
//@Mixin(SpellContainer.class)
//public class SpellContainerMixin implements DuckSpellContainerMixin {
//    @Unique
//    private String proxy_pool = "";
//
//    @Override
//    public String betteradventuremode$getProxyPool() {
//        return proxy_pool;
//    }
//
//    @Override
//    public void betteradventuremode$setProxyPool(String proxyPool) {
//        this.proxy_pool = proxyPool;
//    }
//
////	public SpellContainer copy() {
////		return new SpellContainer(this.content, this.is_proxy, this.pool, this.max_spell_count, new ArrayList(this.spell_ids));
////	}
////
////	public SpellContainer copyWith(List<String> spell_ids) {
////		return new SpellContainer(this.content, this.is_proxy, this.pool, this.max_spell_count, spell_ids);
////	}
//
//	@ModifyExpressionValue(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;"))
//	private static Codec<SpellContainer> extendCodec(Codec<SpellContainer> codec) {
//		return RecordCodecBuilder.create(instance -> instance.group(
//				codec,
//				Codec.STRING
//						.optionalFieldOf("proxy_pool", "")
//						.forGetter(x -> ((DuckSpellContainerMixin) (Object) x).betteradventuremode$getProxyPool())
//		).apply(instance, (SpellContainer container, String proxy_pool) -> {
//			((DuckSpellContainerMixin) (Object) container).betteradventuremode$setProxyPool(proxy_pool);
//			return container;
//		}));
//	}
//}
