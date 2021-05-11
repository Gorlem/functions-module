package com.ddoerr.modules.functions.actions;

import com.ddoerr.modules.functions.ModuleInfo;

import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IScriptAction;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptActionEndfunction extends ScriptAction {
	public ScriptActionEndfunction() {
		super(ScriptContext.MAIN, "endfunction");
	}
	
	@Override
	public void onInit() {
		this.context.getCore().registerScriptAction(this);
	}
	
	@Override
	public boolean isStackPopOperator() {
		return true;
	}
	
	@Override
	public boolean matchesConditionalOperator(IScriptAction action) {
		return action instanceof ScriptActionFunction;
	}
}
