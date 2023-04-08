package com.ddoerr.modules.functions.actions;

import java.util.List;
import com.ddoerr.modules.functions.ActionProcessorHandler;
import com.ddoerr.modules.functions.ModuleInfo;
import com.ddoerr.modules.functions.actions.FunctionState.Argument;
import com.ddoerr.modules.functions.parser.ActionParserFunction;

import net.eq2online.macros.core.executive.MacroAction;
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
		context.getCore().registerScriptAction(this);
		context.getParser().addActionParser(new ActionParserFunction(context));
	}
	
	@Override
	public boolean isConditionalOperator() {
		return true;
	}
	
	@Override
	public boolean executeConditional(IScriptActionProvider provider, IMacro macro, IMacroAction instance,
			String rawParams, String[] params) {
		
		if (instance.getActionProcessor().getConditionalExecutionState()) {			
			int start = ActionProcessorHandler.from(instance).getCurrentPointer();
			instance.setState(start);
		}
		
		return false;
	}
	
	@Override
	public boolean executeStackPop(IScriptActionProvider provider, IMacro macro, IMacroAction instance,
			String rawParams, String[] params, IMacroAction popAction) {
		
		if (instance.getState() == null) {
			return true;
		}
		
		ActionProcessorHandler actionProcessorHandler = ActionProcessorHandler.from(instance);
		
		int start = instance.getState();
		int end = actionProcessorHandler.getCurrentPointer();
		
		String functionName = params.length == 0 ? "default" : params[0];
		
		actionProcessorHandler.replaceActions(functionName);
		
		List<IMacroAction> actions = actionProcessorHandler.getActionsBetween(start, end);
		List<Argument> arguments = Argument.tokenize(((MacroAction)instance).getUnparsedParams());

		macro.setState("fn#" + functionName.toLowerCase(), new FunctionState(actions, arguments));
		
		return true;
	}
}
