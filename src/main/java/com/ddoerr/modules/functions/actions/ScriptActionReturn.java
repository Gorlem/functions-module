package com.ddoerr.modules.functions.actions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ddoerr.modules.functions.ModuleInfo;
import com.ddoerr.modules.functions.VariableHandler;
import com.ddoerr.modules.functions.parser.ActionParserReturn;

import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.ReturnValue;
import net.eq2online.macros.scripting.api.ReturnValueArray;
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
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams,
			String[] params) {		
		IReturnValue returnValue;

		if (VariableHandler.isArray(rawParams)) {
			returnValue = VariableHandler.getArray(macro, rawParams);
		} else if (params.length > 1) {
			List<String> values = Arrays.stream(params)
				.map((value) -> VariableHandler.expand(macro, value))
				.collect(Collectors.toList());
			
			returnValue = new ReturnValueArray(false);
			((ReturnValueArray)returnValue).putStrings(values); 
		} else {
			returnValue = new ReturnValue(VariableHandler.expand(macro, rawParams));
		}
		
		macro.setState("return", returnValue);		
		
		macro.kill();
		
		return null;
	}
}
