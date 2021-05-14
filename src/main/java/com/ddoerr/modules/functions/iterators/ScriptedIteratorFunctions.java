package com.ddoerr.modules.functions.iterators;

import java.util.Map;

import com.ddoerr.modules.functions.FunctionMacro;
import com.ddoerr.modules.functions.ModuleInfo;

import net.eq2online.macros.scripting.ScriptedIterator;
import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptContext;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptedIteratorFunctions extends ScriptedIterator {
	public ScriptedIteratorFunctions(IScriptActionProvider provider, IMacro macro) {
		super(provider, macro);
		
		while (true) {
			Map<String, Object> stateData = macro.getStateData();
			
			for (Map.Entry<String, Object> entry : stateData.entrySet()) {
				if (!entry.getKey().startsWith("fn#")) {
					continue;
				}
				
				begin();
				
				add("FUNCTIONNAME", entry.getKey().substring(3));
				
				end();
			}
			
			if (macro instanceof FunctionMacro) {
				macro = ((FunctionMacro)macro).getParentMacro();
			} else {
				break;
			}
		}
	}
	
	public ScriptedIteratorFunctions() {
		super(null, null);
	}
	
	@Override
	public void onInit() {
		ScriptContext.MAIN.getCore().registerIterator("functions", this.getClass());
	}

}
