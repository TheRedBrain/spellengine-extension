package com.github.theredbrain.spellengineextension.mixin.spell_engine.api.spell;

import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellContainerMixin;
import net.spell_engine.api.spell.SpellContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SpellContainer.class)
public class SpellContainerMixin implements DuckSpellContainerMixin {
    @Unique
    private String proxy_pool = "";

    @Override
    public String betteradventuremode$getProxyPool() {
        return proxy_pool;
    }

    @Override
    public void betteradventuremode$setProxyPool(String proxyPool) {
        this.proxy_pool = proxyPool;
    }
}
