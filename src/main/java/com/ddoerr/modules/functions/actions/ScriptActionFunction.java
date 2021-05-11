package com.ddoerr.modules.functions.actions;

import java.util.List;

import com.ddoerr.modules.functions.ActionProcessorHandler;
import com.ddoerr.modules.functions.ModuleInfo;

import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptActionFunction extends ScriptAction {

	public ScriptActionFunction() {
		super(ScriptContext.MAIN, "function");
	}
	
	@Override
	public void onInit() {
		this.context.getCore().registerScriptAction(this);
	}
	
	@Override
	public boolean isConditionalOperator() {
		return true;
	}
	
	@Override
	public boolean executeConditional(IScriptActionProvider provider, IMacro macro, IMacroAction instance,
			String rawParams, String[] params) {
		int start = ActionProcessorHandler.from(instance).getCurrentPointer();
		instance.setState(start);
		
		return false;
	}
	
	@Override
	public boolean executeStackPop(IScriptActionProvider provider, IMacro macro, IMacroAction instance,
			String rawParams, String[] params, IMacroAction popAction) {
		ActionProcessorHandler actionProcessorHandler = ActionProcessorHandler.from(instance);
		
		int start = instance.getState();
		int end = actionProcessorHandler.getCurrentPointer();

		provider.actionAddChatMessage(start + " to " + end);
		
		List<IMacroAction> actions = actionProcessorHandler.getActionsBetween(start, end);
		
		for (IMacroAction action : actions) {
			provider.actionAddChatMessage(action.getAction().getName() + ": " + action.getRawParams());
		}
		
		String functionName = params.length == 0 ? "default" : params[0];
		
		macro.setState(functionName, actions);
		
		return true;
	}
}
