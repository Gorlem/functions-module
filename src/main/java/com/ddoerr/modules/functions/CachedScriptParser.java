package com.ddoerr.modules.functions;

import java.util.List;
import java.util.stream.Collectors;

import net.eq2online.macros.core.executive.MacroAction;
import net.eq2online.macros.scripting.ActionParser;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IMacroActionProcessor;
import net.eq2online.macros.scripting.api.IScriptParser;
import net.eq2online.macros.scripting.parser.ScriptContext;

public class CachedScriptParser implements IScriptParser {
	private final List<IMacroAction> actions;
	
	public CachedScriptParser(List<IMacroAction> actions) {
		this.actions = actions;
	}
	
	@Override
	public void addActionParser(ActionParser parser) {
		
	}

	@Override
	public List<IMacroAction> parseScript(IMacroActionProcessor actionProcessor, String script) {
		return actions.stream()
				.map(action -> new MacroAction(actionProcessor, action.getAction(),
						action.getRawParams(), action.getRawParams(), action.getParams(),
						action.hasOutVar() ? action.getOutVarName() : null))
				.collect(Collectors.toList());
	}

	@Override
	public ScriptContext getContext() {
		return ScriptContext.MAIN;
	}

}
