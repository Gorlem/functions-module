package com.ddoerr.modules.functions.vars;

import com.ddoerr.modules.functions.ModuleInfo;

import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.variable.VariableCache;

@APIVersion(ModuleInfo.API_VERSION)
public class VariableProviderFunctions extends VariableCache {

    @Override
    public void updateVariables(boolean clock) {
        if (!clock) {
            return;
        }
        
        this.storeVariable("MODULEFUNCTIONS", true);
    }

    @Override
    public Object getVariable(String variableName) {
        return this.getCachedValue(variableName);
    }

    @Override
    public void onInit() {
        ScriptContext.MAIN.getCore().registerVariableProvider(this);
    }

}
