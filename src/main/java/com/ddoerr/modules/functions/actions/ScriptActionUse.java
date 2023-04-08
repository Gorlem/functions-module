package com.ddoerr.modules.functions.actions;

import java.util.ArrayList;
import java.util.List;

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
public class ScriptActionUse extends ScriptAction {

	public ScriptActionUse() {
		super(ScriptContext.MAIN, "use");
	}
	
	@Override
	public void onInit() {
		context.getCore().registerScriptAction(this);
	}
	
	@Override
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {		
		
		if (macro instanceof FunctionMacro) {
			List<String> variables = new ArrayList<String>();
			
			IMacro parentMacro = ((FunctionMacro)macro).getParentMacro();
			
			for (String param : params) {
				if (VariableHandler.isValid(param)) {
					variables.add(param);
					VariableHandler.set(macro, param, VariableHandler.get(parentMacro, param));
				}
			}
			
			macro.setState("use_variables", variables);
		}
		
		return null;
	}
}
