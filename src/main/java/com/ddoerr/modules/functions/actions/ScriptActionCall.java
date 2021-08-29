package com.ddoerr.modules.functions.actions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ddoerr.modules.functions.CachedScriptParser;
import com.ddoerr.modules.functions.FunctionMacro;
import com.ddoerr.modules.functions.ModuleInfo;
import com.ddoerr.modules.functions.actions.FunctionState.Argument;
import com.ddoerr.modules.functions.parser.ActionParserCall;

import net.eq2online.macros.core.executive.MacroActionProcessor;
import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IMacroActionProcessor;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.IStringProvider;
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
						for (String value : Arrays.stream(params).skip(i + 1).collect(Collectors.toList())) {
							String expandedValue = provider.expand(macro, value, false);
							provider.pushValueToArray(functionMacro, argument.getName(), expandedValue);
						}
					} else if (argument.isArray()) {
						if (hasRemainingValues) {						
							String arrayName = Variable.getValidVariableOrArraySpecifier(params[i + 1]);
							int arraySize = provider.getArraySize(macro, arrayName);
							
							for (int j = 0; j < arraySize; j++) {
								String arrayElement = provider.getArrayElement(macro, arrayName, j).toString();
								
								provider.pushValueToArray(functionMacro, argument.getName(), arrayElement);
							}
						} else {
							String[] defaultValues = (String[])argument.getDefaultValue();
							
							for (String value : defaultValues) {
								provider.pushValueToArray(functionMacro, argument.getName(), value);
							}
						}
					} else {
						String argumentValue = hasRemainingValues ? params[i + 1] : (String)argument.getDefaultValue();
						String expandedValue = provider.expand(macro, argumentValue, false);
						provider.setVariable(functionMacro, argument.getName(), expandedValue);
					}
				} catch (Exception e) {
					provider.actionAddChatMessage("Exception happened while trying to handle the " + (i + 1) + ". argument (" + argument.getName() + ") of the function " + functionName);
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
			return new ReturnValue(IStringProvider.EMPTY);
		}
		
		return state.macro.<IReturnValue>getState("return");
	}
}
