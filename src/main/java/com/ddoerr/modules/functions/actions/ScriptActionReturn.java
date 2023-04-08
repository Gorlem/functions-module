package com.ddoerr.modules.functions.actions;

import com.ddoerr.modules.functions.FunctionMacro;
import com.ddoerr.modules.functions.ModuleInfo;
import com.ddoerr.modules.functions.VariableHandler;
import com.ddoerr.modules.functions.parser.ActionParserReturn;

import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptActionReturn extends ScriptAction {

	public ScriptActionReturn() {
		super(ScriptContext.MAIN, "return");
	}
	
	@Override
	public void onInit() {
		context.getCore().registerScriptAction(this);
		context.getParser().addActionParser(new ActionParserReturn(context));
	}
	
	@Override
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {		
		IReturnValue returnValue = VariableHandler.parseParameters(instance, macro);
		
		macro.setState("return", returnValue);		
		
		((FunctionMacro)macro).killThis();
		
		return null;
	}
}
