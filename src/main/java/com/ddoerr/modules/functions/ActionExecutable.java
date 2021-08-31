package com.ddoerr.modules.functions;

import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;

public class ActionExecutable implements Executable {
	private IMacroAction macroAction;
	private IMacro macro;
	
	public ActionExecutable(IMacroAction macroAction, IMacro macro) {
		this.macroAction = macroAction;
		this.macro = macro;
	}
	
	@Override
	public boolean canExecute() {
		IScriptActionProvider provider = macro.getContext().getScriptContext().getScriptActionProvider();
		return macroAction.getAction().canExecuteNow(provider, macro, macroAction, macroAction.getRawParams(), macroAction.getParams());
	}

	@Override
	public IReturnValue execute() {
		IScriptActionProvider provider = macro.getContext().getScriptContext().getScriptActionProvider();
		return macroAction.getAction().execute(provider, macro, macroAction, macroAction.getRawParams(), macroAction.getParams());
	}

}
