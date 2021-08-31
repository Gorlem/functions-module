package com.ddoerr.modules.functions.actions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ddoerr.modules.functions.CachedScriptParser;
import com.ddoerr.modules.functions.FunctionMacro;
import com.ddoerr.modules.functions.ModuleInfo;
import com.ddoerr.modules.functions.VariableHandler;
import com.ddoerr.modules.functions.actions.FunctionState.Argument;
import com.ddoerr.modules.functions.parser.ActionParserCall;

import net.eq2online.macros.core.executive.MacroActionProcessor;
import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IMacroActionProcessor;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
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
	

	public ScriptActionCall(String name) {
		super(ScriptContext.MAIN, name);
	}
	
	@Override
	public void onInit() {
		this.context.getCore().registerScriptAction(this);
		this.context.getCore().getParser().addActionParser(new ActionParserCall(context));
	}
	
	@Override
	public boolean canExecuteNow(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams,
			String[] params) {
		State state = instance.getState();
		
		if (state == null) {
			String functionName = params.length == 0 ? "default" : provider.expand(macro, params[0], false);
			
			IMacro parent = macro;
			FunctionState functionState = macro.getState("fn#" + functionName.toLowerCase());
			
			while (functionState == null && parent instanceof FunctionMacro) {
				parent = ((FunctionMacro)parent).getParentMacro();
				functionState = parent.getState("fn#" + functionName.toLowerCase());
			}
			
			if (functionState == null) {
				provider.actionAddChatMessage("Could not find function " + functionName);
				return true;
			}
			
			IMacroActionProcessor actionProcessor = MacroActionProcessor.compile(new CachedScriptParser(functionState.getActions()), "$${}$$", 100, 100, macros);
			IMacro functionMacro = new FunctionMacro(macro, provider);
			
			List<Argument> arguments = functionState.getArguments();
			
			for (int i = 0; i < arguments.size(); i++) {					
				boolean hasRemainingValues = i + 1 < params.length;
				
				Argument argument = arguments.get(i);
					
				try {
					if (!hasRemainingValues && !argument.hasDefaultValue()) {
						break;
					}
					
					if (!argument.isValid()) {
						continue;
					} else if (argument.isCatchAll()) {
						List<String> values = Arrays.stream(params)
							.skip(i + 1)
							.map((value) -> VariableHandler.expand(macro, value))
							.collect(Collectors.toList());
						
						VariableHandler.setArray(functionMacro, argument.getName(), values);
					} else if (argument.isArray()) {
						if (hasRemainingValues) {
							VariableHandler.copyArray(macro, params[i + 1], functionMacro, argument.getName());
						} else {
							String[] defaultValues = (String[])argument.getDefaultValue();							
							VariableHandler.setArray(functionMacro, argument.getName(), defaultValues);
						}
					} else {
						String argumentValue = hasRemainingValues ? params[i + 1] : (String)argument.getDefaultValue();						
						String expandedValue = VariableHandler.expand(macro, argumentValue);
						VariableHandler.setScalar(functionMacro, argument.getName(), expandedValue);
					}
				} catch (Exception e) {
					provider.actionAddChatMessage("Exception happened while trying to handle the " + (i + 1) + ". argument (" + argument.getName() + ") of the function " + functionName);
					
					System.out.println(String.join(",", params));
					e.printStackTrace();
				}
			}
			
			instance.setState(state = new State(actionProcessor, functionMacro));
		}
		
		return !state.actionProcessor.execute(state.macro, state.macro.getContext(), false, true, true);
	}
	
	@Override
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams,
			String[] params) {
		State state = instance.getState();
		
		if (state == null || state.macro.getState("return") == null) {
			// Makes sure that you can always assign the result of a function call to a variable
			// Otherwise it would throw an exception
			return VariableHandler.getEmpty();
		}
		
		IReturnValue returnValue = state.macro.<IReturnValue>getState("return");
		macro.setState("chain_value", returnValue);
		macro.setState("chain_variable", instance.getOutVarName());
		return returnValue;
	}
}
