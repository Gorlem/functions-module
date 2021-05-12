package com.ddoerr.modules.functions.actions;

import java.util.List;

import com.ddoerr.modules.functions.CachedScriptParser;
import com.ddoerr.modules.functions.FunctionMacro;
import com.ddoerr.modules.functions.ModuleInfo;

import net.eq2online.macros.core.executive.MacroActionProcessor;
import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IMacroActionProcessor;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.ReturnValue;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptActionCall extends ScriptAction {
	class State {
		private final IMacroActionProcessor actionProcessor;
		private final IMacro macro;
		
		public State(IMacroActionProcessor actionProcessor, IMacro macro) {
			this.actionProcessor = actionProcessor;
			this.macro = macro;
		}
		
		public IMacroActionProcessor getActionProcessor() {
			return actionProcessor;
		}
		
		public IMacro getMacro() {
			return macro;
		}
	}
	
	public ScriptActionCall() {
		super(ScriptContext.MAIN, "call");
	}
	
	@Override
	public void onInit() {
		this.context.getCore().registerScriptAction(this);
	}
	
	@Override
	public boolean canExecuteNow(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams,
			String[] params) {
		State state = instance.getState();
		
		if (state == null) {
			String functionName = "fn#" + (params.length == 0 ? "default" : params[0]);
			
			IMacro parent = macro;
			List<IMacroAction> actions = macro.getState(functionName);
			
			while (actions == null && macro instanceof FunctionMacro) {
				parent = ((FunctionMacro)parent).getParentMacro();
				actions = parent.getState(functionName);
			}
			
			if (actions == null) {
				provider.actionAddChatMessage("Could not find function");
				return true;
			}
			
			IMacroActionProcessor actionProcessor = MacroActionProcessor.compile(new CachedScriptParser(actions), "$${}$$", 100, 100, macros);
			IMacro functionMacro = new FunctionMacro(macro, provider);
			instance.setState(state = new State(actionProcessor, functionMacro));
		}
		
		return !state.actionProcessor.execute(state.macro, state.macro.getContext(), false, true, true);
	}
	
	@Override
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams,
			String[] params) {
		State state = instance.getState();
		return state.macro.<IReturnValue>getState("return");
	}
}
