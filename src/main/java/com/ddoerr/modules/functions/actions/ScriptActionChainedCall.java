package com.ddoerr.modules.functions.actions;

import java.util.ArrayList;

import com.ddoerr.modules.functions.ModuleInfo;
import com.ddoerr.modules.functions.VariableHandler;
import com.ddoerr.modules.functions.parser.ActionParserChainedCall;
import com.google.common.collect.Lists;

import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IReturnValueArray;
import net.eq2online.macros.scripting.api.IScriptActionProvider;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptActionChainedCall extends ScriptActionCall {	
	public ScriptActionChainedCall() {
		super("chainedcall");
	}
	
	@Override
	public void onInit() {
		this.context.getCore().registerScriptAction(this);
		this.context.getCore().getParser().addActionParser(new ActionParserChainedCall(context));
	}
	
	@Override
	public boolean canExecuteNow(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams,
			String[] params) {
		if (instance.getState() != null) {
			return super.canExecuteNow(provider, macro, instance, rawParams, params);
		}
		
		IReturnValue chainValue = macro.<IReturnValue>getState("chain_value");
		String value;
		
		if (chainValue instanceof IReturnValueArray) {
			value = VariableHandler.getTemporaryArray();
			VariableHandler.setArray(macro, value, chainValue);
		} else {
			value = chainValue.getString();
		}
		
		ArrayList<String> paramsList = Lists.newArrayList(params);
		paramsList.add(1, value);
		String[] chainedParams = paramsList.toArray(new String[0]);
		
		int commaIndex = rawParams.indexOf(',');
		String chainedRawParams;
		if (commaIndex == -1) {
			chainedRawParams = rawParams + ',' + value;
		} else {			
			chainedRawParams = rawParams.substring(0, commaIndex) + ',' + value + "," + rawParams.substring(commaIndex);
		}

		return super.canExecuteNow(provider, macro, instance, chainedRawParams, chainedParams);
	}
	
	@Override
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams,
			String[] params) {
		String chainVariable = macro.getState("chain_variable");
		IReturnValue returnValue = super.execute(provider, macro, instance, rawParams, params);
		macro.setState("chain_variable", chainVariable);
		
		VariableHandler.set(macro, chainVariable, returnValue);

		return returnValue;
	}
}
