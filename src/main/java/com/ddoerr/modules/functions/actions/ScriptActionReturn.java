package com.ddoerr.modules.functions.actions;

import java.util.ArrayList;
import java.util.List;

import com.ddoerr.modules.functions.ModuleInfo;
import com.ddoerr.modules.functions.parser.ActionParserReturn;

import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IReturnValueArray;
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
		
		if (Variable.couldBeArraySpecifier(rawParams)) {
			String arrayName = Variable.getValidVariableOrArraySpecifier(rawParams);
			
			int arraySize = provider.getArraySize(macro, arrayName);
			List<String> values = new ArrayList<String>();
			
			for (int i = 0; i < arraySize; i++) {
				values.add(provider.getArrayElement(macro, arrayName, i).toString());
			}
			
			returnValue = new ReturnValueArray(false);
			((ReturnValueArray)returnValue).putStrings(values);
		} else if (params.length > 1) {
			List<String> values = new ArrayList<String>();
			
			for (String param : params) {
				values.add(provider.expand(macro, param, false));
			}
			
			returnValue = new ReturnValueArray(false);
			((ReturnValueArray)returnValue).putStrings(values); 
		} else {
			returnValue = new ReturnValue(provider.expand(macro, rawParams, false));
		}
		
		macro.setState("return", returnValue);		
		
		macro.kill();
		
		return null;
	}
}
