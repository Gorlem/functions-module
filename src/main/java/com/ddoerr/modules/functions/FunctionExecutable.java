package com.ddoerr.modules.functions;

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
		return macro.<IReturnValue>getState("return");
	}

}
