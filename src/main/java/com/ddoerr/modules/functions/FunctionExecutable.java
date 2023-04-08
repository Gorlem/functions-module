package com.ddoerr.modules.functions;

import java.util.List;

import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroActionProcessor;
import net.eq2online.macros.scripting.api.IReturnValue;

public class FunctionExecutable implements Executable {

	private IMacro macro;
	private IMacroActionProcessor actionProcessor;

	public FunctionExecutable(IMacroActionProcessor actionProcessor, IMacro macro) {
		this.actionProcessor = actionProcessor;
		this.macro = macro;
	}
	
	@Override
	public boolean canExecute() {
		return !actionProcessor.execute(macro, macro.getContext(), false, true, true);
	}

	@Override
	public IReturnValue execute() {
		List<String> useVariables = macro.getState("use_variables");
		
		if (useVariables != null) {
			IMacro parentMacro = ((FunctionMacro)macro).getParentMacro();
			
			for (String variable : useVariables) {
				VariableHandler.set(parentMacro, variable, VariableHandler.get(macro, variable));
			}
		}
		
		return macro.<IReturnValue>getState("return");
	}

}
