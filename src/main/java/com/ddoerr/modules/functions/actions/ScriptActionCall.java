package com.ddoerr.modules.functions.actions;

import java.util.List;

import com.ddoerr.modules.functions.CachedScriptParser;
import com.ddoerr.modules.functions.FunctionMacro;
import com.ddoerr.modules.functions.ModuleInfo;

import net.eq2online.macros.core.executive.MacroActionProcessor;
import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptActionCall extends ScriptAction {
	public ScriptActionCall() {
		super(ScriptContext.MAIN, "call");
	}
	
	@Override
	public void onInit() {
		this.context.getCore().registerScriptAction(this);
	}
	
	@Override
	public boolean canExecuteNow(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams,
			String[] params) {
		MacroActionProcessor actionProcessor = instance.getState();
		
		if (actionProcessor == null) {
			String functionName = "fn#" + (params.length == 0 ? "default" : params[0]);
			
			IMacro parent = macro;
			List<IMacroAction> actions = macro.getState(functionName);
			
			while (actions == null && macro instanceof FunctionMacro) {
				parent = ((FunctionMacro)parent).getParentMacro();
				actions = parent.getState(functionName);
			}
			
			if (actions == null) {
				provider.actionAddChatMessage("Could not find function");
				return true;
			}
			
			actionProcessor = MacroActionProcessor.compile(new CachedScriptParser(actions), "$${}$$", 100, 100, macros);
			instance.setState(actionProcessor);
		}
		
		IMacro functionMacro = new FunctionMacro(macro, provider);
		
		return !actionProcessor.execute(functionMacro, macro.getContext(), false, true, true);
	}
	
	@Override
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams,
			String[] params) {
		provider.actionAddChatMessage("fun finished");
		return null;
	}
}
