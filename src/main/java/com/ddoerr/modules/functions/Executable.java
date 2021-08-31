package com.ddoerr.modules.functions;

import net.eq2online.macros.scripting.api.IReturnValue;

public interface Executable {
	boolean canExecute();
	IReturnValue execute();
}
